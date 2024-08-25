package com.example.dustin.util;

import com.example.dustin.vo.PurchaseLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

// PurchaseLog 객체를 Kafka 메시지로 직렬화하기 위한 클래스
public class PurchaseLogSerializer implements Serializer<PurchaseLog> {

    // Jackson ObjectMapper를 사용하여 객체를 JSON 바이트 배열로 직렬화
    private final ObjectMapper objectMapper = new ObjectMapper();

    // configure 메서드는 직렬화기 초기화에 사용되지만 여기서는 아무 동작도 하지 않음
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // 이 메서드는 직렬화기가 필요로 하는 설정을 받을 수 있도록 구현할 수 있지만,
        // 현재 사용되지 않기 때문에 빈 메서드로 유지됩니다.
    }

    // serialize 메서드는 PurchaseLog 객체를 바이트 배열로 직렬화함
    @Override
    public byte[] serialize(String topic, PurchaseLog data) {
        try {
            // 직렬화할 데이터가 null이면 null을 반환하여 아무 동작도 하지 않음
            if (data == null){
                return null;
            }
            // ObjectMapper를 사용하여 PurchaseLog 객체를 JSON 형식의 바이트 배열로 변환
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            // 직렬화 과정에서 예외가 발생하면 SecurityException을 던져 오류를 알림
            throw new SecurityException("Exception Occurred during serialization");
        }
    }

    // close 메서드는 자원을 정리할 때 사용되지만 여기서는 아무 동작도 하지 않음
    @Override
    public void close() {
        // 이 메서드는 직렬화기가 더 이상 필요하지 않을 때 호출되며,
        // 자원을 정리하는 용도로 사용할 수 있지만, 현재는 빈 메서드로 유지됩니다.
    }

}
