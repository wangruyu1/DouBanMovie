package com.douban.movie.spider.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupUtil {
	public static Document getDocumentByUrl(String url) {
		return Jsoup.parse(HttpUtil.getUrlContent(url));
	}

}
