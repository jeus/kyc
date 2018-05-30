/**
 * <h1>RestFul controll KYC</h1>
 * user can define kyc and operator can check validation od information<p>
 * <b>Note:</b> User can only define one KYC information<p>
 * every kyc information has 4 status. ['not_active','pending','accepted','rejected']
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */


package com.b2mark.kyc;

import com.b2mark.kyc.enums.Gender;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.entity.KycCrudRepository;
import com.b2mark.kyc.enums.LicenseType;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;

@SpringBootApplication
public class KycApplication {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KycApplication.class, args);
    }

    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {return new HiddenHttpMethodFilter();}

    @Bean
    ParameterMessageInterpolator parameterMessageInterpolator() {return new ParameterMessageInterpolator();}

    @RequestMapping("/kyc")

    @Bean
    public CommandLineRunner demo(KycCrudRepository kycCrudRepository) {
        return (args) -> {
            log.info("*********************************Before start system this line start lenght:" + args.length);
//            kycCrudRepository.save(new Kycinfo(null, 10, "Ali", "Khandani", "1234567", Gender.male, LicenseType.DL));
//            kycCrudRepository.save(new Kycinfo(null, 11, "Mehdi", "Beygi", "7654321", Gender.male, LicenseType.PS));
//            kycCrudRepository.save(new Kycinfo(null, 12, "Morteza", "Asghari", "9999999", Gender.male, LicenseType.NI));
//            kycCrudRepository.save(new Kycinfo(null, 13, "Bahman", "Ajami", "8888888", Gender.male, LicenseType.PS));
//            kycCrudRepository.save(new Kycinfo(null, 14, "Hosein", "Khaste", "7777777", Gender.male, LicenseType.DL));
//            kycCrudRepository.save(new Kycinfo(null, 15, "Ali", "Khandani", "66666666", Gender.male, LicenseType.DL));

            log.info("Create Six person in Database.ARGS:---->" + args.length);
            for (Object obj : args) {
                log.info("--------ARG-------" + obj.toString());
            }
            log.info("-------------------------------");

            kycCrudRepository.findAll().forEach((val) -> log.info(val.toString()));

            kycCrudRepository.findByUid(11).forEach((val) -> log.info(val.toString()));

            log.info(kycCrudRepository.findById(1L).isPresent() ? "LOADED USER" : "DONT EXIST");
        };
    }
}
