package com.example.dustin.service.promotion;

import com.example.dustin.vo.EffectOrNot;
import com.example.dustin.vo.PurchaseLog;
import com.example.dustin.vo.PurchaseLogOneProduct;
import com.example.dustin.vo.WatchingAdLog;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service  // Spring의 서비스 컴포넌트로 등록
public class AdEvaluationService {

    // Kafka 프로듀서를 나타내는 객체 (메서드에서 사용됨)
    Producer myprdc;

    @Autowired  // Spring이 StreamsBuilder 인스턴스를 주입
    public void buildPipeline(StreamsBuilder sb) {

        // 각 VO에 대한 Serializer와 Deserializer를 설정
        JsonSerializer<EffectOrNot> effectSerializer = new JsonSerializer<>();
        JsonSerializer<PurchaseLog> purchaseLogSerializer = new JsonSerializer<>();
        JsonSerializer<WatchingAdLog> watchingAdLogSerializer = new JsonSerializer<>();
        JsonSerializer<PurchaseLogOneProduct> purchaseLogOneProductSerializer = new JsonSerializer<>();

        JsonDeserializer<EffectOrNot> effectDeserializer = new JsonDeserializer<>(EffectOrNot.class);
        JsonDeserializer<PurchaseLog> purchaseLogDeserializer = new JsonDeserializer<>(PurchaseLog.class);
        JsonDeserializer<WatchingAdLog> watchingAdLogJsonDeserializer = new JsonDeserializer<>(WatchingAdLog.class);
        JsonDeserializer<PurchaseLogOneProduct> purchaseLogOneProductDeserializer = new JsonDeserializer<>(PurchaseLogOneProduct.class);

        // Serde 객체 생성: Serializer와 Deserializer를 합쳐서 사용
        Serde<EffectOrNot> effectOrNotSerde = Serdes.serdeFrom(effectSerializer, effectDeserializer);
        Serde<PurchaseLog> purchaseLogSerde = Serdes.serdeFrom(purchaseLogSerializer, purchaseLogDeserializer);
        Serde<WatchingAdLog> watchingAdLogSerde = Serdes.serdeFrom(watchingAdLogSerializer, watchingAdLogJsonDeserializer);
        Serde<PurchaseLogOneProduct> purchaseLogOneProductSerde = Serdes.serdeFrom(purchaseLogOneProductSerializer, purchaseLogOneProductDeserializer);

        // adLog 토픽을 소비하여 KTable로 수신
        KTable<String, WatchingAdLog> adTable = sb.stream("adLog", Consumed.with(Serdes.String(), watchingAdLogSerde))
                .selectKey((k, v) -> v.getUserId() + "_" + v.getProductId())  // userId와 productId를 조합하여 키 생성
                .filter((k, v) -> Integer.parseInt(v.getWatchingTime()) > 10)  // 시청 시간이 10초 이상인 경우만 필터링
                .toTable(Materialized.<String, WatchingAdLog, KeyValueStore<Bytes, byte[]>>as("adStore")  // KTable로 변환
                        .withKeySerde(Serdes.String())  // 키의 Serde 설정
                        .withValueSerde(watchingAdLogSerde)  // 값의 Serde 설정
                );

        // purchaseLog 토픽을 소비하여 KStream으로 수신
        KStream<String, PurchaseLog> purchaseLogKStream = sb.stream("purchaseLog", Consumed.with(Serdes.String(), purchaseLogSerde));

        // 각 메시지에 대해 수행할 작업 정의
        purchaseLogKStream.foreach((k, v) -> {

            // 각 제품 정보에 대해 반복
            for (Map<String, String> prodInfo : v.getProductInfo()) {
                // 가격이 1,000,000원 미만인 경우에만 처리
                if (Integer.parseInt(prodInfo.get("price")) < 1000000) {
                    // PurchaseLogOneProduct 객체를 생성하고 필요한 정보를 설정
                    PurchaseLogOneProduct tempVo = new PurchaseLogOneProduct();
                    tempVo.setUserId(v.getUserId());
                    tempVo.setProductId(prodInfo.get("productId"));
                    tempVo.setOrderId(v.getOrderId());
                    tempVo.setPrice(prodInfo.get("price"));
                    tempVo.setPurchasedDt(v.getPurchasedDt());

                    // 생성된 객체를 purchaseLogOneProduct 토픽으로 전송
                    myprdc.sendJoinedMsg("purchaseLogOneProduct", tempVo);

                    // 샘플 데이터 생성 메서드를 호출하여 무한히 메시지를 생성
                    // sendNewMsg();
                }
            }
        });

        // purchaseLogOneProduct 토픽을 소비하여 KTable로 수신
        KTable<String, PurchaseLogOneProduct> purchaseLogOneProductKTable = sb.stream("purchaseLogOneProduct", Consumed.with(Serdes.String(), purchaseLogOneProductSerde))
                .selectKey((k, v) -> v.getUserId() + "_" + v.getProductId())  // userId와 productId를 조합하여 키 생성
                .toTable(Materialized.<String, PurchaseLogOneProduct, KeyValueStore<Bytes, byte[]>>as("purchaseLogStore")  // KTable로 변환
                        .withKeySerde(Serdes.String())  // 키의 Serde 설정
                        .withValueSerde(purchaseLogOneProductSerde)  // 값의 Serde 설정
                );

        // ValueJoiner를 사용하여 두 KTable을 조인
        ValueJoiner<WatchingAdLog, PurchaseLogOneProduct, EffectOrNot> tableStreamJoiner = (leftValue, rightValue) -> {
            EffectOrNot returnValue = new EffectOrNot();
            returnValue.setUserId(rightValue.getUserId());
            returnValue.setAdId(leftValue.getAdId());
            returnValue.setOrderId(rightValue.getOrderId());
            Map<String, String> tempProdInfo = new HashMap<>();
            tempProdInfo.put("productId", rightValue.getProductId());
            tempProdInfo.put("price", rightValue.getPrice());
            returnValue.setProductInfo(tempProdInfo);
            System.out.println("Joined!");
            return returnValue;
        };

        // adTable과 purchaseLogOneProductKTable을 조인하고, 결과를 AdEvaluationComplete 토픽으로 전송
        adTable.join(purchaseLogOneProductKTable, tableStreamJoiner)
                .toStream()
                .to("AdEvaluationComplete", Produced.with(Serdes.String(), effectOrNotSerde));
    }

    // 랜덤하게 새로운 메시지를 생성하여 특정 토픽에 전송하는 메서드
    public void sendNewMsg() {
        PurchaseLog tempPurchaseLog = new PurchaseLog();
        WatchingAdLog tempWatchingAdLog = new WatchingAdLog();

        // 랜덤한 ID를 생성하기 위한 코드
        Random rd = new Random();
        int rdUidNumber = rd.nextInt(9999);
        int rdOrderNumber = rd.nextInt(9999);
        int rdProdIdNumber = rd.nextInt(9999);
        int rdPriceIdNumber = rd.nextInt(90000) + 10000;
        int prodCnt = rd.nextInt(9) + 1;
        int watchingTime = rd.nextInt(55) + 5;

        // PurchaseLog 객체에 랜덤한 데이터 설정
        tempPurchaseLog.setUserId("uid-" + String.format("%05d", rdUidNumber));
        tempPurchaseLog.setPurchasedDt("20230101070000");
        tempPurchaseLog.setOrderId("od-" + String.format("%05d", rdOrderNumber));
        ArrayList<Map<String, String>> tempProdInfo = new ArrayList<>();
        Map<String, String> tempProd = new HashMap<>();
        for (int i = 0; i < prodCnt; i++) {
            tempProd.put("productId", "pg-" + String.format("%05d", rdProdIdNumber));
            tempProd.put("price", String.format("%05d", rdPriceIdNumber));
            tempProdInfo.add(tempProd);
        }
        tempPurchaseLog.setProductInfo(tempProdInfo);

        // WatchingAdLog 객체에 랜덤한 데이터 설정
        tempWatchingAdLog.setUserId("uid-" + String.format("%05d", rdUidNumber));
        tempWatchingAdLog.setProductId("pg-" + String.format("%05d", rdProdIdNumber));
        tempWatchingAdLog.setAdId("ad-" + String.format("%05d", rdUidNumber));
        tempWatchingAdLog.setAdType("banner");
        tempWatchingAdLog.setWatchingTime(String.valueOf(watchingTime));
        tempWatchingAdLog.setWatchingDt("20230201070000");

        // 생성된 메시지를 특정 토픽에 전송
        myprdc.sendMsgForPurchaseLog("purchaseLog", tempPurchaseLog);
        myprdc.sendMsgForWatchingAdLog("adLog", tempWatchingAdLog);
    }
}
