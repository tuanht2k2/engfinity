package com.kma.engfinity.repository;

import com.kma.engfinity.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
