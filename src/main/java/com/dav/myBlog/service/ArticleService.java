package com.tts.myBlog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tts.myBlog.model.Article;
import com.tts.myBlog.repository.ArticleRepository;

@Service
public class ArticleService {
  @Autowired
  private ArticleRepository articleRepository;
  
  public List<Article> findAll() {
    List<Article> articles = articleRepository.findAllByOrderByCreatedAtDesc();
    return articles;
  }
}
