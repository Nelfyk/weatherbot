
package com.ruslanburduzhan.weatherbot.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "last_updated_epoch",
    "last_updated",
    "temp_c",
    "temp_f",
    "is_day",
    "condition"
})
public class Current {

    @JsonProperty("last_updated_epoch")
    private Integer lastUpdatedEpoch;
    @JsonProperty("last_updated")
    private String lastUpdated;
    @JsonProperty("temp_c")
    private Float tempC;
    @JsonProperty("temp_f")
    private Float tempF;
    @JsonProperty("is_day")
    private Integer isDay;
    @JsonProperty("condition")
    private Condition condition;

    @JsonProperty("last_updated_epoch")
    public Integer getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }

    @JsonProperty("last_updated_epoch")
    public void setLastUpdatedEpoch(Integer lastUpdatedEpoch) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
    }

    @JsonProperty("last_updated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("last_updated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty("temp_c")
    public Float getTempC() {
        return tempC;
    }

    @JsonProperty("temp_c")
    public void setTempC(Float tempC) {
        this.tempC = tempC;
    }

    @JsonProperty("temp_f")
    public Float getTempF() {
        return tempF;
    }

    @JsonProperty("temp_f")
    public void setTempF(Float tempF) {
        this.tempF = tempF;
    }

    @JsonProperty("is_day")
    public Integer getIsDay() {
        return isDay;
    }

    @JsonProperty("is_day")
    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }

    @JsonProperty("condition")
    public Condition getCondition() {
        return condition;
    }

    @JsonProperty("condition")
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Current.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lastUpdatedEpoch");
        sb.append('=');
        sb.append(((this.lastUpdatedEpoch == null)?"<null>":this.lastUpdatedEpoch));
        sb.append(',');
        sb.append("lastUpdated");
        sb.append('=');
        sb.append(((this.lastUpdated == null)?"<null>":this.lastUpdated));
        sb.append(',');
        sb.append("tempC");
        sb.append('=');
        sb.append(((this.tempC == null)?"<null>":this.tempC));
        sb.append(',');
        sb.append("tempF");
        sb.append('=');
        sb.append(((this.tempF == null)?"<null>":this.tempF));
        sb.append(',');
        sb.append("isDay");
        sb.append('=');
        sb.append(((this.isDay == null)?"<null>":this.isDay));
        sb.append(',');
        sb.append("condition");
        sb.append('=');
        sb.append(((this.condition == null)?"<null>":this.condition));
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
        result = ((result* 31)+((this.tempF == null)? 0 :this.tempF.hashCode()));
        result = ((result* 31)+((this.lastUpdated == null)? 0 :this.lastUpdated.hashCode()));
        result = ((result* 31)+((this.condition == null)? 0 :this.condition.hashCode()));
        result = ((result* 31)+((this.lastUpdatedEpoch == null)? 0 :this.lastUpdatedEpoch.hashCode()));
        result = ((result* 31)+((this.tempC == null)? 0 :this.tempC.hashCode()));
        result = ((result* 31)+((this.isDay == null)? 0 :this.isDay.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Current)) {
            return false;
        }
        Current rhs = ((Current) other);
        return (((((((this.tempF == rhs.tempF)||((this.tempF!= null)&&this.tempF.equals(rhs.tempF)))&&((this.lastUpdated == rhs.lastUpdated)||((this.lastUpdated!= null)&&this.lastUpdated.equals(rhs.lastUpdated))))&&((this.condition == rhs.condition)||((this.condition!= null)&&this.condition.equals(rhs.condition))))&&((this.lastUpdatedEpoch == rhs.lastUpdatedEpoch)||((this.lastUpdatedEpoch!= null)&&this.lastUpdatedEpoch.equals(rhs.lastUpdatedEpoch))))&&((this.tempC == rhs.tempC)||((this.tempC!= null)&&this.tempC.equals(rhs.tempC))))&&((this.isDay == rhs.isDay)||((this.isDay!= null)&&this.isDay.equals(rhs.isDay))));
    }

}
