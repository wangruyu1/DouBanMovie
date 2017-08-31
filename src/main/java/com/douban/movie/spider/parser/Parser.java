package com.douban.movie.spider.parser;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.douban.movie.spider.entity.Music;

public interface Parser<T> {

	public T parseForEntity(Document doc);

	public List<T> parseForList(Document doc, String url);

	List<Music> parseForList(String pageTagUrl,Map<String, String> params);

}
