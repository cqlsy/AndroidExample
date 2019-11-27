package com.lsy.net.filedownupload;

public interface UpAndDownListener {

    void onProgress(long bytesWritten, long contentLength, boolean done);
}
