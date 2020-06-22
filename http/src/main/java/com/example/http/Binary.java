package com.example.http;

import java.io.IOException;
import java.io.OutputStream;

public interface Binary {

    long fileLength();

    String mimType();

    void onWrite(OutputStream outputStream)throws IOException;

    String fileName();
}
