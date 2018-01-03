package com.vwtest.video.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vwtest.video.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByName(String username);
}
