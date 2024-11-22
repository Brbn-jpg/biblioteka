package com.api.biblioteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.biblioteka.model.Author;

public interface AuthorRepository extends JpaRepository<Author,Long>{

}
