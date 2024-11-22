package com.api.biblioteka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Book {

  private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long book_id;


  public Long getId() {
    return book_id;
}

public void setId(Long book_id) {
    this.book_id = book_id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public Author getAuthor() {
    return author;
}

public void setAuthor(Author author) {
    this.author = author;
}

@Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id", nullable = true)
  @JsonIgnore
  private Author author;

  public Book() {
  }

}