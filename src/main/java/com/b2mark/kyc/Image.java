package com.b2mark.kyc;

import com.b2mark.kyc.entity.CountryJpaRepository;
import com.b2mark.kyc.entity.KycJpaRepository;
import com.b2mark.kyc.enums.ImageType;
import com.b2mark.kyc.exception.BadRequest;
import com.b2mark.kyc.image.storage.StorageService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/img")
@EnableGlobalMethodSecurity(securedEnabled = true)
@Api()
@CrossOrigin(origins = {"http://avazcoin.com", "http://staging1.b2mark.com"})
public class Image {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    private final StorageService storageService;

    @Autowired
    Image( StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{uid}/{imgtype}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String uid,@PathVariable String imgtype) {

        ImageType imageType = null;
        if ((imageType = ImageType.fromString(imgtype)) != null) {
            Resource file = storageService.loadAsResource(imageType,uid);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } else {
            throw new BadRequest("imagetype [" + imageType + "] is not valid");
        }
    }
}
