package com.example.bommung;

import com.example.bommung.fileupload.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BommungApplication {

	public static void main(String[] args) {
		SpringApplication.run(BommungApplication.class, args);
	}

}
