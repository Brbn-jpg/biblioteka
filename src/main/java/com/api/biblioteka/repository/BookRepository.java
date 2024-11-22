package com.api.biblioteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.biblioteka.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
}
