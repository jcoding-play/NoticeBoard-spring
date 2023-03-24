package spring.noticeboard.domain.article;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFilename;
    private String storeFilename;

    public UploadFile(String uploadFilename, String storeFilename) {
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
    }
}
