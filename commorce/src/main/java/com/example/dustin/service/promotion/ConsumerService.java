package com.example.dustin.service.promotion;

import org.springframework.stereotype.Service;

@Service  // 이 클래스가 Spring의 서비스 컴포넌트임을 나타냅니다.
public class ConsumerService {

    // Kafka 메시지를 소비하기 위한 리스너 메서드입니다.
    // 현재는 주석 처리되어 있어 동작하지 않습니다.

    // @KafkaListener 애너테이션을 사용하여 Kafka 메시지를 소비하는 메서드를 정의합니다.
    // 주어진 토픽("fastcampus")과 소비자 그룹 ID("foo")를 지정합니다.
    // @KafkaListener(topics = "fastcampus", groupId = "foo")
    // public void consumer(String message) {
    //     // 수신한 메시지를 콘솔에 출력합니다.
    //     System.out.println(String.format("Subscribed :  %s", message));
    // }

}
