/**
 * configuration persist file in system.
 * storage:
 *   endpoint:
 *     - uripath: /img
 *       dirpath: upload-dir
 *       filepermit: jpeg,jpg,png,gif
 *     - uripath: /doc
 *       dirpath: doc_upload
 *       filepermit: doc,docx
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.image.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("storage")
@Component
@Setter
@Getter
public class StorageProp {

    /**
     * Folder location for storing files
     */
    private List<Endpoints> endpoint = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        endpoint.forEach(args -> strBuilder.append(args.toString()));
        return strBuilder.toString();
    }


    @Setter
    @Getter
    public static class Endpoints {
        private String uriPath; //for example /image Directory Path
        private String dirPath; //where save files
        private String filePermits; //Comma seperated file format jpg,gif,...


        @Override
        public String toString() {
            return "endpoint{uriPath='" + uriPath + "',dirPath='" + dirPath + "',filepermit='" + filePermits + "'}";
        }

        public Path getPath() {
            Path path = FileSystems.getDefault().getPath(dirPath);
            return path;
        }

        public void setFilePermit(String filePermit) {
            this.filePermits = filePermit.replaceAll("\\s", "")
                    .replaceAll(",,", ",").toLowerCase();
        }

        public boolean isValidFormatFile(String format) {
            String[] fileperm = filePermits.split(",");
            for (String type : fileperm) {
                if (type.equalsIgnoreCase(format)) {
                    return true;
                }
            }
            return false;
        }

    }

}
