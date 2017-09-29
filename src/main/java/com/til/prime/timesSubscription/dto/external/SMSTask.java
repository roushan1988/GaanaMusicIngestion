package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.TaskPriorityEnum;

import java.io.Serializable;
import java.util.Map;

public class SMSTask implements Serializable {
    private String templateKey;
    private String partnerId;
    private String group;
    private String mobileNumber;
    private Map<String ,String > context;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public TaskPriorityEnum getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriorityEnum taskPriority) {
        this.taskPriority = taskPriority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SMSTask{");
        sb.append("templateKey='").append(templateKey).append('\'');
        sb.append(", partnerId='").append(partnerId).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", mobileNumber='").append(mobileNumber).append('\'');
        sb.append(", context=").append(context);
        sb.append(", taskPriority=").append(taskPriority);
        sb.append('}');
        return sb.toString();
    }
}
