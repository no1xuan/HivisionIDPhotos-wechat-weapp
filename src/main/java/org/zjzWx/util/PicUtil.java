package org.zjzWx.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

//图片工具类
public class PicUtil {

    //folderName          文件夹目录
    //directory           图片路径
    //originalFilename    原始图片名
    //file                前端传过来的图片
    public static String filesCopy(String folderName,String directory,String originalFilename,MultipartFile file) throws IOException {
        String filename = null;

        File uploadFolder = new File(directory, folderName);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // 生成新的文件名
        filename = generateUniqueFilename(originalFilename, file);
        Path filePath = uploadFolder.toPath().resolve(filename);
        // 保存文件
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }



    public static String generateUniqueFilename(String originalFilename, MultipartFile file) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());
        String contentHash = getFileContentHash(file); // 获取文件内容哈希值
        return timestamp  + contentHash + getExtension(originalFilename);
    }

    private static String getFileContentHash(MultipartFile file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(file.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex);
        }
        return "";
    }

    public static class MultipartInputStreamFileResource extends ByteArrayResource {
        private final String filename;

        public MultipartInputStreamFileResource(MultipartFile multipartFile) throws Exception {
            super(multipartFile.getBytes());
            this.filename = multipartFile.getOriginalFilename();
        }

        @Override
        public String getFilename() {
            return this.filename;
        }
    }


    public static MultipartFile base64ToMultipartFile(String base64) {
        // 提取Base64内容
        String[] baseStrs = base64.split(",");

        // 获取 MIME 类型
        String mimeType = baseStrs[0].split(":")[1].split(";")[0];
        String extension = mimeType.split("/")[1]; // 提取文件扩展名

        // 解码Base64数据
        byte[] data = Base64.getDecoder().decode(baseStrs[1]);

        // 创建MultipartFile对象
        return new MockMultipartFile(
                "file", // 表单字段名
                "filename." + extension, // 原始文件名，带扩展名
                mimeType, // 文件类型
                data // 文件数据
        );
    }






}
