package com.example.dustin.controller;

import com.example.dustin.service.promotion.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController  // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
public class ProducerController {

    Producer producer;  // 메시지를 발행하는 데 사용할 Producer 서비스입니다.

    @Autowired  // Spring이 이 생성자를 사용하여 의존성을 주입하도록 설정합니다.
    ProducerController(Producer producer) {
        this.producer = producer;  // 주입된 Producer 인스턴스를 클래스의 멤버 변수로 설정합니다.
    }

    // /message 경로로 들어오는 POST 요청을 처리하는 메서드입니다.
    @PostMapping("/message")
    public void PublishMessage(@RequestParam String msg) {
        // 요청 매개변수로 전달된 메시지를 Kafka에 발행합니다.
        producer.pub(msg);
    }
}
