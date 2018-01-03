package com.vwtest.video.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.vwtest.video.domain.File;

@RepositoryRestResource(path = "files", collectionResourceRel = "files")
public interface FileRepository extends JpaRepository<File, Long> {
	Optional<File> findByName(String name);

	Optional<File> findByNameAndUserId(String name, Long userId);
}