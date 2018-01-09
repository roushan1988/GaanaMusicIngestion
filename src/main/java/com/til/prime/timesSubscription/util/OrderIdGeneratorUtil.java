package com.til.prime.timesSubscription.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

public class OrderIdGeneratorUtil {

    private static final int SSO_ID_PREFIX_LENGTH = 3;
    private static final int TICKET_ID_PREFIX_LENGTH = 3;
    private static final int INDEPENDENT_SUFFIX_LENGTH = 18;

    public static final String generateOrderId(String ssoId, String ticketId, int length){
        StringBuilder sb = new StringBuilder();
        sb.append(ssoId.substring(0, Math.min(ssoId.length(), SSO_ID_PREFIX_LENGTH)));
        if(StringUtils.isNotEmpty(ticketId)){
            sb.append(ticketId.substring(0, Math.min(ssoId.length(), TICKET_ID_PREFIX_LENGTH)));
            RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(CharacterPredicates.LETTERS,
                    CharacterPredicates.DIGITS).build();
            sb.append(generator.generate(Math.max(length-SSO_ID_PREFIX_LENGTH-TICKET_ID_PREFIX_LENGTH, INDEPENDENT_SUFFIX_LENGTH)));
            return sb.toString();
        }else{
            RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(CharacterPredicates.LETTERS,
                    CharacterPredicates.DIGITS).build();
            sb.append(generator.generate(Math.max(length-SSO_ID_PREFIX_LENGTH, INDEPENDENT_SUFFIX_LENGTH)));
            return sb.toString();
        }
    }
}
