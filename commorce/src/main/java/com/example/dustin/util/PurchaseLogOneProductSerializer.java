package com.example.dustin.util;

import com.example.dustin.vo.PurchaseLogOneProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

// PurchaseLogOneProduct 객체를 Kafka 메시지로 직렬화하기 위한 클래스
public class PurchaseLogOneProductSerializer implements Serializer<PurchaseLogOneProduct> {

    // Jackson ObjectMapper를 사용하여 객체를 JSON 바이트 배열로 직렬화
    private final ObjectMapper objectMapper = new ObjectMapper();

    // configure 메서드는 직렬화기 초기화에 사용되지만 여기서는 아무 동작도 하지 않음
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    // serialize 메서드는 PurchaseLogOneProduct 객체를 바이트 배열로 직렬화함
    @Override
    public byte[] serialize(String topic, PurchaseLogOneProduct data) {
        try {
            // 데이터가 null인 경우 null 반환
            if (data == null){
                return null;
            }
            // ObjectMapper를 사용하여 PurchaseLogOneProduct 객체를 JSON 바이트 배열로 변환
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            // 직렬화 과정에서 예외가 발생하면 SecurityException을 던짐
            throw new SecurityException("Exception Occurred during serialization");
        }
    }

    // close 메서드는 자원을 정리할 때 사용되지만 여기서는 아무 동작도 하지 않음
    @Override
    public void close() {
    }

}
