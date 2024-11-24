package com.api.biblioteka.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Author {

  private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long author_id;


  public Long getAuthor_id() {
    return author_id;
}

public void setAuthor_id(Long author_id) {
    this.author_id = author_id;
}

public String getFirstName() {
    return firstName;
}

public void setFirstName(String firstName) {
    this.firstName = firstName;
}

public String getLastName() {
    return lastName;
}

public void setLastName(String lastName) {
    this.lastName = lastName;
}

public List<Book> getBooks() {
    return books;
}

public void setBooks(List<Book> books) {
    this.books = books;
}

@Column(nullable = false)
  private String firstName;


  @Column(nullable = false)
  private String lastName;


  @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Book> books;

  public Author() {
  }

}