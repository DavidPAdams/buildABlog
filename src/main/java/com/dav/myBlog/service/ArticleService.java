package com.dav.myBlog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dav.myBlog.model.Article;
import com.dav.myBlog.repository.ArticleRepository;

@Service
public class ArticleService {
  @Autowired
  private ArticleRepository articleRepository;
  
  public List<Article> findAll() {
    List<Article> articles = articleRepository.findAllByOrderByCreatedAtDesc();
    return articles;
  }
}
