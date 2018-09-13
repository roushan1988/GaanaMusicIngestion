package com.til.prime.timesSubscription.dto.external;

/**
 * @author amarnath.pathak
 * @date 20/08/18
 **/
public class ValuePropDTO {

    private String heading;
    private String subHeading;
    private String sdkCta;
    private String largeCta;
    private String smallCta;
    private boolean payment;
    private boolean redirectToParent;
    private String message;


    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    public String getSdkCta() {
        return sdkCta;
    }

    public void setSdkCta(String sdkCta) {
        this.sdkCta = sdkCta;
    }

    public String getLargeCta() {
        return largeCta;
    }

    public void setLargeCta(String largeCta) {
        this.largeCta = largeCta;
    }

    public String getSmallCta() {
        return smallCta;
    }

    public void setSmallCta(String smallCta) {
        this.smallCta = smallCta;
    }

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public boolean isRedirectToParent() {
        return redirectToParent;
    }

    public void setRedirectToParent(boolean redirectToParent) {
        this.redirectToParent = redirectToParent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValueProp{" +
                "heading='" + heading + '\'' +
                ", subHeading='" + subHeading + '\'' +
                ", message='" + message + '\'' +
                ", sdkCta='" + sdkCta + '\'' +
                ", smallCta='" + smallCta + '\'' +
                ", largeCta='" + largeCta + '\'' +
                ", payment='" + payment + '\'' +
                ", redirectToParent='" + redirectToParent + '\'' +
                '}';
    }
}
