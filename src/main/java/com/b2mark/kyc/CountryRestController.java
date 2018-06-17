package com.b2mark.kyc;

import com.b2mark.kyc.entity.Country;
import com.b2mark.kyc.entity.CountryJpaRepository;
import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.exception.ContentNotFound;
import com.b2mark.kyc.image.storage.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/country")
@Api
@CrossOrigin(origins = {"http://avazcoin.com","http://staging1.b2mark.com"})
public class CountryRestController {

    private static final Logger log = LoggerFactory.getLogger(CountryRestController.class);
    private final CountryJpaRepository countryJpaRepository;

    @Autowired
    CountryRestController(CountryJpaRepository countryJpaRepository) {
        this.countryJpaRepository = countryJpaRepository;

    }

    /**
     * return list of country from database.
     *
     */
    @ApiOperation(value="list of country in the world")
    @GetMapping(produces = "application/json")
    List<Country> allcountry() {
        return this.countryJpaRepository.findAll();
    }


    /**
     * send country information of specific country
     * if country Id not found return 204 no content
     *
     * @param cid country Identification
     * @return json kyc information of user
     */
    @ApiOperation(value = "get country with cid (Country Identification)")
    @GetMapping(path = "/{cid}", produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
    Optional<Country> cidCountry(@PathVariable(value = "cid", required = true) String cid) {
        Optional<Country> country;
        if ((country = this.countryJpaRepository.findById(cid)).isPresent()) {
            log.info("####################country  find all:" + cid);
            return country;
        } else {
            throw new ContentNotFound();
        }
    }


}
