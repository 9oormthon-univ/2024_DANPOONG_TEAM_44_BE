package org.danpoong.zipcock_44;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.util.*;

@SpringBootApplication
@EnableJpaAuditing
public class ZipCock44Application {

    public static void main(String[] args) {
        SpringApplication.run(ZipCock44Application.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        //현재는 랜덤 uuid를 넣었지만
        //실무에서 사용할 때에는 security holder에서 꺼내서 쓰던가 세션에서 꺼내서 저장해야함.
        return () -> Optional.of(UUID.randomUUID().toString());
    }

}


