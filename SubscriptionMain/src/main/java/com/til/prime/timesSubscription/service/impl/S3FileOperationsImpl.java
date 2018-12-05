package com.til.prime.timesSubscription.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.til.prime.timesSubscription.service.S3FileOperations;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class S3FileOperationsImpl implements S3FileOperations {

    private static final Logger LOGGER = Logger.getLogger(S3FileOperationsImpl.class);

    private static final String S3_URI_PREFIX = "s3:/";

    @Value("${mx.gaana.s3.accessKey}")
    private String accessKey;

    @Value("${mx.gaana.s3.secretKey}")
    private String secretKey;

    @Value("${mx.gaana.s3.region}")
    private String clientRegion;

    private AmazonS3Client s3Client;

    @Override
    public String uploadFile(File file, String bucket, String folder) {
        String filePath = folder + File.separator + file.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filePath, file)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        try {
            s3Client.putObject(putObjectRequest);
            // @formatter:off
            return Stream
                    .of(
                            S3_URI_PREFIX,
                            bucket,
                            filePath)
                    .collect(Collectors.joining(File.separator));
            // @formatter:on
        } catch (SdkClientException e) {
            LOGGER.error("exception", e);
        }
        // return s3Client.getResourceUrl(bucket, filePath);
        return null;
    }

    @Override
    public String uploadFromUrl(String urlString, String bucket, String folder, Long id, String identifier) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            LOGGER.error(urlString);
            LOGGER.error("exception", e);
            return null;
        }
        String filename = new File(url.getPath()).getName();
        String s3Path = null;
        try {
            File file = new File(id+"/"+identifier+"/"+filename);
            file.getParentFile().mkdirs();
            FileUtils.copyURLToFile(url, file);
            s3Path = uploadFile(file, bucket, folder);
            if (StringUtils.isEmpty(s3Path)) {
                LOGGER.info("Error for urlString: "+urlString);
            }
            file.delete();
            System.out.println("file: "+filename+", s3Path: "+s3Path);
        } catch (IOException e) {
            LOGGER.error("Exception", e);
        }
        return s3Path;
    }

    @Override
    @PostConstruct
    public void init() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        // @formatter:off
        s3Client = (AmazonS3Client)
                AmazonS3ClientBuilder
                        .standard()
                        .withRegion(clientRegion)
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .build();
        // @formatter:on
    }
}
