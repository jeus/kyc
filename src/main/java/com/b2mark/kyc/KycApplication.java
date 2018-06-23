/**
 * <h1>RestFul controll KYC</h1>
 * user can define kyc and operator can check validation od information<p>
 * <b>Note:</b> User can only define one KYC information<p>
 * every kyc information has 4 status. ['pending','accepted','rejected']
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */


package com.b2mark.kyc;

import com.b2mark.kyc.entity.KycCrudRepository;
import com.b2mark.kyc.image.storage.StorageProperties;
import com.b2mark.kyc.image.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;


@EnableEurekaClient
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class KycApplication {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KycApplication.class, args);
    }

    @RequestMapping("/kyc")

    @Bean
    public CommandLineRunner demo(KycCrudRepository kycCrudRepository,StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();//TODO: have to delete this line.
            storageService.init();
        };
    }
}
