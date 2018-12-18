package com.til.prime.timesSubscription.pojo;

public class SearchTuple {
    private String language;
    private String title;

    public SearchTuple(String language, String title) {
        this.language = language;
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchTuple{");
        sb.append("language='").append(language).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
