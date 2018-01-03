package com.vwtest.video;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vwtest.video.domain.Users;
import com.vwtest.video.repositories.UsersRepository;

@SpringBootApplication
@ComponentScan
public class VWVideoRestApplication implements CommandLineRunner {

	@Autowired
	UsersRepository repository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(VWVideoRestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repository.save(new Users("User1", passwordEncoder.encode("User1"), "User1@vwtest.com", Arrays.asList("USER")));
		repository.save(new Users("User2", passwordEncoder.encode("User2"), "User2@vwtest.com", Arrays.asList("USER")));
		repository.save(new Users("User3", passwordEncoder.encode("User3"), "User3@vwtest.com", Arrays.asList("USER")));
		repository.save(new Users("Admin1", passwordEncoder.encode("Admin1"), "Admin1@vwtest.com",
				Arrays.asList("USER", "ADMIN")));
	}
}
