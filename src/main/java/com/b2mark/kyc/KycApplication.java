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

import com.b2mark.kyc.entity.tables.Country;
import com.b2mark.kyc.entity.tables.CountryJpaRepository;
import com.b2mark.kyc.image.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EnableEurekaClient
@SpringBootApplication
//@EnableConfigurationProperties(StorageProp.class)
public class KycApplication {


    public static Map<String,String> mapCountries = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KycApplication.class, args);
    }

    @Bean
    public CommandLineRunner initial(CountryJpaRepository countryJpaRepository,StorageService storageService) {
        return (args) -> {
            storageService.init("/img");
            List<Country> countries = new ArrayList<>();
            //Caching countries in memory.
            countries  = countryJpaRepository.findAll();
            countries.forEach(country -> {mapCountries.put(country.getId(),country.getName());});
        };
    }
}


