package com.freshman.webapi.util.fileutil;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class FileUtil {

    /**
     * 保存文件
     *
     * @param files    文件列表
     * @param filePath 保存路径，需要以/结束
     * @throws Exception
     */
    public static void saveUploadFiles(List<MultipartFile> files, String filePath) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            byte[] bytes = file.getBytes();
            FileOutputStream outputStream = new FileOutputStream(filePath + file.getOriginalFilename());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }
    }

    /**
     * 保存文件
     *
     * @param file     文件
     * @param filePath 保存路径，需要以/结束
     * @param fileName 文件名称
     * @throws Exception
     */
    public static void saveUploadFile(MultipartFile file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        if (file.isEmpty()) {
            return;
        }
        byte[] bytes = file.getBytes();
        FileOutputStream outputStream = new FileOutputStream(filePath + fileName);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
