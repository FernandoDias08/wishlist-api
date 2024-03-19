package com.fernandodias.wishlist.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WishlistApplication {

	public static void main(String[] args) {
	      SpringApplication application = new SpringApplication(WishlistApplication.class);
//	      application.setBannerMode(Banner.Mode.OFF);
	      application.run(args);
		}

}
