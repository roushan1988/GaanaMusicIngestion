package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.dto.external.GenerateOrderRequest;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.util.TimeUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="user_subscription")
public class UserSubscriptionModel extends BaseModel {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
    @Column(name="ticket_id")
    private String ticketId;
    @ManyToOne(fetch = FetchType.EAGER)
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
    @Column(name="status_published")
    private boolean statusPublished;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;
    @Column(name="plan_status")
    @Enumerated(EnumType.STRING)
    private PlanStatusEnum planStatus;
    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    @Column(name="status_date")
    private Date statusDate;
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
    @Column(name="refunded_amount")
    private BigDecimal refundedAmount;

    public UserSubscriptionModel() {
    }

    public UserSubscriptionModel(UserSubscriptionModel model, GenerateOrderRequest request, boolean renewalRequest) {
        Date date = new Date();
        this.user = model.getUser();
        this.ticketId = model.getTicketId();
        this.subscriptionVariant = model.subscriptionVariant;
        this.business = model.getBusiness();
        this.channel = model.getChannel();
        setCreated(date);
        if(renewalRequest){
            this.startDate = TimeUtils.addMillisInDate(model.getEndDate(), 1);
            this.endDate = TimeUtils.addDaysInDate(startDate, model.getSubscriptionVariant().getDurationDays().intValue());
            this.platform = request.isJob()? PlatformEnum.JOB : PlatformEnum.valueOf(request.getPlatform());
            this.planStatus = PlanStatusEnum.INIT;
            this.transactionStatus = TransactionStatusEnum.SUBSCRIPTION_TRANS_INITIATED;
            this.status = StatusEnum.getStatusForUserSubscription(this.startDate, this.endDate, date);
        }else{
            this.startDate = model.getStartDate();
            this.endDate = model.getEndDate();
            this.platform = model.getPlatform();
            this.planStatus = model.getPlanStatus();
            this.transactionStatus = model.getTransactionStatus();
            this.status = model.getStatus();
        }
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

    public boolean isStatusPublished() {
        return statusPublished;
    }

    public void setStatusPublished(boolean statusPublished) {
        this.statusPublished = statusPublished;
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

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserSubscriptionModel{");
        sb.append("user=").append(user);
        sb.append(", ticketId='").append(ticketId).append('\'');
        sb.append(", subscriptionVariant=").append(subscriptionVariant);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", paymentMethod='").append(paymentMethod).append('\'');
        sb.append(", paymentReference='").append(paymentReference).append('\'');
        sb.append(", orderCompleted=").append(orderCompleted);
        sb.append(", ssoCommunicated=").append(ssoCommunicated);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", planStatus=").append(planStatus);
        sb.append(", status=").append(status);
        sb.append(", transactionStatus=").append(transactionStatus);
        sb.append(", business=").append(business);
        sb.append(", autoRenewal=").append(autoRenewal);
        sb.append(", channel=").append(channel);
        sb.append(", platform=").append(platform);
        sb.append(", refundedAmount=").append(refundedAmount);
        sb.append('}');
        return sb.toString();
    }
}
