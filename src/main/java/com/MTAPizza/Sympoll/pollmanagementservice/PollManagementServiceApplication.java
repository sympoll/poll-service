package com.MTAPizza.Sympoll.pollmanagementservice;

import com.MTAPizza.Sympoll.pollmanagementservice.controller.ServiceController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class PollManagementServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PollManagementServiceApplication.class, args);
	}

}
