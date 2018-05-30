/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.b2mark.kyc.Image;

import com.b2mark.kyc.enums.ImageType;
import com.b2mark.kyc.exception.ContentNotFound;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Greg Turnquist
 */
@Configuration
@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;
//    private final MeterRegistry meterRegistry;

    public ImageService(ResourceLoader resourceLoader) {

        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> findOneImage(String filename) {
        return Mono.fromSupplier(() ->
                resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename))
                .log("findOneImage");
    }

    /**
     * @param files
     * @param imgTypeStr has 3 type
     *                <ul>
     *                <li>Passport Cover:[cover]</li>
     *                <li>Passport Personal Page[passport]</li>
     *                <li>Selfie With Photo ID And Note[passid]</li>
     *                </ul>
     * @return
     */
    public Mono<Void> createImage(Flux<FilePart> files, String imgTypeStr) {
        return files.log("createImage-files")
                .flatMap(file -> {
                    ImageType imgType;
                    if((imgType = ImageType.fromString(imgTypeStr)) == null)
                    {
                        throw new ContentNotFound("This Image Type Not Found"+imgTypeStr);
                    }
                    //TODO Test: Insert imgType by undefied image type.
                    Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT + imgType.getDirectory(), file.filename()).toFile())
                            .log("createImage-picktarget")
                            .map(destFile -> {
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-newfile")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");

                    return Mono.when(copyFile).log("createImage-when");
                })
                .log("createImage-flatMap")
                .then()
                .log("createImage-done");
    }

    public Mono<Void> deleteImage(String filename, ImageType imgType) {
        Mono<Object> deleteFile = Mono.fromRunnable(() -> {
            try {
                //TODO: delete by Uid (user identification).
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT + imgType.getDirectory(), filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).log("deleteImage-file");

        return Mono.when(deleteFile).log("deleteImage-when")
                .then().log("deleteImage-done");
    }

    /**
     * Pre-load some fake image
     *
     * @return Spring Boot {@link CommandLineRunner} automatically run after app context is loaded.
     */
    @Bean
    CommandLineRunner setUp() throws IOException {
        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT + ImageType.cover.getDirectory()));
            Files.createDirectory(Paths.get(UPLOAD_ROOT + ImageType.passid.getDirectory()));
            Files.createDirectory(Paths.get(UPLOAD_ROOT + ImageType.passport.getDirectory()));
        };
    }
}
