package com.til.prime.timesSubscription.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Gaana DB schema POJO
 * 
 * @author manas.saxena
 *
 *
 * mysql -B -uroot gaana -e "SELECT track_id FROM temp_tg_album_enrich;" > test.csv
 */
@Entity
@Table(name = "tg_album_enrich")
public class RawMxGaanaDbEntity extends BaseModel {

    @Column(name = "track_id", nullable = false)
    private Long trackId;

    @Column(name = "track_title", nullable = true)
    private String trackTitle;

    @Column(name = "album_title", nullable = true)
    private String albumTitle;

    @Column(name = "release_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

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

    @Column(name = "lyricist", nullable = true)
    private String lyricist;

    @Column(name = "label", nullable = true)
    private String label;

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RawMxGaanaDbEntity{");
        sb.append("trackId=").append(trackId);
        sb.append(", trackTitle='").append(trackTitle).append('\'');
        sb.append(", albumTitle='").append(albumTitle).append('\'');
        sb.append(", releaseDate=").append(releaseDate);
        sb.append(", singer='").append(singer).append('\'');
        sb.append(", composer='").append(composer).append('\'');
        sb.append(", actor='").append(actor).append('\'');
        sb.append(", actress='").append(actress).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append(", lyricist='").append(lyricist).append('\'');
        sb.append(", label='").append(label).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
