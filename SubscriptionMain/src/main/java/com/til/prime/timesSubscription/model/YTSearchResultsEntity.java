package com.til.prime.timesSubscription.model;

import javax.persistence.*;

@Entity
@Table(name="yt_search_results")
public class YTSearchResultsEntity extends BaseModel {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "song_id", nullable = false)
    private MxGaanaDbEntity song;
    @Column
    private String title;
    @Column
    private String url;
    @Column(name="view_count")
    private Long viewCount;
    @Column(name="like_count")
    private Long likeCount;
    @Column
    private String channel;
    @Column(name="channel_verified")
    private Boolean verified;
    @Column
    private String time;
    @Column
    private String thumbnail;
    @Column(name="max_resolution_thumbnail")
    private String maxResolutionThumbnail;


    public MxGaanaDbEntity getSong() {
        return song;
    }

    public void setSong(MxGaanaDbEntity song) {
        this.song = song;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMaxResolutionThumbnail() {
        return maxResolutionThumbnail;
    }

    public void setMaxResolutionThumbnail(String maxResolutionThumbnail) {
        this.maxResolutionThumbnail = maxResolutionThumbnail;
    }
}
