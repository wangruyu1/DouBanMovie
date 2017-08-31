package com.douban.movie.spider.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.douban.movie.spider.spider.MovieSpider;
import com.douban.movie.spider.spider.MusicSpider;
import com.douban.movie.spider.spider.Spider;

@Component
public class MainSpider implements ApplicationRunner {
	private List<Spider> spiders = new ArrayList<>();

	@Autowired
	private MovieSpider movieSpider;
	@Autowired
	private MusicSpider musicSpider;

	public void init() {
		spiders.add(musicSpider);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		init();
		spiders.forEach(spider -> {
			spider.spider();
		});
	}
}
