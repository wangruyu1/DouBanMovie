package com.douban.movie.spider.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.douban.movie.spider.entity.Music;
import com.douban.movie.spider.mapper.MusicMapper;
import com.douban.movie.spider.parser.MusicParser;
import com.douban.movie.spider.utils.JsoupUtil;

@Component
public class MusicSpider extends AbstractSpider implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(MusicSpider.class);
	private static final String ENTRY_URL = "https://music.douban.com/tag/";
	private static final String PROTOCOL_DOMAIN = "https://music.douban.com/";
	private static final int MAX_THREAD_NUM = 10;
	private static final int MAX_THREAD_SLEEP_TIME = 100;
	private static final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(MAX_THREAD_NUM);
	private static Document homeDoc = null;

	@Autowired
	private MusicParser musicParser;
	@Autowired
	private MusicMapper musicMapper;

	@Override
	public void spider() {
		LOGGER.info("开始爬取豆瓣音乐...");
		List<String> categories = getCategories();
		for (String c : categories) {
			while (threadPool.getActiveCount() == MAX_THREAD_NUM) {

			}
			List<String> tags = getTagsByCategory(c);
			for (String tag : tags) {
				threadPool.submit(new MusicTask(tag, c));
			}
		}
		while (threadPool.getActiveCount() > 0) {

		}
		threadPool.shutdown();
		LOGGER.info("爬取豆瓣音乐结束.");

	}

	private List<String> getTagsByCategory(String c) {
		List<String> tags = new ArrayList<>();
		Elements es = homeDoc.select("div[id=" + c + "] a");
		for (Element e : es) {
			tags.add(PROTOCOL_DOMAIN + e.attr("href"));
		}
		return tags;
	}

	public List<String> getCategories() {
		List<String> categories = new ArrayList<>();
		homeDoc = JsoupUtil.getDocumentByUrl(ENTRY_URL);
		Elements elements = homeDoc.select("div.mod");
		for (Element e : elements) {
			categories.add(e.attr("id"));
		}

		return categories;
	}

	public class MusicTask implements Runnable {
		private String tagUrl = null;
		private String category = null;

		public MusicTask(String tagUrl, String category) {
			this.tagUrl = tagUrl;
			this.category = category;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(MAX_THREAD_SLEEP_TIME));
			} catch (InterruptedException e) {
				LOGGER.error("线程" + Thread.currentThread().getName() + "挂起失败.");
			}
			LOGGER.info("开始爬取豆瓣音乐标签:" + tagUrl);
			tagUrl += "?type=" + MusicSortedType.COMMENT.value;
			Map<String, String> params = new HashMap<>();
			params.put("category", category);
			Document doc = JsoupUtil.getDocumentByUrl(tagUrl);
			int pageStart = 0;
			String pageTagUrl = tagUrl + "&start=";
			String tmpPageTagUrl = "";
			while (true) {
				tmpPageTagUrl = pageTagUrl + pageStart;
				pageStart += 20;
				List<Music> musics = musicParser.parseForList(pageTagUrl, params);
				// 暂时这么写
				if (musics == null || musics.size() == 0) {
					break;
				}
				for (Music m : musics) {
					Music dbMusic = musicMapper.queryByUrlMd5(m.getUrlMd5());
					if (dbMusic != null) {
						m.setId(dbMusic.getId());
						musicMapper.updateByPrimaryKey(m);
					} else {
						musicMapper.insert(m);
					}
				}

			}
		}

	}

	public enum MusicSortedType {
		SCORE("S"), COMMENT("O"), TIME("R"), GENERAL("T");
		private String value;

		private MusicSortedType(String value) {
			this.value = value;
		}

		public String valueOf() {
			return this.value;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		homeDoc = JsoupUtil.getDocumentByUrl(ENTRY_URL);
	}

}
