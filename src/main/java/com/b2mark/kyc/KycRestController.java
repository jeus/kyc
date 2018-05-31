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


import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.entity.Kycinfo;
import com.b2mark.kyc.enums.ImageType;
import com.b2mark.kyc.enums.Status;
import com.b2mark.kyc.exception.BadRequest;
import com.b2mark.kyc.exception.ContentNotFound;
import com.b2mark.kyc.Image.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/kyc")
class KycRestController {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    private final KycJpaRepository kycJpaRepository;
    private final StorageService storageService;




    @Autowired
    KycRestController(KycJpaRepository kycJpaRepository, StorageService storageService) {
        this.kycJpaRepository = kycJpaRepository;
        this.storageService = storageService;
    }

    /**
     * send KYC information of specific user
     * if UID not found return 204 no content
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
     * in administrator panel operator controll users kyc in systems
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
        log.info("MTD:add DESC:add new kycinfo for users");
        //TODO: have to check validation user that insert is same to specific user (UID)
        //TODO: Test(operator shouldnt create kyc)
        Kycinfo kycInfo = kycJpaRepository.save(input);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{uid}")
                .buildAndExpand(kycInfo.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


    @PutMapping
    ResponseEntity<?> update(@RequestBody Kycinfo input) {
        log.info("MTD:update DSC:update exist kycinfo");
        //TODO: have to check validation user that update is same to specific user(UID)
        //TODO: Test(operator shouldnt update kyc)
        //TODO: when update change lastUpdate to now();
        if (kycJpaRepository.existsByUid(15)) {
            input.setId(88L);
            kycJpaRepository.save(input);
            return ResponseEntity.noContent().build();
        } else {
            throw new ContentNotFound("This user id undefined");
        }
    }


    @GetMapping("/{uid}/reject")
    String reject(@PathVariable Integer uid) {
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
    String pending(@PathVariable Integer uid) {
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
    String accept(@PathVariable Integer uid) {
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


    @GetMapping("/files/{imgtype}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String imgtype) {

        ImageType imageType = null;
        if((imageType = ImageType.fromString(imgtype)) != null ) {
            Resource file = storageService.loadAsResource(imageType);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
        else
        {
            throw  new BadRequest("imagetype [" +imageType+"] is not valid" );
        }
    }

    @PostMapping("/img")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestHeader("imgtype") String imgtypeStr,
                                   RedirectAttributes redirectAttributes) {
        ImageType imageType = null;
        if((imageType = ImageType.fromString(imgtypeStr)) != null) {
            storageService.store(file,imageType);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
            return "";
        }else
        {
            throw new BadRequest("header key:imagetype is not found or invalid imagetype:[cover,passport,passid]");
        }
    }

    private void validateUser(Integer userId) {
        //TODO: this section have to check user validation. if have kyc or not.
//        this.accountRepository.findByUsername(userId).orElseThrow(
//                () -> new UserNotFoundException(userId));
    }


}