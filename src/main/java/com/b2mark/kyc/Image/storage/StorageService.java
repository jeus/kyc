package com.b2mark.kyc.image.storage;

import com.b2mark.kyc.enums.ImageType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file , ImageType imgType,String uid);

    Stream<Path> loadAll();

    Path load(String uid);

    Resource loadAsResource(ImageType imgType,String uid);

    void deleteAll();

}
