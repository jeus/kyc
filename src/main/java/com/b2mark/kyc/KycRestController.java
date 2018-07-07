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
import com.b2mark.kyc.entity.KycStatus;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.enums.ImageType;
import com.b2mark.kyc.enums.Status;
import com.b2mark.kyc.exception.BadRequest;
import com.b2mark.kyc.exception.ContentNotFound;
import com.b2mark.kyc.exception.Unauthorized;
import com.b2mark.kyc.image.storage.StorageService;
import io.swagger.annotations.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/kyc")
//@EnableGlobalMethodSecurity(securedEnabled = true)
@Api()
@CrossOrigin(origins = {"http://avazcoin.com", "http://staging1.b2mark.com"})
class KycRestController {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    private final KycJpaRepository kycJpaRepository;
    private final StorageService storageService;
    private final CountryJpaRepository countryJpaRepository;
//    private final Authentication authentication;


    @Autowired
    KycRestController(KycJpaRepository kycJpaRepository, StorageService storageService,
                      CountryJpaRepository countryJpaRepository) {
        this.kycJpaRepository = kycJpaRepository;
        this.storageService = storageService;
        this.countryJpaRepository = countryJpaRepository;
//        this.authentication = authentication;
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
    Optional<Kycinfo> getKycByUid(@PathVariable(value = "uid", required = true) String uid) {
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

    @ApiOperation(value = "return kyc paginatio if not found 204 content not found")
    @GetMapping(produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
    Collection<Kycinfo> getAllKyces(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                    @RequestParam(value = "dir", defaultValue = "asc", required = false) String dir,
                                    @RequestParam(value = "status", defaultValue = "all", required = false) String st, Authentication authentication) {
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


    @ApiModelProperty(value = "uid", required = false)
    @PostMapping
    ResponseEntity<Kycinfo> addKyc(@RequestBody Kycinfo input, Authentication authentication) {
        log.info("MTD:add DESC:add new kycinfo for users");
        if (kycJpaRepository.existsByUid(authentication.getName()))//Check uid registered.
        {
            throw new BadRequest("Kyc for this user registerd before that");
        }
        if (KycApplication.mapCountries.get(input.getCountry()) == null)//Check country id valid
        {
            throw new BadRequest("Country ID is not Valid " + input.getCountry());
        }
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
    ResponseEntity<Kycinfo> updateKyc(@RequestBody Kycinfo kycInput, Authentication authentication) {
        log.info("MTD:update DSC:update exist kycinfo");
        if (authentication.getName().equals(kycInput.getUid())) {
            Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
            if (optKycInfo.isPresent()) {
                Kycinfo kycInfo = optKycInfo.get();
                if (kycInfo.getStatus().equals(Status.checking)) {
                    throw new Unauthorized("You cant update information. Our Operators are checking your data. Please Wait");
                }
                if (KycApplication.mapCountries.get(kycInput.getCountry()) == null) {
                    throw new BadRequest("Country ID is not Valid " + kycInput.getCountry());
                }
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
        } else {
            throw new Unauthorized("UID is not currect");
        }
    }

    @GetMapping("/status")
    ResponseEntity<KycStatus> getStatus(Authentication authentication) {
        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
        Map<String, Object> status = new HashMap<>();
        if (optKycInfo.isPresent()) {
            Kycinfo kycinfo = optKycInfo.get();
            KycStatus kycStatus = new KycStatus(kycinfo.getUid(), kycinfo.getStatus());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("status/{uid}")
                    .buildAndExpand(kycStatus.getUid()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<>(kycStatus, headers, HttpStatus.OK);
        } else {
            throw new ContentNotFound("This user is invalid");
        }
    }

    @ApiOperation(value = "list of status (know your customer) by uid (user identification)")
    @GetMapping(path = "/status/{uid}", produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
    ResponseEntity<KycStatus> getKycStatusByUid(@PathVariable(value = "uid", required = false) String uid, Authentication authentication) {
        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
        if (optKycInfo.isPresent()) {
            Kycinfo kycinfo = optKycInfo.get();
            KycStatus kycStatus = new KycStatus(kycinfo.getUid(), kycinfo.getStatus());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("kyc/{uid}")
                    .buildAndExpand(kycStatus.getUid()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<>(kycStatus, headers, HttpStatus.OK);
        } else {
            throw new ContentNotFound("This user is invalid");
        }
    }

    @ApiOperation("return status kyc paginatio if not found 204 content not found")
    @GetMapping(path = "/status", produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
   Collection<KycStatus> getAllKycStatuses(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                    @RequestParam(value = "dir", defaultValue = "asc", required = false) String dir,
                                    @RequestParam(value = "status", defaultValue = "all", required = false) String st, Authentication authentication) {
        authentication.getAuthorities().forEach(System.out::println);
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
        Collection<KycStatus> statusList = new ArrayList<>();

        if (authorized) {
            List<Kycinfo> kycinfos = this.kycJpaRepository.findAll(pageable).getContent();
            if (kycinfos.size() != 0 && kycinfos != null) {
                kycinfos.forEach(args -> {
                    KycStatus kycStatus = new KycStatus(args.getUid(), args.getStatus());
                    statusList.add(kycStatus);
                });
                return statusList;
            } else {
                throw new ContentNotFound("Data Not Found");
            }
        } else {
            Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
            if (optKycInfo.isPresent()) {
                Kycinfo kycinfo = optKycInfo.get();
                KycStatus kycStatus = new KycStatus(kycinfo.getUid(), kycinfo.getStatus());
                statusList.add(kycStatus);
                return statusList;
            } else {
                throw new ContentNotFound("This user is invalid");
            }
        }
    }

    @GetMapping(path = "/status/test", produces = "application/json")
    public String checkStatus() {
        return "OK DARE DOROST kar mikone";
    }


    @Secured("webapp-admin,ROLE_KYCHECKER")
    @PutMapping("/{uid}/{status}")
    ResponseEntity<KycStatus> editStatus(@PathVariable String uid, @PathVariable String status, Authentication authentication) {
        Optional<Kycinfo> kycinfoOptional;

        boolean authorized =
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("webapp-admin")) ||
                        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_KYCHECKER"));
        if (!authorized) {
            throw new Unauthorized("You don't have permission to change status");
        }
        Status status1 = Status.fromString(status);
        if (status1 == null) {
            throw new BadRequest("status is not defined [pending,checking,rejected,accepted]");
        }
        //TODO: change role from keycloak rest service.
        if ((kycinfoOptional = this.kycJpaRepository.findByUid(uid)).isPresent()) {
            log.info("####################kyc indo find all:" + uid);
            Kycinfo kycinfo = kycinfoOptional.get();
            kycinfo.setStatus(status1);
            kycJpaRepository.save(kycinfo);

            KycStatus kycStatus = new KycStatus(kycinfo.getUid(), kycinfo.getStatus());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("kyc/{uid}")
                    .buildAndExpand(kycStatus.getUid()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<>(kycStatus, headers, HttpStatus.OK);
        } else {
            throw new ContentNotFound("This user is invalid");
        }
    }


    @GetMapping("/img/{imgtype}")
    @ResponseBody
    public ResponseEntity<Resource> getKycImage(@PathVariable String imgtype, Authentication authentication) {

        ImageType imageType = null;
        if ((imageType = ImageType.fromString(imgtype)) != null) {
            Resource file = storageService.loadAsResource(imageType, authentication.getName(), "/img");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } else {
            throw new BadRequest("imagetype [" + imageType + "] is not valid");
        }
    }

    @PostMapping("/img")
    public String addKycImage(@RequestParam("file") MultipartFile file,
                              @RequestParam("imgtype") String imgtypeStr,
                              RedirectAttributes redirectAttributes, Authentication authentication) {
        ImageType imageType = null;
        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
        if (optKycInfo.isPresent()) {
            Kycinfo kycInfo = optKycInfo.get();
            if (kycInfo.getStatus().equals(Status.checking)) {
                throw new Unauthorized("You cant update information. Our Operators are checking your data. Please Wait");
            }
            if (kycInfo.getStatus().equals(Status.accepted)) {
                throw new Unauthorized("Your Information is Accepted, You can't change this information.");
            }
        }
        if ((imageType = ImageType.fromString(imgtypeStr)) != null) {
            storageService.store(file, imageType, authentication.getName(), "/img");
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