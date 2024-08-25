package com.example.dustin.service.promotion;

import com.example.dustin.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service  // 이 클래스가 Spring의 서비스 컴포넌트임을 나타냅니다.
public class Producer {

    @Autowired
    KafkaConfig myConfig;  // KafkaConfig 빈을 자동으로 주입 받습니다.

    String topicName = "defaultTopic";  // 기본적으로 사용할 Kafka 토픽 이름을 설정합니다.

    private KafkaTemplate<String, Object> kafkaTemplate;  // Kafka 메시지를 전송할 때 사용할 KafkaTemplate 객체입니다.

    // KafkaTemplate을 생성자로 주입받아 초기화하는 생성자입니다.
    // 이 생성자는 현재 주석 처리되어 사용되지 않습니다.
    // public Producer(KafkaTemplate kafkaTemplate) {
    //     this.kafkaTemplate = kafkaTemplate;
    // }

    // 기본 KafkaTemplate을 사용하여 'defaultTopic'에 메시지를 발행하는 메서드입니다.
    public void pub(String msg) {
        kafkaTemplate = myConfig.KafkaTemplateForGeneral();  // KafkaConfig에서 KafkaTemplate을 가져옵니다.
        kafkaTemplate.send(topicName, msg);  // 지정된 토픽에 메시지를 전송합니다.
    }

    // 특정 토픽에 조인된 메시지를 발행하는 메서드입니다.
    public void sendJoinedMsg(String topicNm, Object msg) {
        kafkaTemplate = myConfig.KafkaTemplateForGeneral();  // KafkaConfig에서 KafkaTemplate을 가져옵니다.
        kafkaTemplate.send(topicNm, msg);  // 지정된 토픽에 메시지를 전송합니다.
    }

    // 특정 토픽에 광고 로그 메시지를 발행하는 메서드입니다.
    public void sendMsgForWatchingAdLog(String topicNm, Object msg) {
        kafkaTemplate = myConfig.KafkaTemplateForWatchingAdLog();  // KafkaConfig에서 광고 로그용 KafkaTemplate을 가져옵니다.
        kafkaTemplate.send(topicNm, msg);  // 지정된 토픽에 메시지를 전송합니다.
    }

    // 특정 토픽에 구매 로그 메시지를 발행하는 메서드입니다.
    public void sendMsgForPurchaseLog(String topicNm, Object msg) {
        kafkaTemplate = myConfig.KafkaTemplateForPurchaseLog();  // KafkaConfig에서 구매 로그용 KafkaTemplate을 가져옵니다.
        kafkaTemplate.send(topicNm, msg);  // 지정된 토픽에 메시지를 전송합니다.
    }
}
