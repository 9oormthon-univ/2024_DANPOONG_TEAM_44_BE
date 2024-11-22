package org.danpoong.zipcock_44.global.security.config;

import java.util.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // RestTemplate 클래스 : Spring 에서 HTTP 요청을 처리 하는 클래스로 RESTful 웹 서비스와 상호작용할 때 , 사용
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        // HttpMessageConverter : HTTP 요청 및 응답 데이터를 변환하는데 사용되는 객체
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new FormHttpMessageConverter());
        // 새로운 FormHttpMessageConverter객체를 넣고 restTemplate에 저장
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }
}
