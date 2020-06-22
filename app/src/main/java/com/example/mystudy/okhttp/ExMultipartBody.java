package com.example.mystudy.okhttp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.ForwardingSink;
import okio.Okio;
import okio.Source;
import okio.Timeout;

/**
 * 使用静态代理
 * 监听文件上传进度
 */
public class ExMultipartBody extends RequestBody {

    private RequestBody mRequestBody;
    private long curLength;
    private IProgressListener mListener;

    public ExMultipartBody(RequestBody requestBody) {
        mRequestBody = requestBody;
    }

    public ExMultipartBody(RequestBody requestBody, IProgressListener listener) {
        mRequestBody = requestBody;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        long contentLength = contentLength();

        //BufferedSink是一个服务器的输出流，也可以再通过一个代理BufferedSink
//        Ssink ssink = new Ssink(sink);
//        mRequestBody.writeTo(ssink);


        //或者使用OKio的类ForwardingSink,其实也可以看成是一个代理
        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                if (mListener!=null){
                    curLength += byteCount;
                    mListener.onProgress(curLength, contentLength);
                }
                super.write(source, byteCount);
            }
        };
        //转为我们要的BufferedSink
        BufferedSink bufferedSink = Okio.buffer(forwardingSink);
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private class Ssink implements BufferedSink {

        private BufferedSink bufferedSink;

        public Ssink(BufferedSink sink) {
            bufferedSink = sink;
        }

        @Override
        public Buffer buffer() {
            return null;
        }

        @Override
        public BufferedSink write(ByteString byteString) throws IOException {
            return bufferedSink.write(byteString);
        }

        @Override
        public BufferedSink write(byte[] source) throws IOException {
            return null;
        }

        @Override
        public BufferedSink write(byte[] source, int offset, int byteCount) throws IOException {
            return null;
        }

        @Override
        public long writeAll(Source source) throws IOException {
            return 0;
        }

        @Override
        public BufferedSink write(Source source, long byteCount) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeUtf8(String string) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeUtf8(String string, int beginIndex, int endIndex) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeUtf8CodePoint(int codePoint) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeString(String string, Charset charset) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeString(String string, int beginIndex, int endIndex, Charset charset) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeByte(int b) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeShort(int s) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeShortLe(int s) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeInt(int i) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeIntLe(int i) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeLong(long v) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeLongLe(long v) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeDecimalLong(long v) throws IOException {
            return null;
        }

        @Override
        public BufferedSink writeHexadecimalUnsignedLong(long v) throws IOException {
            return null;
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {

        }

        @Override
        public void flush() throws IOException {

        }

        @Override
        public Timeout timeout() {
            return null;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public BufferedSink emit() throws IOException {
            return null;
        }

        @Override
        public BufferedSink emitCompleteSegments() throws IOException {
            return null;
        }

        @Override
        public OutputStream outputStream() {
            return null;
        }

        @Override
        public int write(ByteBuffer src) throws IOException {
            return 0;
        }

        @Override
        public boolean isOpen() {
            return false;
        }
    }
}
