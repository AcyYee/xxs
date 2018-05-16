package com.cwy.xxs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author acy19
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.cwy.xxs.dao.mybatis")
public class XxsApplication {

	public static void main(String[] args) {
		SpringApplication.run(XxsApplication.class, args);
	}

}
