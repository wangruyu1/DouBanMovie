package com.douban.movie.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@ServletComponentScan
public class DouBanMovieApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DouBanMovieApplication.class);
		 app.setWebEnvironment(false);
		app.run(args);
	}

}
