
package com.ruslanburduzhan.weatherbot.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "region",
    "country",
    "lat",
    "lon",
    "tz_id",
    "localtime_epoch",
    "localtime"
})
public class Location {

    @JsonProperty("name")
    private String name;
    @JsonProperty("region")
    private String region;
    @JsonProperty("country")
    private String country;
    @JsonProperty("lat")
    private Float lat;
    @JsonProperty("lon")
    private Float lon;
    @JsonProperty("tz_id")
    private String tzId;
    @JsonProperty("localtime_epoch")
    private Integer localtimeEpoch;
    @JsonProperty("localtime")
    private String localtime;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("lat")
    public Float getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(Float lat) {
        this.lat = lat;
    }

    @JsonProperty("lon")
    public Float getLon() {
        return lon;
    }

    @JsonProperty("lon")
    public void setLon(Float lon) {
        this.lon = lon;
    }

    @JsonProperty("tz_id")
    public String getTzId() {
        return tzId;
    }

    @JsonProperty("tz_id")
    public void setTzId(String tzId) {
        this.tzId = tzId;
    }

    @JsonProperty("localtime_epoch")
    public Integer getLocaltimeEpoch() {
        return localtimeEpoch;
    }

    @JsonProperty("localtime_epoch")
    public void setLocaltimeEpoch(Integer localtimeEpoch) {
        this.localtimeEpoch = localtimeEpoch;
    }

    @JsonProperty("localtime")
    public String getLocaltime() {
        return localtime;
    }

    @JsonProperty("localtime")
    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Location.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("region");
        sb.append('=');
        sb.append(((this.region == null)?"<null>":this.region));
        sb.append(',');
        sb.append("country");
        sb.append('=');
        sb.append(((this.country == null)?"<null>":this.country));
        sb.append(',');
        sb.append("lat");
        sb.append('=');
        sb.append(((this.lat == null)?"<null>":this.lat));
        sb.append(',');
        sb.append("lon");
        sb.append('=');
        sb.append(((this.lon == null)?"<null>":this.lon));
        sb.append(',');
        sb.append("tzId");
        sb.append('=');
        sb.append(((this.tzId == null)?"<null>":this.tzId));
        sb.append(',');
        sb.append("localtimeEpoch");
        sb.append('=');
        sb.append(((this.localtimeEpoch == null)?"<null>":this.localtimeEpoch));
        sb.append(',');
        sb.append("localtime");
        sb.append('=');
        sb.append(((this.localtime == null)?"<null>":this.localtime));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.localtime == null)? 0 :this.localtime.hashCode()));
        result = ((result* 31)+((this.country == null)? 0 :this.country.hashCode()));
        result = ((result* 31)+((this.tzId == null)? 0 :this.tzId.hashCode()));
        result = ((result* 31)+((this.localtimeEpoch == null)? 0 :this.localtimeEpoch.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.lon == null)? 0 :this.lon.hashCode()));
        result = ((result* 31)+((this.region == null)? 0 :this.region.hashCode()));
        result = ((result* 31)+((this.lat == null)? 0 :this.lat.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Location)) {
            return false;
        }
        Location rhs = ((Location) other);
        return (((((((((this.localtime == rhs.localtime)||((this.localtime!= null)&&this.localtime.equals(rhs.localtime)))&&((this.country == rhs.country)||((this.country!= null)&&this.country.equals(rhs.country))))&&((this.tzId == rhs.tzId)||((this.tzId!= null)&&this.tzId.equals(rhs.tzId))))&&((this.localtimeEpoch == rhs.localtimeEpoch)||((this.localtimeEpoch!= null)&&this.localtimeEpoch.equals(rhs.localtimeEpoch))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.lon == rhs.lon)||((this.lon!= null)&&this.lon.equals(rhs.lon))))&&((this.region == rhs.region)||((this.region!= null)&&this.region.equals(rhs.region))))&&((this.lat == rhs.lat)||((this.lat!= null)&&this.lat.equals(rhs.lat))));
    }

}
