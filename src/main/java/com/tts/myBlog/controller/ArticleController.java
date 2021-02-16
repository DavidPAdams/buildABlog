package com.tts.myBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {

    @GetMapping(value = "/")
    public String index() {
      return "article/index";
    }
}
