package com.folio4me.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션 공통 설정.
 */
@Configuration
public class AppConfig {

    @Value("${folio.data.base-path:data}")
    private String dataBasePath;

    /**
     * JSON 직렬화/역직렬화를 위한 ObjectMapper 빈.
     * Java 8 날짜/시간 타입 지원 및 이쁜 출력 설정.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    public String getDataBasePath() {
        return dataBasePath;
    }
}
