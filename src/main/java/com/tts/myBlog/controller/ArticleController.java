package com.tts.myBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tts.myBlog.model.Article;
import com.tts.myBlog.repository.ArticleRepository;

@Controller
public class ArticleController {
  
  @Autowired
  private ArticleRepository articleRepository;

  @GetMapping(value = {"/", "/articles"})
  public String index(Article article, Model model) {
    Iterable<Article> articles = articleRepository.findAll();
    model.addAttribute("articles", articles);
    return "article/index";
  }
  
  @GetMapping(value = "/articles/new")
  public String getNewArticleForm(Model model) {
    model.addAttribute("article", new Article());
    return "article/new";
  }
  
  @PostMapping(value = "/articles/new")
  public String create(Article article, Model model) {
    articleRepository.save(article);
    model.addAttribute("article", article);
    return "article/show";
  }
  
}
