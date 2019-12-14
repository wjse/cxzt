package com.k66.cxzt;

import com.k66.cxzt.utils.IdGenUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(basePackages = "com.k66.cxzt.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public IdGenUtil idGenUtil(){
		return new IdGenUtil();
	}

}
