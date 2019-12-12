package com.example.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestBody {

    //表单格式
    public static final String FORM = "multipart/form-data";
    private String type;
    //参数  文件
    private Map<String, Object> params;
    private String boundary = createBoundary();
    private String startBoundary = "--" + boundary;
    private String endBoundary = startBoundary + "--";

    public RequestBody() {
        params = new HashMap<>();
    }

    private String createBoundary() {
        return "OkHttp" + UUID.randomUUID().toString();
    }

    public String getContentType() {
        //规范
        return type + "; boundary = " + boundary;
    }

    public long getContentLength() {
        //字节数
        long length = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                String posText = getText(key, (String) value);
                length += posText.getBytes().length;
            }
            if (value instanceof Binary) {
                Binary binary = (Binary) value;
                String posText = getText(key, binary);
                length += posText.getBytes().length;
                length+=binary.fileLength()+"\r\n".getBytes().length;
            }
        }

        if (params.size() != 0) {
            length += endBoundary.getBytes().length;
        }

        return length;
    }

    /**
     * 拼接
     * post表单提交拼接参数
     * startBoundary + "\r\n"
     * Content-Disposition;form-data;name="pagesize"
     * Context-Type:text/plain
     *
     * 1
     */
    private String getText(String key, String value) {
        return startBoundary + "\r\n" +
                "Content-Disposition: form-data; name= \"" + key + "\"\r\n" +
                "Context-Type:text/plain\r\n" +
                "\r\n" +
                value +
                "\r\n";
    }

    /**
     * 文件的内容流
     * startBoundary + "\r\n"
     * Content-Disposition;form-data;name="file";filename = "test.png"
     * Context-Type:(文件的type)
     */
    private String getText(String key, Binary value) {
        return startBoundary + "\r\n" +
                "Content-Disposition: form-data; name= \"" + key + "\" filename= \"" + value.fileName() + "\""+
                    "Context-Type: "+value.mimType()+"\r\n" +
                "\r\n";
    }

    public void onWriteBody(OutputStream outputStream) throws IOException {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                String posText = getText(key, (String) value);
                outputStream.write(posText.getBytes());
            }
            if (value instanceof Binary){
                Binary binary = (Binary) value;
                String posText = getText(key, binary);
                outputStream.write(posText.getBytes());
                binary.onWrite(outputStream);
                outputStream.write("\r\n".getBytes());
            }
        }
        if (params.size() != 0) {
            outputStream.write(endBoundary.getBytes());
        }
    }

    public RequestBody addParams(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RequestBody addParams(String key, Binary value) {
        params.put(key, value);
        return this;
    }

    public RequestBody type(String type) {
        this.type = type;
        return this;
    }

    public static Binary create(final File file){
        return new Binary() {
            @Override
            public long fileLength() {
                return file.length();
            }

            @Override
            public String mimType() {
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String minType = fileNameMap.getContentTypeFor(file.getName());
                if (minType==null || minType.isEmpty()){
                    return "application/octet-stream";
                }
                return minType;
            }

            @Override
            public void onWrite(OutputStream outputStream) throws IOException {
                InputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[2048];
                int len = 0;
                while ((len = inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }
                inputStream.close();
            }

            @Override
            public String fileName() {
                return file.getName();
            }
        };
    }
}
