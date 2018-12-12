/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.image.storage;

import com.b2mark.kyc.enums.ImageType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init(String uriPath);

    //TODO: have to change generic have to ImageType change to property or parameter.
    void store(MultipartFile file,ImageType imgType,String uid,String uriPath);

    Stream<Path> loadAll(String uriPath);

    Path load(String fileName,String uriPath);

    Resource loadAsResource(ImageType imageType,String uid,String uriPath);

    void deleteAll(String uriPath);

}
