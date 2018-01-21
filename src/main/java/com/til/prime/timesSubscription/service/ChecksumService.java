package com.til.prime.timesSubscription.service;

public interface ChecksumService {
    String calculateChecksumHmacSHA256(String secretKey, String allParamValue) throws Exception;
    String calculateChecksumMD5(String allParamValue) throws Exception;
}
