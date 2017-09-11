package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.GenerateOrderRequest;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.util.OrderIdGeneratorUtil;
import com.til.prime.timesSubscription.util.TimeUtils;
import sun.dc.pr.PRError;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="user_subscription")
public class UserSubscriptionModel extends BaseModel {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
    @Column(name="ticket_id")
    private String ticketId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_variant_id", nullable = false)
    private SubscriptionVariantModel subscriptionVariant;
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
    @Enumerated(EnumType.STRING)
    private BusinessEnum business;
    @Column(name="auto_renewal")
    private boolean autoRenewal;
    @Column
    @Enumerated(EnumType.STRING)
    private ChannelEnum channel;
    @Column
    @Enumerated(EnumType.STRING)
    private PlatformEnum platform;

    public UserSubscriptionModel() {
    }

    public UserSubscriptionModel(UserSubscriptionModel model, GenerateOrderRequest request) {
        this.user = model.getUser();
        this.ticketId = model.getTicketId();
        this.subscriptionVariant = model.subscriptionVariant;
        this.startDate = model.getStartDate();
        this.endDate = model.getEndDate();
        this.planStatus = model.getPlanStatus();
        this.transactionStatus = model.getTransactionStatus();
        this.business = model.getBusiness();
        this.channel = model.getChannel();
        this.platform = model.getPlatform();
        setCreated(new Date());
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public SubscriptionVariantModel getSubscriptionVariant() {
        return subscriptionVariant;
    }

    public void setSubscriptionVariant(SubscriptionVariantModel subscriptionVariant) {
        this.subscriptionVariant = subscriptionVariant;
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

    public boolean isAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
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
