package com.douban.movie.spider.spider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.douban.movie.spider.constant.AppConstant;
import com.douban.movie.spider.entity.Movie;
import com.douban.movie.spider.mapper.MovieMapper;
import com.douban.movie.spider.parser.MovieParser;
import com.douban.movie.spider.utils.MD5Util;
import com.google.gson.Gson;

@Component
public class MovieSpider extends AbstractMovieSpider {
	private static final Logger LOGGER = LoggerFactory.getLogger(MovieSpider.class);
	private static String[] types = new String[] { "movie", "tv" };
	private static final String TAG_URL = "https://movie.douban.com/j/search_tags";
	private static final String SEARCH_URL = "https://movie.douban.com/j/search_subjects";
	private static final String URL = "https://movie.douban.com/j/search_subjects?tag=&sort=recommend&page_limit=20&page_start=0";
	private static final String SORTED_TYPE = "recommend";
	private static final int DEFAULT_PAGE_LIMIT = 20;
	private static final int DEFAULT_PAGE_START = 0;
	private static final Set<String> tagSet = new HashSet<>();
	private static final int MAX_THREAD_NM = 10;
	private MovieParser mp = new MovieParser();

	private ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREAD_NM);
	private CountDownLatch countDownLatch = null;

	@Autowired
	private MovieMapper movieMapper;

	// 初始化分类链接
	public void init() {
		try {
			String[] tags = null;
			Document doc = null;
			for (int i = 0; i < types.length; i++) {
				String tagUrl = this.addType(TAG_URL, types[i]);
				String searchUrl = this.addType(SEARCH_URL, types[i]);
				doc = this.getJsonDocument(tagUrl);
				String tagContent = doc.body().html();
				Gson gson = new Gson();
				Map map = gson.fromJson(tagContent, Map.class);
				tags = gson.fromJson(map.get("tags").toString(), String[].class);
				for (String tag : tags) {
					String partUrl = this.addSortParam(searchUrl, SORTED_TYPE);
					partUrl = this.addTag(partUrl, tag);
					// partUrl = this.addPageLimit(partUrl, DEFAULT_PAGE_LIMIT,
					// DEFAULT_PAGE_START);
					tagSet.add(partUrl);
				}
			}
			countDownLatch = new CountDownLatch(tags.length);
		} catch (Exception e) {
			LOGGER.error("初始化链接失败.", e);
		}
	}

	public void spider() {
		this.init();
		// 开始爬取
		for (String url : tagSet) {
			threadPool.submit(new MovieTagSpiderTask(url));
		}
		try {
			// 注意main线程的情况
			countDownLatch.await();
			threadPool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String addSortParam(String url, String sort) {
		return url + "&sort=" + sort;
	}

	private String addPageLimit(String url, int pageLimit, int pageStart) {
		return url + "&page_limit=" + pageLimit + "&page_start=" + pageStart;
	}

	private String addTag(String url, String tag) throws UnsupportedEncodingException {
		return url + "&tag=" + tag;// URLEncoder.encode(tag,
									// AppConstant.URL_ENCODE_CHARSET);
	}

	private String addType(String url, String type) {
		return url + "?type=" + type;
	}

	public class MovieTagSpiderTask implements Runnable {
		private String url;

		public MovieTagSpiderTask(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			try {
				LOGGER.info("开始解析url" + this.url);
				int pageLimit = DEFAULT_PAGE_LIMIT;
				int pageStart = DEFAULT_PAGE_START;
				Document doc = null;
				while (true) {
					String pageUrl = addPageLimit(this.url, pageLimit, pageStart);
					doc = getJsonDocument(pageUrl);
					pageStart += pageLimit;
					List<Movie> movies = mp.parseForList(doc, pageUrl);
					if (movies == null || movies.size() == 0) {
						return;
					}
					movies.forEach(movie -> {
						try {
							movie.setUrlMd5(MD5Util.encode(movie.getUrl()));
							if ("F345A3DA086ACDEC496366E738613DE5".equals(movie.getUrlMd5())) {
								System.out.println(movie.getUrlMd5());
								System.out.println(movie.toString());
								System.out.println(pageUrl);
							}
							Movie dbMovieovie = movieMapper.queryByUrlMd5(movie.getUrlMd5());
							if (dbMovieovie == null) {
								movieMapper.insertExceptId(movie);
							} else {
								movie.setId(dbMovieovie.getId());
								movieMapper.updateByPrimaryKey(movie);
							}
						} catch (Exception e) {
							LOGGER.error("解析url:" + movie.getUrl() + "失败.", e);
						}
					});

				}
			} catch (Exception e2) {
				LOGGER.error("解析出错.", e2);
			} finally {
				countDownLatch.countDown();
			}
		}

	}

}
