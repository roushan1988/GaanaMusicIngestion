package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.TaskPriorityEnum;

import java.io.Serializable;
import java.util.Map;

public class EmailTask implements Serializable {
    private String templateKey;
    private String partnerId;
    private String group;
    private String emailId;
    private String fromName;
    private String fromEmail;
    private Map<String,String> context;
    private String ctaKey;
    private TaskPriorityEnum taskPriority;

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public String getCtaKey() {
        return ctaKey;
    }

    public void setCtaKey(String ctaKey) {
        this.ctaKey = ctaKey;
    }

    public TaskPriorityEnum getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriorityEnum taskPriority) {
        this.taskPriority = taskPriority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EmailTask{");
        sb.append("templateKey='").append(templateKey).append('\'');
        sb.append(", partnerId='").append(partnerId).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", emailId='").append(emailId).append('\'');
        sb.append(", fromName='").append(fromName).append('\'');
        sb.append(", fromEmail='").append(fromEmail).append('\'');
        sb.append(", context=").append(context);
        sb.append(", ctaKey='").append(ctaKey).append('\'');
        sb.append(", taskPriority=").append(taskPriority);
        sb.append('}');
        return sb.toString();
    }
}