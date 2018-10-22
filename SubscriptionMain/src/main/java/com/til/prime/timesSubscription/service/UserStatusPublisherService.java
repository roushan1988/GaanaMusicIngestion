package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.PublishedUserStatusDTO;

public interface UserStatusPublisherService {
    boolean publishUserStatus(PublishedUserStatusDTO msg);
}
