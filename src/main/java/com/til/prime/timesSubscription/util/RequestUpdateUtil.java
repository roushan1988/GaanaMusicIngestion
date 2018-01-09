package com.til.prime.timesSubscription.util;

import com.til.prime.timesSubscription.dto.external.GenericRequest;
import com.til.prime.timesSubscription.dto.external.UserDTO;
import org.apache.commons.lang3.StringUtils;

public class RequestUpdateUtil {
    public static final void updateRequest(GenericRequest request, String ssoId, String ticketId, String mobile){
        UserDTO user = request.getUser();
        if(user==null){
            user = new UserDTO();
            request.setUser(user);
        }
        if(StringUtils.isNotEmpty(ssoId)){
            user.setSsoId(ssoId);
        }
        if(StringUtils.isNotEmpty(ticketId)){
            user.setTicketId(ticketId);
        }
        if(StringUtils.isNotEmpty(mobile)){
            user.setMobile(mobile);
        }
    }
}
