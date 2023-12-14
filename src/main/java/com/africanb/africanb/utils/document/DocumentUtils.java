package com.africanb.africanb.utils.document;

import com.africanb.africanb.helper.searchFunctions.Utilities;
import lombok.extern.java.Log;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Log
public class DocumentUtils {


    public static boolean createFileOnDiskHard(byte[] content, String fileLocation) throws IOException {
        if(Utilities.isBlank(fileLocation) ){
            log.info("repertoire inexistant="+fileLocation);
           return false;
        }
        if(content.length==0){
            log.info("Contenu zero");
            return false;
        }
        File newFile = new File(fileLocation);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(newFile);
            fos.write(content);
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        } catch (SecurityException se){
            se.printStackTrace();
            return false;
        }finally {
            fos.close();
        }
        return true;
    }


    public static boolean checkIfDirectoryExists(String path){
        if(path==null) return false;
        File directory = null;
        try{
            directory = new File(path);
            if(!directory.exists()) return false;
        }catch (SecurityException se){
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean checkIfDocumentExistsOnDirectory(String filePath){
        if(filePath==null) return false;
        File pdfFile = null;
        try{
            pdfFile = new File(filePath);
            if(!pdfFile.exists()) return false;
        }catch (SecurityException se){
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean createDirectoryOnHardDisk(String pathDirectory){
        File directory = null;
        try{
            directory = new File(pathDirectory);
            directory.mkdirs();
        }catch (SecurityException se){
            se.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean compareFileSizeToLimitSize(MultipartFile file, Double limitSize) {
        long fileSize = file.getSize(); //Taille en octets
        double fileSizeInMb = fileSize / (1024 * 1024.0); // Taille en mégaoctets
        log.info("_78 compareFileSizeToLimitSize :: fileSizeInMb =="+fileSizeInMb);
        if(fileSizeInMb > limitSize){
            log.info("_80 compareFileSizeToLimitSize :: fileSizeInMb =="+fileSizeInMb);
            return false;
        }
        return true;
    }

}
