package com.usfca.cs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig is the configuration class for Web.
 * 
 * @author zinal
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**").allowedOrigins("http://localhost:3000") // Your React app URL
				.allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
	}
}
