package com.douban.movie.spider;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.douban.movie.spider.constant.AppConstant;
import com.douban.movie.spider.entity.Movie;
import com.douban.movie.spider.mapper.MovieMapper;
import com.douban.movie.spider.service.MovieService;

@Controller
public class MovieController {

	@Autowired
	private MovieService movieService;
	@Autowired
	private MovieMapper movieMapper;

	@RequestMapping(value = "/movies")
	public String queryMovieByPage(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", defaultValue = "" + AppConstant.PAGE_NUM) Integer pageNum) {
		System.out.println(movieMapper);
		List<Movie> movies = movieService.queryMoviesByPage(page, pageNum);
		request.setAttribute("movies", movies);
		return "movies";
	}

}
