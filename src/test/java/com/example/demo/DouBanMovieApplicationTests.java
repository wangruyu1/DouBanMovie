package com.example.demo;

import java.net.URI;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.douban.movie.spider.DouBanMovieApplication;
import com.douban.movie.spider.entity.Movie;
import com.douban.movie.spider.httpclient.HttpUtil;
import com.douban.movie.spider.mapper.MovieMapper;
import com.douban.movie.spider.utils.MD5Util;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DouBanMovieApplication.class)
public class DouBanMovieApplicationTests {
	@Autowired
	private MovieMapper movieMapper;

	@Test
	public void contextLoads() {
		Movie movie = new Movie();
		movie.setName("test");
		movie.setUrl("http://www.douban.com");
		movie.setUrlMd5(MD5Util.encode(movie.getUrl()));
		movieMapper.insert(movie);
	}
	@Test
	public void test1(){
	}

}
