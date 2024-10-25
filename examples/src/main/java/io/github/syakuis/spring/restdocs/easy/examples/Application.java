package io.github.syakuis.spring.restdocs.easy.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
@SpringBootApplication(proxyBeanMethods = false)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
