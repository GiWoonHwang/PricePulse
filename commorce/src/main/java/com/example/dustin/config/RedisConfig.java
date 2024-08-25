package com.example.dustin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration  // 이 클래스가 Spring의 구성 클래스임을 나타냅니다.
@RequiredArgsConstructor  // final 필드에 대한 생성자를 자동으로 생성합니다.
public class RedisConfig {

    private final RedisProperties redisProperties;  // Spring Boot의 Redis 설정 속성을 주입받는 필드

    @Bean  // Redis 연결을 위한 RedisConnectionFactory Bean 정의
    public RedisConnectionFactory redisConnectionFactory() {
        // LettuceConnectionFactory를 생성하여 Redis 서버에 대한 연결을 설정
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    @Primary  // 이 메서드가 기본적으로 사용될 RedisTemplate을 정의함을 나타냅니다.
    public RedisTemplate<String, Object> redisTemplate() {
        // RedisTemplate을 생성하여 Redis와의 데이터 입출력을 관리
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 앞서 정의한 RedisConnectionFactory를 설정하여 Redis 서버와의 연결을 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Redis 키와 값을 직렬화할 때 사용할 직렬화 방식을 StringRedisSerializer로 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        // 설정이 완료된 RedisTemplate을 반환
        return redisTemplate;
    }

}
