package com.example.dustin.service.promotion;

import org.springframework.stereotype.Service;

@Service  // 이 클래스가 Spring의 서비스 컴포넌트임을 나타냅니다.
public class KTableService {

    // Kafka Streams를 사용하여 두 개의 Kafka 토픽을 KTable로 변환하고, 그들을 조인하는 메서드입니다.
    // 현재는 주석 처리되어 있어 동작하지 않습니다.

    // @Autowired 애너테이션을 사용하여 Spring이 StreamsBuilder 인스턴스를 주입하도록 설정할 수 있습니다.
    // 주석을 해제하고 필요한 의존성을 추가하면, Kafka Streams 파이프라인을 설정할 수 있습니다.

    // @Autowired
    // public void buildPipeline(StreamsBuilder sb) {
    //
    //     // "leftTopic" 토픽을 소비하여 KStream으로 받고, 이를 KTable로 변환합니다.
    //     KTable<String, String> leftTable = sb.stream("leftTopic", Consumed.with(Serdes.String(), Serdes.String())).toTable();
    //
    //     // "rightTopic" 토픽을 소비하여 KStream으로 받고, 이를 KTable로 변환합니다.
    //     KTable<String, String> rightTable = sb.stream("rightTopic", Consumed.with(Serdes.String(), Serdes.String())).toTable();
    //
    //     // 두 개의 KTable을 조인할 때 사용할 ValueJoiner를 정의합니다.
    //     // 이 예제에서는 두 값(leftValue와 rightValue)을 연결하여 문자열을 반환합니다.
    //     ValueJoiner<String, String, String> stringJoiner = (leftValue, rightValue) -> {
    //         return "[StringJoiner]" + leftValue + "-" + rightValue;
    //     };
    //
    //     // 두 개의 KTable을 조인합니다.
    //     KTable<String, String> joinedTable = leftTable.join(rightTable, stringJoiner);
    //
    //     // 조인된 결과를 "joinedMsg" 토픽으로 내보냅니다.
    //     joinedTable.toStream().to("joinedMsg");
    // }
}
