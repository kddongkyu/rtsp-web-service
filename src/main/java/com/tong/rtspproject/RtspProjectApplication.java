package com.tong.rtspproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "data")
@EntityScan(basePackages = "data")
@EnableJpaRepositories(basePackages = "data.repository")
public class RtspProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtspProjectApplication.class, args);
	}

}
