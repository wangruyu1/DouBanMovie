package com.douban.movie.spider.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.douban.movie.spider.entity.Music;
import com.douban.movie.spider.utils.DateUtil;
import com.douban.movie.spider.utils.JsoupUtil;
import com.douban.movie.spider.utils.MD5Util;

@Service
public class MusicParser implements Parser<Music> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MusicParser.class);

	@Override
	public Music parseForEntity(Document doc) {
		return null;
	}

	@Override
	public List<Music> parseForList(Document doc, String url) {
		return null;
	}

	@Override
	public List<Music> parseForList(String pageTagUrl, Map<String, String> params) {
		Document doc = JsoupUtil.getDocumentByUrl(pageTagUrl);
		Elements elements = doc.select("div[id=subject_list] table tr");
		List<Music> musics = new ArrayList<>();
		String tag = doc.select("div[id=content] h1").text().split(":")[1].trim();
		for (Element e : elements) {
			try {
				Music m = new Music();
				m.setUrl(e.select("a.nbg").attr("href"));
				m.setUrlMd5(MD5Util.encode(m.getUrl()));
				m.setImg(e.select("img").attr("src"));
				m.setName(e.select("td[valign=top] a").text());
				String[] desc = e.select("td[valign=top] p").get(0).text().split("/");
				m.setSinger(desc[0]);
				m.setPublishTime(desc[1]);
				m.setScore(Float.valueOf(e.select("div.star.clearfix span.rating_nums").text().trim()));
				m.setComment(Integer.valueOf(e.select("div.star.clearfix span.pl").text().replaceAll("[^0-9]", "")));
				m.setCategory(params.get("category"));
				m.setTag(tag);
				musics.add(m);
			} catch (Exception e1) {
				LOGGER.error("解析音乐实体失败", e1);
			}
		}
		return musics;
	}

}
