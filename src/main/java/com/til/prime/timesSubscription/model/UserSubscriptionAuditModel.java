package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user_subscription_audit")
public class UserSubscriptionAuditModel extends BaseModel {
    @Column(name="user_subscription_id")
    private Long userSubscriptionId;
    @Column(name="subscription_variant_id")
    private Long subscriptionVariantId;
    @Column(name="subscription_plan_id")
    private Long subscriptionPlanId;
    @Column
    private EventEnum event;
    @Column(name="user_id")
    private Long userId;
    @Column(name="sso_id")
    private String ssoId;
    @Column(name="ticket_id")
    private String ticketId;
    @Column(name="order_id")
    private String orderId;
    @Column(name="payment_method")
    private String paymentMethod;
    @Column(name="payment_reference")
    private String paymentReference;
    @Column(name="order_completed")
    private boolean orderCompleted;
    @Column(name="sso_communicated")
    private boolean ssoCommunicated;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;
    @Column(name="plan_status")
    @Enumerated(EnumType.STRING)
    private PlanStatusEnum planStatus;
    @Column(name="transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum transactionStatus;
    @Column
    private BusinessEnum business;
    @Column
    private ChannelEnum channel;
    @Column
    private PlatformEnum platform;

    public Long getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(Long userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }

    public Long getSubscriptionVariantId() {
        return subscriptionVariantId;
    }

    public void setSubscriptionVariantId(Long subscriptionVariantId) {
        this.subscriptionVariantId = subscriptionVariantId;
    }

    public Long getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(Long subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public EventEnum getEvent() {
        return event;
    }

    public void setEvent(EventEnum event) {
        this.event = event;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public boolean isOrderCompleted() {
        return orderCompleted;
    }

    public void setOrderCompleted(boolean orderCompleted) {
        this.orderCompleted = orderCompleted;
    }

    public boolean isSsoCommunicated() {
        return ssoCommunicated;
    }

    public void setSsoCommunicated(boolean ssoCommunicated) {
        this.ssoCommunicated = ssoCommunicated;
    }

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

    public PlanStatusEnum getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(PlanStatusEnum planStatus) {
        this.planStatus = planStatus;
    }

    public TransactionStatusEnum getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatusEnum transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BusinessEnum getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }
}
