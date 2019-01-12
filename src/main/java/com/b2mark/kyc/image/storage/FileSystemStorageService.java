/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.image.storage;

import com.b2mark.common.exceptions.ExceptionsDictionary;
import com.b2mark.common.exceptions.PublicException;
import com.b2mark.kyc.KycApplication;
import com.b2mark.kyc.enums.ImageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final StorageProp storageProp;
    private final String type = ".jpg";

    private static final Logger LOG = LoggerFactory.getLogger(KycApplication.class);


    @Autowired
    public FileSystemStorageService(StorageProp storageProp) {
       this.storageProp = storageProp;
    }

    @Override
    public void store(MultipartFile file,ImageType imgType,String uid,String uriPath) {
        StorageProp.Endpoints endpoint = findEndPoint(uriPath);
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new PublicException(ExceptionsDictionary.PARAMETERISNOTVALID,"Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new PublicException(ExceptionsDictionary.PARAMETERISNOTVALID, "Cannot store file with relative path outside current directory " + filename);
            }
            if(!isValidContentType(endpoint,file.getContentType()))
            {
                throw new PublicException(ExceptionsDictionary.PARAMETERISNOTVALID,"this content type is not valid valid content type is ["+endpoint.getFilePermits()+"]");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, imgType.getPath(endpoint.getPath()).
                                resolve(uid+type),//TODO: have to change master file type.
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            LOG.error("action:store,message:{}",e.getMessage());
            throw new PublicException(ExceptionsDictionary.UNDEFINEDERROR,"Failed to store file " + filename);
        }
    }

    @Override
    public Stream<Path> loadAll(String uriPath) {
        StorageProp.Endpoints endpoint = findEndPoint(uriPath);
        if(endpoint == null)
        {
            LOG.error("action:store,message:{}","This upload URI 'directorectory path' is not valid");
            throw new PublicException(ExceptionsDictionary.UNDEFINEDERROR,"This Error is undefined");
        }
        try {
            return Files.walk(endpoint.getPath(), 1)
                .filter(path -> !path.equals(endpoint.getPath()))
                .map(endpoint.getPath()::relativize);
        }
        catch (IOException e) {
            LOG.error("action:store,message:{}",e.getMessage());
            throw new PublicException(ExceptionsDictionary.UNDEFINEDERROR,"Failed to read stored files");
        }

    }

    @Override
    public Path load(String fileName,String uriPath) {
        StorageProp.Endpoints endpoint = findEndPoint(uriPath);
        return endpoint.getPath().resolve(fileName+type);
    }//TODO:change type .jpg

    @Override
    public Resource loadAsResource(ImageType imageType,String uid,String uriPath) {
        StorageProp.Endpoints endpoint = findEndPoint(uriPath);

        try {
            Path file = imageType.getPath(endpoint.getPath()).resolve(uid+type);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new PublicException(ExceptionsDictionary.CONTENTNOTFOUND, "Could not read the file");
            }
        }
        catch (MalformedURLException e) {
            LOG.error("action:store,message:{}",e.getMessage());
            throw new PublicException(ExceptionsDictionary.UNDEFINEDERROR,"Could not read file ");
        }
    }

    @Override
    public void deleteAll(String uriPath) {
        StorageProp.Endpoints endpoint = findEndPoint(uriPath);
        FileSystemUtils.deleteRecursively(endpoint.getPath().toFile());
    }

    @Override
    public void init(String uriPath) {
        StorageProp.Endpoints endpoint = null;
        if((endpoint = findEndPoint(uriPath))== null)
        {
            return;
        }
        try {
            Files.createDirectories(endpoint.getPath());
            Files.createDirectories(ImageType.cover.getPath(endpoint.getPath()));
            Files.createDirectories(ImageType.passport.getPath(endpoint.getPath()));
            Files.createDirectories(ImageType.passid.getPath(endpoint.getPath()));
        }
        catch (IOException e) {
            LOG.error("action:store,message:{}",e.getMessage());
            throw new PublicException(ExceptionsDictionary.UNDEFINEDERROR,"Could not initialize storage");
        }
    }

    private Boolean isValidContentType(StorageProp.Endpoints endpoints ,String contentType) {
        return endpoints.isValidFormatFile(contentType.substring(contentType.indexOf('/')+1));
    }


    private StorageProp.Endpoints findEndPoint(String uriPath)
    {
        return storageProp.getEndpoint().stream()
                .filter(args -> uriPath.equalsIgnoreCase(args.getUriPath()))
                .findAny().orElse(null);
    }



}
