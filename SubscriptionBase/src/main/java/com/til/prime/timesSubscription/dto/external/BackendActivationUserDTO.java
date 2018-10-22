package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;

import java.util.Objects;

public class BackendActivationUserDTO {
    private String mobile;
    private String email;
    private String firstName;
    private String lastName;
    private Long durationDays;
    private boolean forceActivation;
    private boolean success;
    private String message;
    private BusinessEnum business = BusinessEnum.TIMES_PRIME;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Long durationDays) {
        this.durationDays = durationDays;
    }

    public boolean isForceActivation() {
        return forceActivation;
    }

    public void setForceActivation(boolean forceActivation) {
        this.forceActivation = forceActivation;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessEnum getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackendActivationUserDTO that = (BackendActivationUserDTO) o;
        return Objects.equals(mobile, that.mobile) &&
                Objects.equals(email, that.email) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobile, email, firstName, lastName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BackendActivationUserDTO{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", durationDays=").append(durationDays);
        sb.append(", forceActivation=").append(forceActivation);
        sb.append(", success=").append(success);
        sb.append(", message='").append(message).append('\'');
        sb.append(", business=").append(business);
        sb.append('}');
        return sb.toString();
    }
}
