package com.smartspend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.smartspend.model.TestEntity;
import com.smartspend.repository.TestRepository;


@SpringBootApplication
public class SmartspendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartspendApplication.class, args);
	}
	@Bean
	public CommandLineRunner testDatabase(TestRepository repo) {
		return args -> {
			repo.save(new TestEntity("Database Connected ✅"));
			System.out.println("✅ Test record saved to smartspend_db!");
		};
	}

}
