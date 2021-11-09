package model;

import com.alibaba.fastjson.JSON;
import yanson.json.Json;

import java.io.File;

public class FileChunkPojo {

    private Long fileChunkId;
    private File file;

    public FileChunkPojo() {
    }

    public Long getFileChunkId() {
        return fileChunkId;
    }

    public void setFileChunkId(Long fileChunkId) {
        this.fileChunkId = fileChunkId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        FileChunkPojo fileChunkPojo = new FileChunkPojo();
        fileChunkPojo.setFile(new File("E:\\Github Repository\\Yanson\\yanson\\src\\test\\java\\model\\FileChunkPojo.java"));
        fileChunkPojo.setFileChunkId(1L);
        System.out.println(Json.toJsonString(fileChunkPojo));
        System.out.println(JSON.toJSON(fileChunkPojo));
        System.out.println(fileChunkPojo.getFile());
    }

}
