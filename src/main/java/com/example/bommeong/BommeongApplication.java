package com.example.bommeong;

import com.example.bommeong.fileupload.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BommeongApplication {

	public static void main(String[] args) {
		SpringApplication.run(BommeongApplication.class, args);
	}

}
