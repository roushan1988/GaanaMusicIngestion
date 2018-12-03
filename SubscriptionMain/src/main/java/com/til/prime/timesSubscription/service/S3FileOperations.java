package com.til.prime.timesSubscription.service;

import java.io.File;

public interface S3FileOperations {
    String uploadFile(File file, String bucket, String folder);
    String uploadFromUrl(String urlString, String bucket, String folder, Long id, String identifier);
    void init();
}
