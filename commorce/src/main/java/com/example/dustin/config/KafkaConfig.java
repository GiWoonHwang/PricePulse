package com.example.dustin.config;

import com.example.dustin.util.PurchaseLogOneProductSerializer;
import com.example.dustin.util.PurchaseLogSerializer;
import com.example.dustin.util.WatchingAdLogSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
@EnableKafka
public class KafkaConfig {

    // Kafka Streams의 기본 설정을 위한 Bean 정의
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration myKStreamConfig() {
        Map<String, Object> myKStreamConfig = new HashMap<>();

        // Kafka Streams 애플리케이션의 고유 ID 설정
        myKStreamConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "lecture-6");

        // Kafka 클러스터에 연결하기 위한 서버 주소 설정 (로컬호스트 사용)
        myKStreamConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 기본적으로 사용할 키와 값의 Serde 설정 (문자열로 설정)
        myKStreamConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        myKStreamConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // 프로듀서 설정에서 메시지 전송에 대한 확인을 모두 받도록 설정
        myKStreamConfig.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");

        // 토픽 설정에서 최소 동기화 복제본의 개수 설정
        myKStreamConfig.put(StreamsConfig.topicPrefix(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG), 2);

        // Standby 복제본의 개수를 1로 설정
        myKStreamConfig.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1);

        // 설정을 적용한 KafkaStreamsConfiguration 객체 반환
        return new KafkaStreamsConfiguration(myKStreamConfig);
    }

    // 일반적인 목적의 KafkaTemplate Bean 정의
    @Bean
    public KafkaTemplate<String, Object> KafkaTemplateForGeneral() {
        // ProducerFactory를 이용해 KafkaTemplate 생성 및 반환
        return new KafkaTemplate<>(ProducerFactory());
    }

    // Kafka 프로듀서의 설정을 위한 ProducerFactory Bean 정의
    @Bean
    public ProducerFactory<String, Object> ProducerFactory() {
        Map<String, Object> myConfig = new HashMap<>();

        // Kafka 클러스터에 연결하기 위한 서버 주소 설정 (로컬호스트 사용)
        myConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 메시지 키의 직렬화를 위한 설정 (문자열로 설정)
        myConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 메시지 값의 직렬화를 위한 설정 (사용자 정의 Serializer 사용)
        myConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PurchaseLogOneProductSerializer.class);

        // 설정을 적용한 DefaultKafkaProducerFactory 객체 반환
        return new DefaultKafkaProducerFactory<>(myConfig);
    }

    // 광고 시청 로그를 위한 KafkaTemplate Bean 정의
    @Bean
    public KafkaTemplate<String, Object> KafkaTemplateForWatchingAdLog() {
        // ProducerFactoryForWatchingAdLog를 이용해 KafkaTemplate 생성 및 반환
        return new KafkaTemplate<>(ProducerFactoryForWatchingAdLog());
    }

    // 광고 시청 로그 전송을 위한 ProducerFactory Bean 정의
    @Bean
    public ProducerFactory<String, Object> ProducerFactoryForWatchingAdLog() {
        Map<String, Object> myConfig = new HashMap<>();

        // Kafka 클러스터에 연결하기 위한 서버 주소 설정 (로컬호스트 사용)
        myConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 메시지 키의 직렬화를 위한 설정 (문자열로 설정)
        myConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 메시지 값의 직렬화를 위한 설정 (사용자 정의 Serializer 사용)
        myConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, WatchingAdLogSerializer.class);

        // 설정을 적용한 DefaultKafkaProducerFactory 객체 반환
        return new DefaultKafkaProducerFactory<>(myConfig);
    }

    // 구매 로그를 위한 KafkaTemplate Bean 정의
    @Bean
    public KafkaTemplate<String, Object> KafkaTemplateForPurchaseLog() {
        // ProducerFactoryForPurchaseLog를 이용해 KafkaTemplate 생성 및 반환
        return new KafkaTemplate<>(ProducerFactoryForPurchaseLog());
    }

    // 구매 로그 전송을 위한 ProducerFactory Bean 정의
    @Bean
    public ProducerFactory<String, Object> ProducerFactoryForPurchaseLog() {
        Map<String, Object> myConfig = new HashMap<>();

        // Kafka 클러스터에 연결하기 위한 서버 주소 설정 (로컬호스트 사용)
        myConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 메시지 키의 직렬화를 위한 설정 (문자열로 설정)
        myConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 메시지 값의 직렬화를 위한 설정 (사용자 정의 Serializer 사용)
        myConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PurchaseLogSerializer.class);

        // 설정을 적용한 DefaultKafkaProducerFactory 객체 반환
        return new DefaultKafkaProducerFactory<>(myConfig);
    }

    // 필요시 추가할 수 있는 Kafka 소비자 설정 부분 (현재는 주석 처리됨)
    // @Bean
    // public ConsumerFactory<String, Object> ConsumerFactory() {
    //     Map<String, Object> myConfig = new HashMap<>();
    //
    //     // Kafka 클러스터에 연결하기 위한 서버 주소 설정 (로컬호스트 사용)
    //     myConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    //
    //     // 메시지 키의 역직렬화를 위한 설정 (문자열로 설정)
    //     myConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    //
    //     // 메시지 값의 역직렬화를 위한 설정 (문자열로 설정)
    //     myConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    //
    //     // 설정을 적용한 DefaultKafkaConsumerFactory 객체 반환
    //     return new DefaultKafkaConsumerFactory<>(myConfig);
    // }

    // 필요시 추가할 수 있는 KafkaListenerContainerFactory 설정 부분 (현재는 주석 처리됨)
    // @Bean
    // public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
    //     ConcurrentKafkaListenerContainerFactory<String, Object> myfactory = new ConcurrentKafkaListenerContainerFactory<>();
    //
    //     // 위에서 정의한 ConsumerFactory를 설정
    //     myfactory.setConsumerFactory(ConsumerFactory());
    //
    //     // 설정을 적용한 KafkaListenerContainerFactory 객체 반환
    //     return myfactory;
    // }
}
