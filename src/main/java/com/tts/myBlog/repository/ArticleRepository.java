package com.tts.myBlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.myBlog.model.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

}
