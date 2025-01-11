package com.shoppingmall.cashshop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String itemImgLocation, String orignalFileName, byte[] fileData) throws Exception {

        UUID uuid = UUID.randomUUID(); //UUID를 이용하여 파일명을 새로 생성하여 구별
        String extension = orignalFileName.substring(orignalFileName.lastIndexOf("."));
        String savedFileName = uuid + extension;

        String fileUploadFullUrl = itemImgLocation +"/"+ savedFileName;

        FileOutputStream fos = new FileOutputStream((fileUploadFullUrl));
        fos.write(fileData);
        fos.close();

        return savedFileName;

    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        boolean isDeleted = false;
        if (deleteFile.exists()) {
            isDeleted = deleteFile.delete();  // 반환 값 확인
        }

        if (isDeleted) {
            log.info("File deleted successfully: " + filePath);
        } else {
            log.warning("File deletion failed or file does not exist: " + filePath);
        }
    }
}
