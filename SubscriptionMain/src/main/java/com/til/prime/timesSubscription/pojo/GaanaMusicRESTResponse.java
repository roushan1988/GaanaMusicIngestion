package com.til.prime.timesSubscription.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.til.prime.timesSubscription.enums.MusicClassifier;

import java.util.List;

/**
 * Schema for Gaana REST API response
 * 
 * @author manas.saxena
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GaanaMusicRESTResponse {

    @JsonProperty("modified_on")
    private String modifiedOn;

    @JsonProperty("count")
    private String count;

    @JsonProperty("created_on")
    private String createdOn;

    @JsonProperty("tracks")
    private List<Track> tracks;

    private MusicClassifier classifier;

    public GaanaMusicRESTResponse() {
        super();
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public MusicClassifier getClassifier() {
        return classifier;
    }

    public void setClassifier(MusicClassifier classifier) {
        this.classifier = classifier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classifier == null) ? 0 : classifier.hashCode());
        result = prime * result + ((count == null) ? 0 : count.hashCode());
        result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
        result = prime * result + ((modifiedOn == null) ? 0 : modifiedOn.hashCode());
        result = prime * result + ((tracks == null) ? 0 : tracks.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GaanaMusicRESTResponse other = (GaanaMusicRESTResponse) obj;
        if (classifier != other.classifier)
            return false;
        if (count == null) {
            if (other.count != null)
                return false;
        } else if (!count.equals(other.count))
            return false;
        if (createdOn == null) {
            if (other.createdOn != null)
                return false;
        } else if (!createdOn.equals(other.createdOn))
            return false;
        if (modifiedOn == null) {
            if (other.modifiedOn != null)
                return false;
        } else if (!modifiedOn.equals(other.modifiedOn))
            return false;
        if (tracks == null) {
            if (other.tracks != null)
                return false;
        } else if (!tracks.equals(other.tracks))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GaanaMusicRESTResponse [modifiedOn=");
        builder.append(modifiedOn);
        builder.append(", count=");
        builder.append(count);
        builder.append(", createdOn=");
        builder.append(createdOn);
        builder.append(", tracks=");
        builder.append(tracks);
        builder.append(", classifier=");
        builder.append(classifier);
        builder.append("]");
        return builder.toString();
    }

}
