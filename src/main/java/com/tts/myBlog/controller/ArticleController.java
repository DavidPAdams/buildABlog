package com.tts.myBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tts.myBlog.model.Article;

@Controller
public class ArticleController {

    @GetMapping(value = "/")
    public String index(Article article) {
      return "article/index";
    }
}
