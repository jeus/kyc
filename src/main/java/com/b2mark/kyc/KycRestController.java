package com.b2mark.kyc;


import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.enums.Status;
import com.b2mark.kyc.exception.ContentNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.Collection;
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
     *
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
            throw new ContentNotFound();
        }
    }

    /**
     * return all user kyc. this section enable for admin users
     * in administrator panel operator controll users kyc in system.
     *
     * @return
     */
    @GetMapping(params = {"page", "size", "dir"})
    Collection<Kycinfo> AllKycStat(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                   @RequestParam(value = "status", defaultValue = "all") String st) {
        //TODO: have to check authentication for Operator. this system have to check only user with specific information
        Sort.Direction direction = Sort.Direction.ASC;
        if (dir.toLowerCase().equals("asc")) {
            direction = Sort.Direction.ASC;
        } else if (dir.toLowerCase().equals("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, new Sort(direction, new String[]{"lastupdate"}));

        if (st.equalsIgnoreCase("all")) {
            return this.kycJpaRepository.findAll(pageable).getContent();
        } else {
            Status status = Status.fromString(st);
            if (status != null)
                return this.kycJpaRepository.findByStatus(status);
            else
                throw new ContentNotFound("Kyc Status Undefined");
        }

    }

    @PostMapping
    ResponseEntity<?> add(@RequestBody Kycinfo input) {

        log.info("##############TA INJA OMAD##################");
        //TODO: have to check validation user that insert is same to specific user (UID)
        //TODO: Test(operator shouldnt create kyc)
        Kycinfo kycInfo = kycJpaRepository.save(input);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uid}")
                .buildAndExpand(kycInfo.getId()).toUri();
        return ResponseEntity.created(location).build();

    }


    private void validateUser(Integer userId) {
        //TODO: this section have to check user validation. if have kyc or not.
//        this.accountRepository.findByUsername(userId).orElseThrow(
//                () -> new UserNotFoundException(userId));
    }


}