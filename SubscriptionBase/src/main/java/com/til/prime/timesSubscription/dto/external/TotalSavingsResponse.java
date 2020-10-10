package com.til.prime.timesSubscription.dto.external;

import java.util.Date;

public class TotalSavingsResponse extends GenericResponse {
    private Date startDate;
    private Date endDate;
    private Date lastEndDate;
    private Double totalSavings = 0d;
    private int planStatus;
    private String primeId;
    private Integer brandsCount;
    private String annualSavings;
    private String planCta;
    private String savingText;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(Double totalSavings) {
        this.totalSavings = totalSavings;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public String getPrimeId() {
        return primeId;
    }

    public void setPrimeId(String primeId) {
        this.primeId = primeId;
    }

    public Integer getBrandsCount() {
        return brandsCount;
    }

    public void setBrandsCount(Integer brandsCount) {
        this.brandsCount = brandsCount;
    }

    public String getAnnualSavings() {
        return annualSavings;
    }

    public void setAnnualSavings(String annualSavings) {
        this.annualSavings = annualSavings;
    }

    public Date getLastEndDate() {
        return lastEndDate;
    }

    public void setLastEndDate(Date lastEndDate) {
        this.lastEndDate = lastEndDate;
    }

    public String getPlanCta() {
        return planCta;
    }

    public void setPlanCta(String planCta) {
        this.planCta = planCta;
    }

    public String getSavingText() {
        return savingText;
    }

    public void setSavingText(String savingText) {
        this.savingText = savingText;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TotalSavingsResponse{");
        sb.append("startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", lastEndDate=").append(lastEndDate);
        sb.append(", planCta=").append(planCta);
        sb.append(", savingText=").append(savingText);
        sb.append(", totalSavings=").append(totalSavings);
        sb.append(", planStatus=").append(planStatus);
        sb.append(", primeId='").append(primeId).append('\'');
        sb.append(", brandsCount=").append(brandsCount);
        sb.append(", annualSavings='").append(annualSavings).append('\'');
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrorCategory=").append(validationErrorCategory);
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
