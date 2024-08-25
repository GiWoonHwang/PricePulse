package com.example.dustin.service.promotion;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Service;

@Service  // 이 클래스가 Spring의 서비스 컴포넌트임을 나타냅니다.
public class StreamService {

    private static final Serde<String> STRING_SERDE = Serdes.String();  // String 타입의 Serde 객체를 생성하여 Kafka 스트림에서 사용합니다.

    // Kafka Streams API를 사용하여 스트림을 처리하는 메서드입니다.
    // 현재는 주석 처리되어 있어 동작하지 않습니다.

    // @Autowired
    // public void buildPipeline(StreamsBuilder sb) {
    //
    //     // "fastcampus" 토픽을 소비하여 KStream을 생성하고 콘솔에 출력합니다.
    //     // 특정 조건(value가 "freeClass"를 포함하는 경우)에 맞는 데이터를 "freeClassList" 토픽으로 전송합니다.
    //     // KStream<String, String> myStream = sb.stream("fastcampus", Consumed.with(STRING_SERDE, STRING_SERDE));
    //     // myStream.print(Printed.toSysOut());
    //     // myStream.filter((key, value) -> value.contains("freeClass")).to("freeClassList");
    //
    //     // "leftTopic" 토픽을 소비하여 KStream을 생성합니다.
    //     KStream<String, String> leftStream = sb.stream("leftTopic", Consumed.with(STRING_SERDE, STRING_SERDE));
    //     // "rightTopic" 토픽을 소비하여 KStream을 생성합니다.
    //     KStream<String, String> rightStream = sb.stream("rightTopic", Consumed.with(STRING_SERDE, STRING_SERDE));
    //
    //     // 두 KStream을 조인할 때 사용할 ValueJoiner를 정의합니다.
    //     // 이 예제에서는 두 값(leftValue와 rightValue)을 연결하여 문자열을 반환합니다.
    //     ValueJoiner<String, String, String> stringJoiner = (leftValue, rightValue) -> {
    //         return "[StringJoiner]" + leftValue + "-" + rightValue;
    //     };
    //
    //     // 두 KStream을 아우터 조인할 때 사용할 ValueJoiner를 정의합니다.
    //     // 이 예제에서는 두 값(leftValue와 rightValue)을 연결하여 문자열을 반환합니다.
    //     ValueJoiner<String, String, String> stringOuterJoiner = (leftValue, rightValue) -> {
    //         return "[StringOuterJoiner]" + leftValue + "<" + rightValue;
    //     };
    //
    //     // "leftStream"과 "rightStream"을 조인합니다.
    //     KStream<String, String> joinedStream = leftStream.join(rightStream,
    //             stringJoiner,
    //             JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(10)));
    //
    //     // "leftStream"과 "rightStream"을 아우터 조인합니다.
    //     KStream<String, String> outerJoinedStream = leftStream.outerJoin(rightStream,
    //             stringOuterJoiner,
    //             JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(10)));
    //
    //     // 조인된 스트림을 콘솔에 출력합니다.
    //     joinedStream.print(Printed.toSysOut());
    //     // 조인된 결과를 "joinedMsg" 토픽으로 전송합니다.
    //     joinedStream.to("joinedMsg");
    //     // 아우터 조인된 결과를 "joinedMsg" 토픽으로 전송합니다.
    //     outerJoinedStream.to("joinedMsg");
    // }
}
