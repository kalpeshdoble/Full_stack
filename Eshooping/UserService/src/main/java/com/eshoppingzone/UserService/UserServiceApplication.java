package com.eshoppingzone.UserService;

import com.eshoppingzone.UserService.model.User;
import com.eshoppingzone.UserService.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableJpaRepositories(basePackages = "com.eshoppingzone.UserService.repository")
//@EntityScan(basePackages = "com.eshoppingzone.UserService.model")
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminEmail = "admin@eshoppingzone.com";
			if (!userRepository.existsByEmail(adminEmail)) {
				User admin = new User();
				admin.setUsername("ADMIN");
				admin.setEmail(adminEmail);
				admin.setPassword(passwordEncoder.encode("Admin@123")); // strong password!
				admin.setRole("MERCHANT");
				userRepository.save(admin);
				System.out.println("Admin user created: " + adminEmail);
			}
		};
	}

}

