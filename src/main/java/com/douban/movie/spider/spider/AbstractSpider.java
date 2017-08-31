package com.douban.movie.spider.spider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douban.movie.spider.constant.AppConstant;
import com.douban.movie.spider.utils.HttpUtil;

public abstract class AbstractSpider implements Spider {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpider.class);
	private Map<String, String> header = new HashMap<>();

	public AbstractSpider() {
		// header.put("Content-Type", "application/json; charset=UTF-8");
	}

	public Document getJsonDocument(String url) {
		try {
			String content = HttpUtil.getUrlContent(url, header);
			return Jsoup.parse(content);
		} catch (Exception e) {
			LOGGER.error("获取document失败.", e);
		}
		return null;
	}

	public Document getDocument(String url) {
		try {
			URL rurl = new URL(url);
			return Jsoup.parse(rurl, AppConstant.REQUEST_URL_TIME_OUT);
		} catch (Exception e) {
			LOGGER.error("获取document失败.", e);
		}
		return null;
	}
}
