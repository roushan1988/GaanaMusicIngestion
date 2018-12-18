package com.til.prime.timesSubscription.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Gaana DB schema POJO
 * 
 * @author manas.saxena
 *
 *
 * mysql -B -uroot gaana -e "SELECT track_id FROM temp_tg_album_enrich;" > test.csv
 */
@Entity
@Table(name = "poc_gaana_songs")
public class MxGaanaDbEntity extends BaseModel {

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<YTSearchResultsEntity> songs;
    
    @Column(name = "track_id", nullable = false)
    private long trackId;

    @Column(name = "isrc_code", nullable = true)
    private String isrcCode;

    @Column(name = "track_title", nullable = true)
    private String trackTitle;

    @Column(name = "album_title", nullable = true)
    private String albumTitle;

    @Column(name = "release_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Column(name = "album_release_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date albumReleaseDate;

    @Column(name = "singer", nullable = true)
    private String singer;

    @Column(name = "composer", nullable = true)
    private String composer;

    @Column(name = "actor", nullable = true)
    private String actor;

    @Column(name = "actress", nullable = true)
    private String actress;

    @Column(name = "language", nullable = true)
    private String language;

    @Column(name = "parental_advisory", nullable = true)
    private String parentalAdvisory;

    @Column(name = "lyricist", nullable = true)
    private String lyricist;

    @Column(name = "label", nullable = true)
    private String label;
    
    @Column(name = "album_thumbnail_path", nullable = true)
    private String albumThumbnailPath;
    
    @Column(name = "genres", nullable = true)
    private String genres;
    
    @Column(name = "popularity_index", nullable = true)
    private Long popularityIndex;
    
    @Column(name = "youtube_id", nullable = true)
    private String youtubeId;

    @Column(name = "valid", nullable = true)
    private Boolean valid;

    @Column
    private String thumbnail;

    @Column(name="max_resolution_thumbnail")
    private String maxResolutionThumbnail;

    @Column(name="s3_album_thumbnail_path")
    private String s3AlbumThumbnailPath;

    @Column(name="s3_video_thumbnail_path")
    private String s3VideoThumbnailPath;

    @Column(name="job_tag")
    private String jobTag;

    @Column(name="created")
    private Date created;
    @Column(name="updated", insertable=false, updatable = false)
    private Date updated;
    @Column(name="deleted")
    private boolean deleted;

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getIsrcCode() {
        return isrcCode;
    }

    public void setIsrcCode(String isrcCode) {
        this.isrcCode = isrcCode;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getAlbumReleaseDate() {
        return albumReleaseDate;
    }

    public void setAlbumReleaseDate(Date albumReleaseDate) {
        this.albumReleaseDate = albumReleaseDate;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getActress() {
        return actress;
    }

    public void setActress(String actress) {
        this.actress = actress;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getParentalAdvisory() {
        return parentalAdvisory;
    }

    public void setParentalAdvisory(String parentalAdvisory) {
        this.parentalAdvisory = parentalAdvisory;
    }

    public String getLyricist() {
        return lyricist;
    }

    public void setLyricist(String lyricist) {
        this.lyricist = lyricist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAlbumThumbnailPath() {
        return albumThumbnailPath;
    }

    public void setAlbumThumbnailPath(String albumThumbnailPath) {
        this.albumThumbnailPath = albumThumbnailPath;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Long getPopularityIndex() {
        return popularityIndex;
    }

    public void setPopularityIndex(Long popularityIndex) {
        this.popularityIndex = popularityIndex;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<YTSearchResultsEntity> getSongs() {
        return songs;
    }

    public void setSongs(List<YTSearchResultsEntity> songs) {
        this.songs = songs;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
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

    public String getS3AlbumThumbnailPath() {
        return s3AlbumThumbnailPath;
    }

    public void setS3AlbumThumbnailPath(String s3AlbumThumbnailPath) {
        this.s3AlbumThumbnailPath = s3AlbumThumbnailPath;
    }

    public String getS3VideoThumbnailPath() {
        return s3VideoThumbnailPath;
    }

    public void setS3VideoThumbnailPath(String s3VideoThumbnailPath) {
        this.s3VideoThumbnailPath = s3VideoThumbnailPath;
    }

    public String getJobTag() {
        return jobTag;
    }

    public void setJobTag(String jobTag) {
        this.jobTag = jobTag;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
