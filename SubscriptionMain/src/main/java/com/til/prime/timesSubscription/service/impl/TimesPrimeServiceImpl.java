package com.til.prime.timesSubscription.service.impl;

import com.google.gson.Gson;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.enums.PlatformEnum;
import com.til.prime.timesSubscription.service.ChecksumService;
import com.til.prime.timesSubscription.service.TimesPrimeService;
import com.til.prime.timesSubscription.util.HttpConnectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author amarnath.pathak
 * @date 04/09/18
 **/

@Service
public class TimesPrimeServiceImpl implements TimesPrimeService {
    private static final Logger LOG = Logger.getLogger(TimesPrimeServiceImpl.class);
    private static Gson gson = new Gson();

    @Autowired
    private HttpConnectionUtils httpConnectionUtils;
    @Resource(name = "config_properties")
    private Properties properties;
    @Autowired
    private ChecksumService checksumService;

    @Override
    public Double getTotalSaving(String mobile) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(properties.getProperty(GlobalConstants.PRIME_SECRET_KEY));
            sb.append(mobile);
            String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PRIME_ENCRYPTION_KEY), sb.toString());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("mobile", mobile);
            params.put("secretKey", properties.getProperty(GlobalConstants.PRIME_SECRET_KEY));
            params.put("checksum", checksum);
            String urlParam = httpConnectionUtils.prepareParams(params);
            TotalSavingsResponse response = httpConnectionUtils.requestForObject(null, properties.getProperty(GlobalConstants.PRIME_TOTAL_SAVING_URL_KEY) + urlParam, TotalSavingsResponse.class, GlobalConstants.GET);
            if (response != null && response.getSuccess()) {
                return response.getTotalSavings();
            }
        } catch (Exception e) {
            LOG.error("Exception in api call to prime for total saving of subscription with mobile: " + mobile, e);
        }
        return null;

    }
}
