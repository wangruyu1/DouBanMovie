package com.douban.movie.spider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douban.movie.spider.constant.AppConstant;
import com.douban.movie.spider.entity.Movie;
import com.douban.movie.spider.mapper.MovieMapper;
import com.douban.movie.spider.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {
	@Autowired
	private MovieMapper movieMapper;

	@Override
	public List<Movie> queryMoviesByPage(int page, int pageNum) {
		if (page < 0) {
			page = 1;
		}
		if (pageNum < 1) {
			pageNum = AppConstant.PAGE_NUM;
		}
		return movieMapper.queryMoviesByPage((page - 1) * pageNum, pageNum);
	}

}
