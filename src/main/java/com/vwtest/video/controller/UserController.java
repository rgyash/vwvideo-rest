package com.vwtest.video.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ActuatorMetricWriter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vwtest.video.domain.Users;
import com.vwtest.video.service.CustomUserDetailsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "User Registartion and Login API")
@RequestMapping("/rest/")
@ActuatorMetricWriter
public class UserController {

	private @Autowired CustomUserDetailsService userService;
	private @Autowired PasswordEncoder passwordEncoder;


	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/account")
	@ApiOperation(value = "User Registartion API")
	public Users onPostAccount(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("email") String email) {
		LOGGER.info("Registering user {} started", username);
		final Users user = userService
				.persist(new Users(username, passwordEncoder.encode(password), email, Arrays.asList("USER")));
		LOGGER.info("Registering user {} completed", username);
		return user;
	}

	@GetMapping("/account/all")
	@ApiOperation(value = "Get All Users API")
	@PreAuthorize("hasAnyRole('ADMIN')")
	List<Users> onGetAllAccounts() {
		return userService.getAllUsers();
	}

	@GetMapping("/account/{username}")
	@ApiOperation(value = "Get User API")
	@PreAuthorize("hasAnyRole('USER')")
	public Users onGetAccount(@PathVariable("username") String username, Authentication auth) {

		final Users user = (Users) auth.getPrincipal();
		if (user.getName() != null && user.getName().equals(username)) {
			return user;
		} else {
			throw new ForbiddenException();
		}
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	private final static class ForbiddenException extends RuntimeException {
		private static final long serialVersionUID = -4791719277573281730L;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	private final static class NotFoundException extends RuntimeException {
		private static final long serialVersionUID = 2071374314032968634L;
	}
}