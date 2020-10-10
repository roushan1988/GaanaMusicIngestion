package com.til.prime.timesSubscription.dto.external;

import java.util.Date;


public class CustomerCRM {

	private String ssoId;
	private String emailId;
	private String mobileNumber;
	
	private String firstName;
	private String lastName;
	
	private String name;
	
	private Date activationDate;
	private Date activationDateValSubs;
	private String subscriptionStatus;
	private Date expiryDate;
	private Date expiryDateValSubs;

	private Double timesPointsBalance;
	private Double paytmBalance;
	private Double mobikwikBalance;	
	private String renewalMode;
	
	private boolean autoRenewalStatus;
	private boolean blockedStatus;
	private Date blockedDate;
	private Date blockedDateValSubs;
	
	private OrderSearchResultsCRM orderSearchResultsCRM;
	private boolean futureSubscriptionExists;
	private boolean activeSubscriptionExists;

	private OrderDetailsCRM activeSubscriptionDetails;

	private OrderSearchResultsCRM autoDebitOrders;
	

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
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	
	public Double getTimesPointsBalance() {
		return timesPointsBalance;
	}
	public void setTimesPointsBalance(Double timesPointsBalance) {
		this.timesPointsBalance = timesPointsBalance;
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
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRenewalMode() {
		return renewalMode;
	}
	public void setRenewalMode(String renewalMode) {
		this.renewalMode = renewalMode;
	}

	public OrderSearchResultsCRM getOrderSearchResultsCRM() {
		return orderSearchResultsCRM;
	}
	public void setOrderSearchResultsCRM(OrderSearchResultsCRM orderSearchResultsCRM) {
		this.orderSearchResultsCRM = orderSearchResultsCRM;
	}
	public OrderSearchResultsCRM getAutoDebitOrders() {
		return autoDebitOrders;
	}
	public void setAutoDebitOrders(OrderSearchResultsCRM autoDebitOrders) {
		this.autoDebitOrders = autoDebitOrders;
	}

	public OrderDetailsCRM getActiveSubscriptionDetails() {
		return activeSubscriptionDetails;
	}
	public void setActiveSubscriptionDetails(OrderDetailsCRM activeSubscriptionDetails) {
		this.activeSubscriptionDetails = activeSubscriptionDetails;
	}
	public boolean isAutoRenewalStatus() {
		return autoRenewalStatus;
	}
	public void setAutoRenewalStatus(boolean autoRenewalStatus) {
		this.autoRenewalStatus = autoRenewalStatus;
	}
	public boolean isBlockedStatus() {
		return blockedStatus;
	}
	public void setBlockedStatus(boolean blockedStatus) {
		this.blockedStatus = blockedStatus;
	}
	public boolean isFutureSubscriptionExists() {
		return futureSubscriptionExists;
	}
	public void setFutureSubscriptionExists(boolean futureSubscriptionExists) {
		this.futureSubscriptionExists = futureSubscriptionExists;
	}
	public boolean isActiveSubscriptionExists() {
		return activeSubscriptionExists;
	}
	public void setActiveSubscriptionExists(boolean activeSubscriptionExists) {
		this.activeSubscriptionExists = activeSubscriptionExists;
	}
	public Date getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Date getBlockedDate() {
		return blockedDate;
	}
	public void setBlockedDate(Date blockedDate) {
		this.blockedDate = blockedDate;
	}
	public Date getActivationDateValSubs() {
		return activationDateValSubs;
	}
	public void setActivationDateValSubs(Date activationDateValSubs) {
		this.activationDateValSubs = activationDateValSubs;
	}
	public Date getExpiryDateValSubs() {
		return expiryDateValSubs;
	}
	public void setExpiryDateValSubs(Date expiryDateValSubs) {
		this.expiryDateValSubs = expiryDateValSubs;
	}
	public Date getBlockedDateValSubs() {
		return blockedDateValSubs;
	}
	public void setBlockedDateValSubs(Date blockedDateValSubs) {
		this.blockedDateValSubs = blockedDateValSubs;
	}
	
}
