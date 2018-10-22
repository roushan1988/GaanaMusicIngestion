package com.til.prime.timesSubscription.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TransactionStatusEnum {
    SUBSCRIPTION_TRANS_INITIATED,
    SUBSCRIPTION_TRANS_FAILED,
    SUBSCRIPTION_TRANS_SUCCESS;

    public static final Set<TransactionStatusEnum> COMPLETED_STATES = new HashSet<>(Arrays.asList(SUBSCRIPTION_TRANS_FAILED, SUBSCRIPTION_TRANS_SUCCESS));
}
