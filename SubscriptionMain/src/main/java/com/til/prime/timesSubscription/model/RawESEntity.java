package com.til.prime.timesSubscription.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Objects;

@Document(indexName = "#{@esIndexName}", type = "tg_album_enrich")
public class RawESEntity {
    @Id
    @Field(type = FieldType.Long)
    private Long trackId;
    @Field(type = FieldType.text, searchAnalyzer = "nGram_analyzer", analyzer = "nGram_analyzer")
    private String trackTitle;
    @Field(type = FieldType.keyword)
    private String language;


    public RawESEntity() {
    }

    public RawESEntity(Long trackId, String trackTitle, String language) {
        this.trackId = trackId;
        this.trackTitle = trackTitle;
        this.language = language;
    }

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawESEntity that = (RawESEntity) o;
        return Objects.equals(trackId, that.trackId) &&
                Objects.equals(trackTitle, that.trackTitle) &&
                Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {

        return Objects.hash(trackId, trackTitle, language);
    }
}
