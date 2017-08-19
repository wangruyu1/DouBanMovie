package com.douban.movie.spider.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douban.movie.spider.entity.Movie;
import com.douban.movie.spider.utils.RegexUtil;
import com.google.gson.Gson;

@SuppressWarnings("all")
public class MovieParser implements Parser<Movie> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MovieParser.class);

	@Override
	public Movie parseForEntity(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Movie> parseForList(Document doc, String url) {
		List<Movie> movies = new ArrayList<>();
		Gson gson = new Gson();
		String content = doc.body().text();
		Map<String, List<Map<String, Object>>> movieMap = (Map<String, List<Map<String, Object>>>) gson
				.fromJson(content, Map.class);
		List<Map<String, Object>> movieList = movieMap.get("subjects");
		if (movieList == null || movieList.size() == 0) {
			return null;
		}
		String type = RegexUtil.matchFirstString(url, "type=(.*?)&");
		String tag = RegexUtil.matchFirstString(url, "tag=(.*?)&");
		if (tag == null || "".equals(tag) || tag.length() > 10) {
			System.out.println(url);
		}
		for (Map mp : movieList) {
			try {
				Movie m = new Movie();
				m.setName(mp.get("title").toString());
				m.setUrl(mp.get("url").toString());
				m.setPic(mp.get("cover").toString());
				m.setScore(Float.valueOf(mp.get("rate").toString()));
				m.setBkField1(type);
				m.setBkField2(tag);
				movies.add(m);
			} catch (NumberFormatException e) {
				LOGGER.error("电影数据转化失败.", e);
			}
		}
		return movies;
	}

}