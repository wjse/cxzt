package com.k66.cxzt.web;

import com.k66.cxzt.web.handler.AuthHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

	@Value("${web.origins}")
	private String origins;

	@Bean
	public AuthHandler authHandler(){
		return new AuthHandler();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {

			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(authHandler())
				        .addPathPatterns("/**")
				        .excludePathPatterns("/favicon.ico","/*.html" , "/login" , "/error" , "/static/**" , "/css/**" , "/fonts/**" ,
												             "/icons**" , "/img/**" , "/js/**" , "/vendor/**");
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/static/**")
								.addResourceLocations("classpath*:/static/");
			}

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
								.allowedOrigins(origins)
								.allowCredentials(true)
								.allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
								.allowedHeaders("openId")
								.maxAge(3600);
			}
		};
	}
}
