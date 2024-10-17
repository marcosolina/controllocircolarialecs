package com.alecs.controllocircolariscuola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControllocircolariscuolaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControllocircolariscuolaApplication.class, args);
	}

}
