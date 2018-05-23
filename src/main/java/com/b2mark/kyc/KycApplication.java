package com.b2mark.kyc;

import com.b2mark.kyc.entity.Gender;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.entity.KycinfoRepository;
import com.b2mark.kyc.entity.LicenseType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KycApplication {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KycApplication.class, args);
	}


	@Bean
	public CommandLineRunner demo(KycinfoRepository kycinfoRepository)
	{
		return (args) -> {
		    log.info("Before start system this line start"+args.toString());
			kycinfoRepository.save(new Kycinfo(null,10,"Ali","Khandani","1234567",Gender.male,LicenseType.DL));
			kycinfoRepository.save(new Kycinfo(null,11,"Mehdi","Beygi","7654321",Gender.male,LicenseType.PS));
			kycinfoRepository.save(new Kycinfo(null,12,"Morteza","Asghari","9999999",Gender.male,LicenseType.NI));
			kycinfoRepository.save(new Kycinfo(null,13,"Bahman","Ajami","8888888",Gender.male,LicenseType.PS));
			kycinfoRepository.save(new Kycinfo(null,14,"Hosein","Khaste","7777777",Gender.male,LicenseType.DL));
			kycinfoRepository.save(new Kycinfo(null,15,"Ali","Khandani","66666666",Gender.male,LicenseType.DL));

            log.info("Create Six person in Database.");
            log.info("-------------------------------");


            kycinfoRepository.findAll().forEach((val) -> log.info(val.toString()));

            kycinfoRepository.findByUid(11).forEach((val)->log.info(val.toString()));

            log.info(kycinfoRepository.findById(1L).isPresent()?"LOADED USER":"DONT EXIST");
		};
	}
}
