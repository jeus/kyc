package com.b2mark.kyc;


import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.exception.KycNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kyc")
class KycRestController {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    private final KycJpaRepository kycJpaRepository;

    @Autowired
    KycRestController(KycJpaRepository kycJpaRepository) {
        this.kycJpaRepository = kycJpaRepository;
    }

    /**
     * send KYC information of specific user.
     * if UID not found return 204 no content.
     * @param uid user Identification
     * @return json kyc information of user
     */
    @GetMapping("/{uid}")
    Optional<Kycinfo> uidKyc(@PathVariable Integer uid) {
        Optional<Kycinfo> kycinfo;
        if ((kycinfo = this.kycJpaRepository.findByUid(uid)).isPresent()) {
            log.info("####################kyc indo find all:" + uid);
            return kycinfo;
        } else {
            throw new KycNotFound();
        }
    }

    /**
     * return all user kyc. this section enable for admin users
     *
     * @return
     */
    @GetMapping
    Collection<Kycinfo> AllKycStat() {
        //TODO: have to pagination for loading system.
        //TODO: have to check authentication for users. this system have to check only user with specific information
        log.info("####################kyc all users.");
        return this.kycJpaRepository.findAll();
        //TODO: find by uid (User Identificatiojn)
    }


    @GetMapping("/not_active")
    Collection<Kycinfo> not_active() {
        //TODO: pagination in this section have to enable
        //TODO: have to check authentication for users. this system have to check only user with specific information
        log.info("####################kyc all users.");
        return this.kycJpaRepository.findAll();
        //TODO: find by uid (User Identificatiojn)
    }


    @GetMapping("/pending")
    Collection<Kycinfo> pending() {
        //TODO: pagination in this section have to enable
        //TODO: have to check authentication for users. this system have to check only user with specific information
        log.info("####################kyc all users.");
        return this.kycJpaRepository.findAll();
        //TODO: find by uid (User Identificatiojn)
    }

    @GetMapping("/accepted")
    Collection<Kycinfo> accepted() {
        //TODO: pagination in this section have to enable
        //TODO: have to check authentication for users. this system have to check only user with specific information
        log.info("####################kyc all users.");
        return this.kycJpaRepository.findAll();
        //TODO: find by uid (User Identificatiojn)
    }


    @GetMapping("/rejected")
    Collection<Kycinfo> rejected() {
        //TODO: pagination in this section have to enable
        //TODO: have to check authentication for users. this system have to check only user with specific information
        log.info("####################kyc all users.");
        return this.kycJpaRepository.findAll();
        //TODO: find by uid (User Identificatiojn)
    }


    @PostMapping
    ResponseEntity<?> add(@PathVariable Integer uid, @RequestBody Kycinfo input) {


        this.validateUser(uid);
//        return this.accountRepository
//                .findByUsername(userId)
//                .map(account -> {
//                    Bookmark result = bookmarkRepository.save(new Bookmark(account,
//                            input.getUri(), input.getDescription()));
//
//                    URI location = ServletUriComponentsBuilder
//                            .fromCurrentRequest().path("/{id}")
//                            .buildAndExpand(result.getId()).toUri();
//
//                    return ResponseEntity.created(location).build();
//                })
//                .orElse(ResponseEntity.noContent().build());


        return this.kycJpaRepository.findByUid(uid).map(kyc -> {
            Kycinfo result = kycJpaRepository.save(new Kycinfo(null, input.getUid(), input.getFname(), input.getLname(),
                    input.getLicenseid(), input.getGender(), input.getLtype()));

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{uid}")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).build();
        }).orElse(ResponseEntity.noContent().build());
    }


    private void validateUser(Integer userId) {
        //TODO: this section have to check user validation. if have kyc or not.
//        this.accountRepository.findByUsername(userId).orElseThrow(
//                () -> new UserNotFoundException(userId));
    }


}