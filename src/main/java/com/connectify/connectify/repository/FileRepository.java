package com.connectify.connectify.repository;

import com.connectify.connectify.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
