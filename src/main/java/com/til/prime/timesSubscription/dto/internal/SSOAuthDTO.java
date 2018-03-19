package com.til.prime.timesSubscription.dto.internal;

import java.io.Serializable;

public class SSOAuthDTO implements Serializable {
    private static final long serialVersionUID = 1l;
    private String ssoId;
    private String ticketId;

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SSOAuthDTO{");
        sb.append("ssoId='").append(ssoId).append('\'');
        sb.append(", ticketId='").append(ticketId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
