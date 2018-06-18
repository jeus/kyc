/**
 * <h1>RestFul controll KYC</h1>
 * user can define kyc and operator can check validation od information<p>
 * <b>Note:</b> User can only define one KYC information<p>
 * every kyc information has 4 status. ['pending','checking','accepted','rejected']
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc;


import com.b2mark.kyc.entity.CountryJpaRepository;
import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.enums.ImageType;
import com.b2mark.kyc.enums.Status;
import com.b2mark.kyc.exception.BadRequest;
import com.b2mark.kyc.exception.ContentNotFound;
import com.b2mark.kyc.image.storage.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/kyc")
@EnableGlobalMethodSecurity(securedEnabled = true)
@Api()
@CrossOrigin(origins = {"http://avazcoin.com", "http://staging1.b2mark.com"})
class KycRestController {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    private final KycJpaRepository kycJpaRepository;
    private final StorageService storageService;
    private final CountryJpaRepository countryJpaRepository;


    @Autowired
    KycRestController(KycJpaRepository kycJpaRepository, StorageService storageService, CountryJpaRepository countryJpaRepository) {
        this.kycJpaRepository = kycJpaRepository;
        this.storageService = storageService;
        this.countryJpaRepository = countryJpaRepository;
    }

    /**
     * send KYC information of specific user
     * if UID not found return 204 no content
     *
     * @param uid user Identification
     * @return json kyc information of user
     */
    @ApiOperation(value = "list of kyc (know your customer) by uid (user identification)")
    @GetMapping(path = "/{uid}", produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
    Optional<Kycinfo> uidKyc(@PathVariable(value = "uid", required = true) String uid) {
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
     * in administrator panel operator controll users kyc in systems
     *
     * @return
     */
    @ApiOperation("return kyc paginatio if not found 204 content not found")
    @GetMapping(params = {"page", "size", "dir"}, produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
    Collection<Kycinfo> AllKycStat(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                   @RequestParam(value = "dir", defaultValue = "asc", required = false) String dir,
                                   @RequestParam(value = "status", defaultValue = "all", required = false) String st, Principal principal, Authentication authentication) {
        //TODO: have to check authentication for Operator. this system have to check only user with specific information


        boolean authorized =
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("webapp-admin")) ||
                        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_KYCHECKER"));


        Sort.Direction direction = Sort.Direction.ASC;
        if (dir.toLowerCase().equals("asc")) {
            direction = Sort.Direction.ASC;
        } else if (dir.toLowerCase().equals("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, new Sort(direction, new String[]{"lastupdate"}));

        if (st.equalsIgnoreCase("all")) {
            if (authorized) {
                return this.kycJpaRepository.findAll(pageable).getContent();
            } else {
                String uid = authentication.getName();
                Optional<Kycinfo> kycinfo;
                if ((kycinfo = this.kycJpaRepository.findByUid(uid)).isPresent()) {
                    log.info("####################kyc indo find all:" + uid);
                    Collection<Kycinfo> collection = new ArrayList<Kycinfo>();
                    collection.add(kycinfo.get());
                    return collection;
                } else {
                    throw new ContentNotFound();
                }
            }
        } else {
            Status status = Status.fromString(st);
            if (status != null)
                return this.kycJpaRepository.findByStatus(status);
            else
                throw new ContentNotFound("Kyc Status Undefined");
        }
    }

    @PostMapping
    ResponseEntity<Kycinfo> add(@RequestBody Kycinfo input, Authentication authentication) {
        log.info("MTD:add DESC:add new kycinfo for users");
        //TODO: have to check validation user that insert is same to specific user (UID)
        //TODO: Test(operator shouldnt create kyc)
        input.setUid(authentication.getName());
        Kycinfo kycInfo = kycJpaRepository.save(input);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uid}")
                .buildAndExpand(kycInfo.getUid()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(kycInfo, headers, HttpStatus.CREATED);
    }


    @PutMapping
    ResponseEntity<Kycinfo> update(@RequestBody Kycinfo kycInput, Authentication authentication) {
        log.info("MTD:update DSC:update exist kycinfo");
        //TODO: have to check validation user that update is same to specific user(UID)
        //TODO: CHECK Status Value not set in this system.
        //TODO: Test(operator shouldnt update kyc)
        //TODO: when update change lastUpdate to now();

        if (authentication.getName().equals(kycInput.getUid())) {
        }
        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());//TODO: Check This UID and OTHER UID
        if (optKycInfo.isPresent()) {
            Kycinfo kycInfo = optKycInfo.get();
            kycInfo.setCountry(kycInput.getCountry());
            kycInfo.setFname(kycInput.getFname());
            kycInfo.setLname(kycInput.getLname());
            kycInfo.setGender(kycInput.getGender());
            kycInfo.setLicenseid(kycInput.getLicenseid());
            kycInfo.setLtype(kycInput.getLtype());
            kycInfo.setLastupdate(new Date());

            kycJpaRepository.save(kycInfo);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{uid}")
                    .buildAndExpand(kycInfo.getUid()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<>(kycInfo, headers, HttpStatus.CREATED);
        } else {
            throw new ContentNotFound("This user id undefined");
        }
    }

    @GetMapping("/status")
    Map<String, Object> status(Authentication authentication) {
        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
        Map<String, Object> status = new HashMap<>();

        if (optKycInfo.isPresent()) {
            Kycinfo kycinfo = optKycInfo.get();
            status.put("uid", kycinfo.getUid());
            status.put("status", kycinfo.getStatus());
            return status;
        } else {
            throw new ContentNotFound("This user is invalid");
        }
    }

    @GetMapping("/{uid}/reject")
    String reject(@PathVariable String uid) {
        Optional<Kycinfo> kycinfoOptional;
        //TODO: check role and permission
        if ((kycinfoOptional = this.kycJpaRepository.findByUid(uid)).isPresent()) {
            log.info("####################kyc indo find all:" + uid);
            Kycinfo kycinfo = kycinfoOptional.get();
            kycinfo.setStatus(Status.rejected);
            kycJpaRepository.save(kycinfo);
            return "";
        } else {
            throw new ContentNotFound("This user is invalid");
        }
    }


    @GetMapping("/{uid}/pending")
    String pending(@PathVariable String uid) {
        //TODO: check role and permission
        Optional<Kycinfo> kycinfoOptional;
        if ((kycinfoOptional = this.kycJpaRepository.findByUid(uid)).isPresent()) {
            log.info("####################kyc indo find all:" + uid);
            Kycinfo kycinfo = kycinfoOptional.get();
            kycinfo.setStatus(Status.accepted);
            kycJpaRepository.save(kycinfo);
            return "";
        } else {
            throw new ContentNotFound("this user is invalid");
        }
    }

    @GetMapping("/{uid}/accept")
    String accept(@PathVariable String uid) {
        //TODO: check role and permission
        Optional<Kycinfo> kycinfoOptional;
        if ((kycinfoOptional = this.kycJpaRepository.findByUid(uid)).isPresent()) {
            log.info("####################kyc indo find all:" + uid);
            Kycinfo kycinfo = kycinfoOptional.get();
            kycinfo.setStatus(Status.accepted);
            kycJpaRepository.save(kycinfo);
            return "";
        } else {
            throw new ContentNotFound("this user is invalid");
        }
    }


    @GetMapping("/img/{imgtype}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String imgtype) {

        ImageType imageType = null;
        if ((imageType = ImageType.fromString(imgtype)) != null) {
            Resource file = storageService.loadAsResource(imageType);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } else {
            throw new BadRequest("imagetype [" + imageType + "] is not valid");
        }
    }

    @PostMapping("/img")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("imgtype") String imgtypeStr,
                                   RedirectAttributes redirectAttributes) {
        ImageType imageType = null;
        if ((imageType = ImageType.fromString(imgtypeStr)) != null) {
            storageService.store(file, imageType);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
            return "OK";
        } else {
            throw new BadRequest("header key:imagetype is not found or invalid imagetype:[cover,passport,passid]");
        }
    }


    private void validateUser(Integer userId) {
        //TODO: this section have to check user validation. if have kyc or not.
//        this.accountRepository.findByUsername(userId).orElseThrow(
//                () -> new UserNotFoundException(userId));
    }
}