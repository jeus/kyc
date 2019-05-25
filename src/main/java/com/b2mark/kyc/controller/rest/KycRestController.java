package com.b2mark.kyc.controller.rest;


import com.b2mark.common.exceptions.ExceptionsDictionary;
import com.b2mark.common.exceptions.PublicException;
import com.b2mark.common.temp.RandomNameGenerator;
import com.b2mark.kyc.KycApplication;
import com.b2mark.kyc.entity.MerchantKyc;
import com.b2mark.kyc.entity.tables.CountryJpaRepository;
import com.b2mark.kyc.entity.tables.KycJpaRepository;
import com.b2mark.kyc.entity.KycStatus;
import com.b2mark.kyc.entity.tables.Kycinfo;
import com.b2mark.kyc.enums.ImageType;
import com.b2mark.kyc.enums.LicenseType;
import com.b2mark.kyc.enums.Status;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping("/kyc")
@Api()
@CrossOrigin(origins = {"https://becopay.com"})
class KycRestController {

    private static final Logger LOG = LoggerFactory.getLogger(KycApplication.class);

    private final KycJpaRepository kycJpaRepository;
    private final StorageService storageService;
    private final CountryJpaRepository countryJpaRepository;

    private final RandomNameGenerator randomNameGenerator;


    @Autowired
    KycRestController(KycJpaRepository kycJpaRepository, StorageService storageService,
                      CountryJpaRepository countryJpaRepository, RandomNameGenerator randomNameGenerator) {
        this.kycJpaRepository = kycJpaRepository;
        this.storageService = storageService;
        this.countryJpaRepository = countryJpaRepository;
        this.randomNameGenerator = randomNameGenerator;
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
    Optional<Kycinfo> getKycByUid(@PathVariable(value = "uid", required = true) String uid, @ApiIgnore Authentication authentication) {
        Optional<Kycinfo> kycinfo;
        boolean authorized =
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("webapp-admin")) ||
                        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_KYCHECKER"));

        if (!authorized && !authentication.getName().equals(uid)) {
            throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, "You don't have permission");
        }
        if ((kycinfo = this.kycJpaRepository.findByUid(uid)).isPresent()) {
            LOG.info("action:GetKyc,uid:{}", authentication.getName());
            return kycinfo;
        } else {
            throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "this uid is not valid!");
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
                                    @RequestParam(value = "status", defaultValue = "all", required = false) String st, @ApiIgnore Authentication authentication) {

        boolean authorized = authentication.getAuthorities().contains(new SimpleGrantedAuthority("webapp-admin")) ||
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
                    Collection<Kycinfo> collection = new ArrayList<Kycinfo>();
                    collection.add(kycinfo.get());
                    LOG.info("action:GetAllKyc,uid:{}", authentication.getName());
                    return collection;
                } else {
                    throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "KYC is not exist");
                }
            }
        } else {
            Status status = Status.fromString(st);
            if (status != null) {
                LOG.info("action:GetAllKyc,uid:{}", authentication.getName());
                return this.kycJpaRepository.findByStatus(status);
            } else
                throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "Kyc Status Undefined");
        }
    }


    @ApiOperation(value = "return kyc paginatio if not found 204 content not found")
    @GetMapping(path = "/merchant", produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
    Collection<MerchantKyc> getAllMerchantKyces(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                @RequestParam(value = "dir", defaultValue = "asc", required = false) String dir,
                                                @RequestParam(value = "status", defaultValue = "all", required = false) String status, @ApiIgnore Authentication authentication) {
        Collection<Kycinfo> kycinfos = getAllKyces(page, size, dir, status, authentication);
        Collection<MerchantKyc> merchantKycs = kycinfos.stream().map(s -> new MerchantKyc(s)).collect(Collectors.toList());
        LOG.info("action:GetAllKyc,uid:{},page:{},size:{},dir:{},status:{}", authentication.getName(), authentication.getName(), page, size, dir, status);
        return merchantKycs;

    }


    @ApiModelProperty(value = "uid", required = false)
    @PostMapping
    ResponseEntity<Kycinfo> addKyc(@RequestBody Kycinfo input, @ApiIgnore Authentication authentication) {
        if (kycJpaRepository.existsByUid(authentication.getName())) {//Check uid registered.
            throw new PublicException(ExceptionsDictionary.FREQUENTLYREQUEST, "Kyc for this user registerd before that");
        }
        if (KycApplication.mapCountries.get(input.getCountry()) == null) {//Check country id valid
            throw new PublicException(ExceptionsDictionary.PARAMETERISNOTVALID, "Country ID is not Valid " + input.getCountry());
        }
        input.setUid(authentication.getName());
        Kycinfo kycInfo = kycJpaRepository.save(input);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uid}")
                .buildAndExpand(kycInfo.getUid()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        LOG.info("action:AddKyc,UID:{},getFName:{},getLName:{}", authentication.getName(), input.getFname(), input.getLname());
        return new ResponseEntity<>(kycInfo, headers, HttpStatus.CREATED);
    }

    @ApiModelProperty(value = "mobile", required = false)
    @PostMapping("/merchant")
    ResponseEntity<MerchantKyc> addMerchantKyc(@RequestBody MerchantKyc input, @ApiIgnore Authentication authentication) {

        Kycinfo kycinfo = input.getKycinfo();
        kycinfo.setCountry("IR");
        kycinfo.setLtype(LicenseType.NI);

        ResponseEntity<Kycinfo> kycinfo1 = addKyc(kycinfo, authentication);
        MerchantKyc merchantKyc = new MerchantKyc(kycinfo1.getBody());


        Kycinfo kycinfo2 = kycJpaRepository.save(merchantKyc.getKycinfo());
        MerchantKyc merchantKyc1 = new MerchantKyc(kycinfo2);


        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uid}")
                .buildAndExpand(merchantKyc1.getKycinfo().getUid()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        LOG.info("action:AddMerchant,UID:{},getFName:{},getLName:{}", authentication.getName(), input.getFname(), input.getLname());
        return new ResponseEntity<>(merchantKyc1, headers, HttpStatus.CREATED);
    }


    @PutMapping
    ResponseEntity<Kycinfo> updateKyc(@RequestBody Kycinfo input, Authentication authentication) {

        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
        if (optKycInfo.isPresent()) {
            Kycinfo kycInfo = optKycInfo.get();
            if (kycInfo.getStatus().equals(Status.pending)) {
                throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, "before that you send information our operators will check your information");
            }
            if (kycInfo.getStatus().equals(Status.checking)) {
                throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, "You cant update information. Our Operators are checking your data. Please Wait");
            }
            if (kycInfo.getStatus().equals(Status.accepted)) {
                throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, "You cant update information. You are accepted");
            }
            if (KycApplication.mapCountries.get(kycInfo.getCountry()) == null) {
                throw new PublicException(ExceptionsDictionary.NUMBERISNOTVALID, "Country ID is not Valid " + kycInfo.getCountry());
            }
            kycInfo.setCountry(kycInfo.getCountry());
            kycInfo.setFname(kycInfo.getFname());
            kycInfo.setLname(kycInfo.getLname());
            kycInfo.setGender(kycInfo.getGender());
            kycInfo.setLicenseid(kycInfo.getLicenseid());
            kycInfo.setLtype(kycInfo.getLtype());
            kycInfo.setLastupdate(new Date());
            kycJpaRepository.save(kycInfo);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{uid}")
                    .buildAndExpand(kycInfo.getUid()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            LOG.info("action:AddKyc,uid:{},getfname:{},getlname:{}", authentication.getName(), input.getFname(), input.getLname());
            return new ResponseEntity<>(kycInfo, headers, HttpStatus.CREATED);
        } else {
            return addKyc(input, authentication);
        }
    }

    @GetMapping("/status")
    ResponseEntity<KycStatus> getStatus(@ApiIgnore Authentication authentication) {
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
            LOG.info("action:getStatus,uid:{}", authentication.getName());
            return new ResponseEntity<>(kycStatus, headers, HttpStatus.OK);
        } else {
            throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "This user is invalid");
        }
    }


    @ApiOperation(value = "list of status (know your customer) by uid (user identification)")
    @GetMapping(path = "/status/{uid}", produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "service and address is ok but content not found")
            }
    )
    ResponseEntity<KycStatus> getKycStatusByUid(@PathVariable(value = "uid", required = false) String uid, @ApiIgnore Authentication authentication) {
        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
        if (optKycInfo.isPresent()) {
            Kycinfo kycinfo = optKycInfo.get();
            KycStatus kycStatus = new KycStatus(kycinfo.getUid(), kycinfo.getStatus());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("kyc/{uid}")
                    .buildAndExpand(kycStatus.getUid()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            LOG.info("action:getKycStatusByUid,uid:{},requestuid:{}", authentication.getName(), uid);
            return new ResponseEntity<>(kycStatus, headers, HttpStatus.OK);
        } else {
            throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "This user is invalid");
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
                                            @RequestParam(value = "status", defaultValue = "all", required = false) String status, @ApiIgnore Authentication authentication) {
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
                LOG.info("action:getAllKycStatuses,uid:{},page:{},size:{},dir:{},status:{}", authentication.getName(), page, size, dir, status);
                return statusList;
            } else {
                throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "Data Not Found");
            }
        } else {
            Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
            if (optKycInfo.isPresent()) {
                Kycinfo kycinfo = optKycInfo.get();
                KycStatus kycStatus = new KycStatus(kycinfo.getUid(), kycinfo.getStatus());
                statusList.add(kycStatus);
                LOG.info("action:getAllKycStatuses,uid:{},page:{},size:{},dir:{},status:{}", authentication.getName(), page, size, dir, status);
                return statusList;
            } else {
                throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "This user is invalid");
            }
        }
    }

    @PutMapping("/{uid}/{status}")
    ResponseEntity<KycStatus> updateKycStatus(@PathVariable String uid, @PathVariable String status, @ApiIgnore Authentication authentication) {
        Optional<Kycinfo> kycinfoOptional;
        boolean authorized =
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("webapp-admin")) ||
                        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_KYCHECKER"));
        if (!authorized) {
            throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, "You don't have permission to change status");
        }
        Status status1 = Status.fromString(status);
        if (status1 == null) {
            throw new PublicException(ExceptionsDictionary.PARAMETERNOTFOUND, "status is not defined [pending,checking,rejected,accepted]");
        }
        //TODO: change role from keycloak rest service.
        if ((kycinfoOptional = this.kycJpaRepository.findByUid(uid)).isPresent()) {
            Kycinfo kycinfo = kycinfoOptional.get();
            String fromStatus = kycinfo.getStatus().name();
            kycinfo.setStatus(status1);
            kycJpaRepository.save(kycinfo);

            KycStatus kycStatus = new KycStatus(kycinfo.getUid(), kycinfo.getStatus());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("kyc/{uid}")
                    .buildAndExpand(kycStatus.getUid()).toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            LOG.info("action:UpdateKycStatus,UID:{},from-status:{},to-status:{}", authentication.getName(), fromStatus, status);
            return new ResponseEntity<>(kycStatus, headers, HttpStatus.OK);
        } else {
            throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "This user is invalid");
        }
    }


    @GetMapping("/img/{imgtype}")
    @ResponseBody
    public ResponseEntity<Resource> getKycImage(@PathVariable String imgtype, Authentication authentication) {

        ImageType imageType = null;
        if ((imageType = ImageType.fromString(imgtype)) != null) {
            Resource file = storageService.loadAsResource(imageType, authentication.getName(), "/img");
            LOG.info("action:getKycImage,uid:{},imgtype:{}", authentication.getName(), imgtype);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } else {
            throw new PublicException(ExceptionsDictionary.PARAMETERISNOTVALID, "imagetype [" + imageType + "] is not valid");
        }
    }

    @PostMapping("/img")
    public String addKycImage(@RequestParam("file") MultipartFile file,
                              @RequestParam("imgtype") String imgtypeStr, Authentication authentication) {
        ImageType imageType = null;
        Optional<Kycinfo> optKycInfo = kycJpaRepository.findByUid(authentication.getName());
        if (optKycInfo.isPresent()) {
            Kycinfo kycInfo = optKycInfo.get();
            if (kycInfo.getStatus().equals(Status.checking)) {
                throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, "You cant update information. Our Operators are checking your data. Please Wait");
            }
            if (kycInfo.getStatus().equals(Status.accepted)) {
                throw new PublicException(ExceptionsDictionary.UNAUTHORIZED, "Your Information is Accepted, You can't change this information.");
            }
        }
        if ((imageType = ImageType.fromString(imgtypeStr)) != null) {
            storageService.store(file, imageType, authentication.getName(), "/img");
            LOG.info("action:AddKycImage,UID:{},fileName:{},fileSize:{},fileContentType:{},imgtype:{}", authentication.getName(), file.getName(), file.getSize(), file.getContentType(), imgtypeStr);
            return "OK";
        } else {
            throw new PublicException(ExceptionsDictionary.PARAMETERISNOTVALID, "header key:imagetype is not found or invalid imagetype:[cover,passport,passid]");
        }
    }

}