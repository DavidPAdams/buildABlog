package com.tts.myBlog.model;

import java.util.Date;

public class Article {
  private Long id; 
  private String title;
  private String author;
  private String entry;
  private Date createdAt;
  
  public Article() {}

  public Article(String title, String author, String entry) {
    this.title = title;
    this.author = author;
    this.entry = entry;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getEntry() {
    return entry;
  }

  public void setEntry(String entry) {
    this.entry = entry;
  }

  public Long getId() {
    return id;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return "Article [id=" + id + ", title=" + title + ", author=" + author + ", entry=" + entry + "]";
  }
  
}
