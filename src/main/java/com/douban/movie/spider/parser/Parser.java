package com.douban.movie.spider.parser;

import java.util.List;

import org.jsoup.nodes.Document;

public interface Parser<T> {

	public T parseForEntity(Document doc);

	public List<T> parseForList(Document doc, String url);

}
