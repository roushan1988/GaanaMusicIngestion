package com.til.prime.timesSubscription.dto.external;

import java.util.Date;

public class CustomerSearchDTO {
    private String ssoId;
    private String ticketId;
    private String name;
    private String mobile;
    private String email;
    private String city;
    private Date dateOfActivation;
    private String currentStatus;
    private Date expiryDate;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDTO{");
        sb.append("ssoId='").append(ssoId).append('\'');
        sb.append(", ticketId='").append(ticketId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Date getDateOfActivation() {
		return dateOfActivation;
	}

	public void setDateOfActivation(Date dateOfActivation) {
		this.dateOfActivation = dateOfActivation;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

}
