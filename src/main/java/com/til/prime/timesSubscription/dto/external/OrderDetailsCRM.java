package com.til.prime.timesSubscription.dto.external;

import java.util.Date;
import java.util.List;


public class OrderDetailsCRM {

	private String orderId;
	private Date orderDate;
	private Date orderDateValSubs;
	private Date startDate;
	private Date startDateValSubs;
	private Date endDate;
	private Date endDateValSubs;

	private String currentStatus;
	private Double amount;
	private String channelId;
	private String ip;
	private String deviceId;
	private String version;
	
	private String paymentModes;
	private Double pgAmount;
	private Double timesPointsAmount;
	private String gc;
	private Double gcAmount;
	private String pgStatus;
	private String timesPointsStatus;
	private String gcStatus;
	private String pgMode;
	
	private Double paytmBalance;
	private Double mobikwikBalance;
	private Double timesPointsBalance;
	
	private String pgError;
	private String gcError;
	private String tpError;
	private String pgType;

	
	private String ssoId;
	private String emailId;
	private String mobileNumber;
	private String firstName;
	private String lastName;
	private String name;
	private Date userCreatedDate;
	private Date userCreatedDateValSubs;
	private String userSubscriptionStatus;

	private boolean autoRenewal;
	private String subscriptionPlan;
	private String userSubscriptionId;
	private String variantId;
	
	private String planID; 
	private String planPrice;
	private String planType; 
	private String business;
	private String channel;
	private String platform;
	private String planDurationDays;
	
	
	private Date expiryDate;
	private Date expiryDateValSubs;
	private Date renewalDate;
	private Date renewalDateValSubs;
	private String billedAmount;
	private String renewalAmount;
	private String currency;
	private String country;
	
	private String appVersion;
	
	private List<CRMRefundDetails> crmRefundDetails;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPaymentModes() {
		return paymentModes;
	}

	public void setPaymentModes(String paymentModes) {
		this.paymentModes = paymentModes;
	}

	public Double getPgAmount() {
		return pgAmount;
	}

	public void setPgAmount(Double pgAmount) {
		this.pgAmount = pgAmount;
	}

	public Double getTimesPointsAmount() {
		return timesPointsAmount;
	}

	public void setTimesPointsAmount(Double timesPointsAmount) {
		this.timesPointsAmount = timesPointsAmount;
	}

	public String getGc() {
		return gc;
	}

	public void setGc(String gc) {
		this.gc = gc;
	}

	public Double getGcAmount() {
		return gcAmount;
	}

	public void setGcAmount(Double gcAmount) {
		this.gcAmount = gcAmount;
	}

	public String getPgStatus() {
		return pgStatus;
	}

	public void setPgStatus(String pgStatus) {
		this.pgStatus = pgStatus;
	}

	public String getTimesPointsStatus() {
		return timesPointsStatus;
	}

	public void setTimesPointsStatus(String timesPointsStatus) {
		this.timesPointsStatus = timesPointsStatus;
	}

	public String getGcStatus() {
		return gcStatus;
	}

	public void setGcStatus(String gcStatus) {
		this.gcStatus = gcStatus;
	}

	public String getPgMode() {
		return pgMode;
	}

	public void setPgMode(String pgMode) {
		this.pgMode = pgMode;
	}

	public Double getPaytmBalance() {
		return paytmBalance;
	}

	public void setPaytmBalance(Double paytmBalance) {
		this.paytmBalance = paytmBalance;
	}

	public Double getMobikwikBalance() {
		return mobikwikBalance;
	}

	public void setMobikwikBalance(Double mobikwikBalance) {
		this.mobikwikBalance = mobikwikBalance;
	}

	public Double getTimesPointsBalance() {
		return timesPointsBalance;
	}

	public void setTimesPointsBalance(Double timesPointsBalance) {
		this.timesPointsBalance = timesPointsBalance;
	}

	public String getPgError() {
		return pgError;
	}

	public void setPgError(String pgError) {
		this.pgError = pgError;
	}

	public String getTpError() {
		return tpError;
	}

	public void setTpError(String tpError) {
		this.tpError = tpError;
	}
	
	public String getGcError() {
		return gcError;
	}

	public void setGcError(String gcError) {
		this.gcError = gcError;
	}

	public String getTimesPointsError() {
		return tpError;
	}

	public void setTimesPointsError(String tpError) {
		this.tpError = tpError;
	}

	public String getPgType() {
		return pgType;
	}

	public void setPgType(String pgType) {
		this.pgType = pgType;
	}

	public String getSsoId() {
		return ssoId;
	}

	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getUserCreatedDate() {
		return userCreatedDate;
	}

	public void setUserCreatedDate(Date userCreatedDate) {
		this.userCreatedDate = userCreatedDate;
	}

	public Date getUserCreatedDateValSubs() {
		return userCreatedDateValSubs;
	}

	public void setUserCreatedDateValSubs(Date userCreatedDateValSubs) {
		this.userCreatedDateValSubs = userCreatedDateValSubs;
	}

	public List<CRMRefundDetails> getCrmRefundDetails() {
		return crmRefundDetails;
	}

	public void setCrmRefundDetails(List<CRMRefundDetails> crmRefundDetails) {
		this.crmRefundDetails = crmRefundDetails;
	}

	public String getSubscriptionPlan() {
		return subscriptionPlan;
	}

	public void setSubscriptionPlan(String subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}

	public String getBilledAmount() {
		return billedAmount;
	}

	public void setBilledAmount(String billedAmount) {
		this.billedAmount = billedAmount;
	}

	public String getRenewalAmount() {
		return renewalAmount;
	}

	public void setRenewalAmount(String renewalAmount) {
		this.renewalAmount = renewalAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getUserSubscriptionId() {
		return userSubscriptionId;
	}

	public void setUserSubscriptionId(String userSubscriptionId) {
		this.userSubscriptionId = userSubscriptionId;
	}

	public String getVariantId() {
		return variantId;
	}

	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}

	public String getPlanID() {
		return planID;
	}

	public void setPlanID(String planID) {
		this.planID = planID;
	}

	public String getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(String planPrice) {
		this.planPrice = planPrice;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlanDurationDays() {
		return planDurationDays;
	}

	public void setPlanDurationDays(String planDurationDays) {
		this.planDurationDays = planDurationDays;
	}

	public String getUserSubscriptionStatus() {
		return userSubscriptionStatus;
	}

	public void setUserSubscriptionStatus(String userSubscriptionStatus) {
		this.userSubscriptionStatus = userSubscriptionStatus;
	}
	
	public boolean isAutoRenewal() {
		return autoRenewal;
	}

	public void setAutoRenewal(boolean autoRenewal) {
		this.autoRenewal = autoRenewal;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public Date getOrderDateValSubs() {
		return orderDateValSubs;
	}

	public void setOrderDateValSubs(Date orderDateValSubs) {
		this.orderDateValSubs = orderDateValSubs;
	}

	

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDateValSubs() {
		return startDateValSubs;
	}

	public void setStartDateValSubs(Date startDateValSubs) {
		this.startDateValSubs = startDateValSubs;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDateValSubs() {
		return endDateValSubs;
	}

	public void setEndDateValSubs(Date endDateValSubs) {
		this.endDateValSubs = endDateValSubs;
	}
	
	public Date getExpiryDateValSubs() {
		return expiryDateValSubs;
	}

	public void setExpiryDateValSubs(Date expiryDateValSubs) {
		this.expiryDateValSubs = expiryDateValSubs;
	}

	public Date getRenewalDateValSubs() {
		return renewalDateValSubs;
	}

	public void setRenewalDateValSubs(Date renewalDateValSubs) {
		this.renewalDateValSubs = renewalDateValSubs;
	}


}
