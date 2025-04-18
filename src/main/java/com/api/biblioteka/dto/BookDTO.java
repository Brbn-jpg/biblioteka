package com.api.biblioteka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookDTO {

  private Long id;
  private String name;
  private AuthorDTO author;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AuthorDTO getAuthor() {
    return author;
  }

  public void setAuthor(AuthorDTO author) {
    this.author = author;
  }


  public static class AuthorDTO {
      public AuthorDTO(Long id, String firstName2, String lastName2) {
        this.firstName = firstName2;
        this.lastName = lastName2;
        this.id = id;
    }
    
    public long getId() {
      return id;
    }

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("author_id")
    private long id;
  }
}