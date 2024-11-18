package org.danpoong.zipcock_44;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZipCock44Application {

    public static void main(String[] args) {
        SpringApplication.run(ZipCock44Application.class, args);
    }

}
