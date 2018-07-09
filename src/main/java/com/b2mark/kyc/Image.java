/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */
package com.b2mark.kyc;

import com.b2mark.kyc.enums.ImageType;
import com.b2mark.kyc.exception.BadRequest;
import com.b2mark.kyc.exception.ContentNotFound;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/img")
@EnableGlobalMethodSecurity(securedEnabled = true)
@Api()
@CrossOrigin(origins = {"http://avazcoin.com", "http://staging1.b2mark.com"})
public class Image {

    private static final Logger log = LoggerFactory.getLogger(KycApplication.class);

    private final StorageService storageService;

    @Autowired
    Image(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{uid}/{imgtype}")
    @ResponseBody
    public ResponseEntity<Resource> getKycImageByUidAndImgType(@PathVariable String uid, @PathVariable String imgtype) {

        ImageType imageType = null;
        if ((imageType = ImageType.fromString(imgtype)) != null) {
            Resource file = storageService.loadAsResource(imageType, uid,"/img");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } else {
            throw new BadRequest("imagetype [" + imageType + "] is not valid");
        }
    }


    //TODO: not work have to change for work
//    @GetMapping("/tumb/{uid}/{imgtype}")
//    @ResponseBody
    public ResponseEntity<BufferedImage> getKycThumbImageByUidandImgType(@PathVariable String uid, @PathVariable String imgtype) {
        BufferedImage bufferedImage = null;
        ImageType imageType = null;
        if ((imageType = ImageType.fromString(imgtype)) != null) {
            Resource file = storageService.loadAsResource(imageType, uid,"/img");
            try {
                File pathToFile = new File(file.getURI());
                BufferedImage image = ImageIO.read(pathToFile);
                bufferedImage =
                        createResizedCopy(image, 400, 400, false);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (bufferedImage != null) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"").body(bufferedImage);
            } else {
                throw new ContentNotFound("Not Found this Image");
            }
        } else {
            throw new BadRequest("imagetype [" + imageType + "] is not valid");
        }
    }

    private BufferedImage createResizedCopy(BufferedImage originalImage,
                                            int scaledWidth, int scaledHeight,
                                            boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

}
