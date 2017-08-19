package com.douban.movie.spider.service;

import java.util.List;

import com.douban.movie.spider.entity.Movie;

public interface MovieService {
	List<Movie> queryMoviesByPage(int page, int pageNum);
}
