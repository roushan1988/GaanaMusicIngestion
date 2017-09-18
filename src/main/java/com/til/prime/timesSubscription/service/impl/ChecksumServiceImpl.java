package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.service.ChecksumService;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class ChecksumServiceImpl implements ChecksumService {
    @Override
    public  String calculateChecksumHmacSHA256(String secretKey, String allParamValue) throws Exception {
        System.out.println("this is secret key :" + secretKey);
        System.out.println("all param is :" + allParamValue);
        byte[] dataToEncryptByte = allParamValue.getBytes();
        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] checksumByte = mac.doFinal(dataToEncryptByte);
        String checksum = toHex(checksumByte);
        return checksum;
    }

    private String toHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder(bytes.length * 2);
        byte[] arg5 = bytes;
        int arg4 = bytes.length;

        for (int arg3 = 0; arg3 < arg4; ++arg3) {
            Byte b = Byte.valueOf(arg5[arg3]);
            String str = Integer.toHexString(b.byteValue());
            int len = str.length();
            if (len == 8) {
                buffer.append(str.substring(6));
            } else if (str.length() == 2) {
                buffer.append(str);
            } else {
                buffer.append("0" + str);
            }
        }

        return buffer.toString();
    }
}