
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
        "condition",
        "wind_mph",
        "wind_kph",
        "wind_degree",
        "wind_dir",
        "pressure_mb",
        "pressure_in",
        "precip_mm",
        "precip_in",
        "humidity",
        "cloud",
        "feelslike_c",
        "feelslike_f",
        "vis_km",
        "vis_miles",
        "uv",
        "gust_mph",
        "gust_kph"
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
    @JsonProperty("wind_mph")
    private Float windMph;
    @JsonProperty("wind_kph")
    private Float windKph;
    @JsonProperty("wind_degree")
    private Integer windDegree;
    @JsonProperty("wind_dir")
    private String windDir;
    @JsonProperty("pressure_mb")
    private Float pressureMb;
    @JsonProperty("pressure_in")
    private Float pressureIn;
    @JsonProperty("precip_mm")
    private Float precipMm;
    @JsonProperty("precip_in")
    private Float precipIn;
    @JsonProperty("humidity")
    private Integer humidity;
    @JsonProperty("cloud")
    private Integer cloud;
    @JsonProperty("feelslike_c")
    private Float feelslikeC;
    @JsonProperty("feelslike_f")
    private Float feelslikeF;
    @JsonProperty("vis_km")
    private Float visKm;
    @JsonProperty("vis_miles")
    private Float visMiles;
    @JsonProperty("uv")
    private Float uv;
    @JsonProperty("gust_mph")
    private Float gustMph;
    @JsonProperty("gust_kph")
    private Float gustKph;

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

    @JsonProperty("wind_mph")
    public Float getWindMph() {
        return windMph;
    }

    @JsonProperty("wind_mph")
    public void setWindMph(Float windMph) {
        this.windMph = windMph;
    }

    @JsonProperty("wind_kph")
    public Float getWindKph() {
        return windKph;
    }

    public int getWindMps() {
        return Math.round(windKph * 1000 / 3600);
    }

    @JsonProperty("wind_kph")
    public void setWindKph(Float windKph) {
        this.windKph = windKph;
    }

    @JsonProperty("wind_degree")
    public Integer getWindDegree() {
        return windDegree;
    }

    @JsonProperty("wind_degree")
    public void setWindDegree(Integer windDegree) {
        this.windDegree = windDegree;
    }

    @JsonProperty("wind_dir")
    public String getWindDir() {
        return windDir;
    }

    @JsonProperty("wind_dir")
    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    @JsonProperty("pressure_mb")
    public Float getPressureMb() {
        return pressureMb;
    }

    @JsonProperty("pressure_mb")
    public void setPressureMb(Float pressureMb) {
        this.pressureMb = pressureMb;
    }

    @JsonProperty("pressure_in")
    public Float getPressureIn() {
        return pressureIn;
    }

    @JsonProperty("pressure_in")
    public void setPressureIn(Float pressureIn) {
        this.pressureIn = pressureIn;
    }

    @JsonProperty("precip_mm")
    public Float getPrecipMm() {
        return precipMm;
    }

    @JsonProperty("precip_mm")
    public void setPrecipMm(Float precipMm) {
        this.precipMm = precipMm;
    }

    @JsonProperty("precip_in")
    public Float getPrecipIn() {
        return precipIn;
    }

    @JsonProperty("precip_in")
    public void setPrecipIn(Float precipIn) {
        this.precipIn = precipIn;
    }

    @JsonProperty("humidity")
    public Integer getHumidity() {
        return humidity;
    }

    @JsonProperty("humidity")
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    @JsonProperty("cloud")
    public Integer getCloud() {
        return cloud;
    }

    @JsonProperty("cloud")
    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    @JsonProperty("feelslike_c")
    public Float getFeelslikeC() {
        return feelslikeC;
    }

    @JsonProperty("feelslike_c")
    public void setFeelslikeC(Float feelslikeC) {
        this.feelslikeC = feelslikeC;
    }

    @JsonProperty("feelslike_f")
    public Float getFeelslikeF() {
        return feelslikeF;
    }

    @JsonProperty("feelslike_f")
    public void setFeelslikeF(Float feelslikeF) {
        this.feelslikeF = feelslikeF;
    }

    @JsonProperty("vis_km")
    public Float getVisKm() {
        return visKm;
    }

    @JsonProperty("vis_km")
    public void setVisKm(Float visKm) {
        this.visKm = visKm;
    }

    @JsonProperty("vis_miles")
    public Float getVisMiles() {
        return visMiles;
    }

    @JsonProperty("vis_miles")
    public void setVisMiles(Float visMiles) {
        this.visMiles = visMiles;
    }

    @JsonProperty("uv")
    public Float getUv() {
        return uv;
    }

    @JsonProperty("uv")
    public void setUv(Float uv) {
        this.uv = uv;
    }

    @JsonProperty("gust_mph")
    public Float getGustMph() {
        return gustMph;
    }

    @JsonProperty("gust_mph")
    public void setGustMph(Float gustMph) {
        this.gustMph = gustMph;
    }

    @JsonProperty("gust_kph")
    public Float getGustKph() {
        return gustKph;
    }

    @JsonProperty("gust_kph")
    public void setGustKph(Float gustKph) {
        this.gustKph = gustKph;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Current.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lastUpdatedEpoch");
        sb.append('=');
        sb.append(((this.lastUpdatedEpoch == null) ? "<null>" : this.lastUpdatedEpoch));
        sb.append(',');
        sb.append("lastUpdated");
        sb.append('=');
        sb.append(((this.lastUpdated == null) ? "<null>" : this.lastUpdated));
        sb.append(',');
        sb.append("tempC");
        sb.append('=');
        sb.append(((this.tempC == null) ? "<null>" : this.tempC));
        sb.append(',');
        sb.append("tempF");
        sb.append('=');
        sb.append(((this.tempF == null) ? "<null>" : this.tempF));
        sb.append(',');
        sb.append("isDay");
        sb.append('=');
        sb.append(((this.isDay == null) ? "<null>" : this.isDay));
        sb.append(',');
        sb.append("condition");
        sb.append('=');
        sb.append(((this.condition == null) ? "<null>" : this.condition));
        sb.append(',');
        sb.append("windMph");
        sb.append('=');
        sb.append(((this.windMph == null) ? "<null>" : this.windMph));
        sb.append(',');
        sb.append("windKph");
        sb.append('=');
        sb.append(((this.windKph == null) ? "<null>" : this.windKph));
        sb.append(',');
        sb.append("windDegree");
        sb.append('=');
        sb.append(((this.windDegree == null) ? "<null>" : this.windDegree));
        sb.append(',');
        sb.append("windDir");
        sb.append('=');
        sb.append(((this.windDir == null) ? "<null>" : this.windDir));
        sb.append(',');
        sb.append("pressureMb");
        sb.append('=');
        sb.append(((this.pressureMb == null) ? "<null>" : this.pressureMb));
        sb.append(',');
        sb.append("pressureIn");
        sb.append('=');
        sb.append(((this.pressureIn == null) ? "<null>" : this.pressureIn));
        sb.append(',');
        sb.append("precipMm");
        sb.append('=');
        sb.append(((this.precipMm == null) ? "<null>" : this.precipMm));
        sb.append(',');
        sb.append("precipIn");
        sb.append('=');
        sb.append(((this.precipIn == null) ? "<null>" : this.precipIn));
        sb.append(',');
        sb.append("humidity");
        sb.append('=');
        sb.append(((this.humidity == null) ? "<null>" : this.humidity));
        sb.append(',');
        sb.append("cloud");
        sb.append('=');
        sb.append(((this.cloud == null) ? "<null>" : this.cloud));
        sb.append(',');
        sb.append("feelslikeC");
        sb.append('=');
        sb.append(((this.feelslikeC == null) ? "<null>" : this.feelslikeC));
        sb.append(',');
        sb.append("feelslikeF");
        sb.append('=');
        sb.append(((this.feelslikeF == null) ? "<null>" : this.feelslikeF));
        sb.append(',');
        sb.append("visKm");
        sb.append('=');
        sb.append(((this.visKm == null) ? "<null>" : this.visKm));
        sb.append(',');
        sb.append("visMiles");
        sb.append('=');
        sb.append(((this.visMiles == null) ? "<null>" : this.visMiles));
        sb.append(',');
        sb.append("uv");
        sb.append('=');
        sb.append(((this.uv == null) ? "<null>" : this.uv));
        sb.append(',');
        sb.append("gustMph");
        sb.append('=');
        sb.append(((this.gustMph == null) ? "<null>" : this.gustMph));
        sb.append(',');
        sb.append("gustKph");
        sb.append('=');
        sb.append(((this.gustKph == null) ? "<null>" : this.gustKph));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.tempF == null) ? 0 : this.tempF.hashCode()));
        result = ((result * 31) + ((this.precipMm == null) ? 0 : this.precipMm.hashCode()));
        result = ((result * 31) + ((this.uv == null) ? 0 : this.uv.hashCode()));
        result = ((result * 31) + ((this.feelslikeC == null) ? 0 : this.feelslikeC.hashCode()));
        result = ((result * 31) + ((this.gustMph == null) ? 0 : this.gustMph.hashCode()));
        result = ((result * 31) + ((this.gustKph == null) ? 0 : this.gustKph.hashCode()));
        result = ((result * 31) + ((this.windDir == null) ? 0 : this.windDir.hashCode()));
        result = ((result * 31) + ((this.pressureIn == null) ? 0 : this.pressureIn.hashCode()));
        result = ((result * 31) + ((this.precipIn == null) ? 0 : this.precipIn.hashCode()));
        result = ((result * 31) + ((this.isDay == null) ? 0 : this.isDay.hashCode()));
        result = ((result * 31) + ((this.cloud == null) ? 0 : this.cloud.hashCode()));
        result = ((result * 31) + ((this.lastUpdated == null) ? 0 : this.lastUpdated.hashCode()));
        result = ((result * 31) + ((this.condition == null) ? 0 : this.condition.hashCode()));
        result = ((result * 31) + ((this.windMph == null) ? 0 : this.windMph.hashCode()));
        result = ((result * 31) + ((this.visKm == null) ? 0 : this.visKm.hashCode()));
        result = ((result * 31) + ((this.windKph == null) ? 0 : this.windKph.hashCode()));
        result = ((result * 31) + ((this.humidity == null) ? 0 : this.humidity.hashCode()));
        result = ((result * 31) + ((this.feelslikeF == null) ? 0 : this.feelslikeF.hashCode()));
        result = ((result * 31) + ((this.windDegree == null) ? 0 : this.windDegree.hashCode()));
        result = ((result * 31) + ((this.visMiles == null) ? 0 : this.visMiles.hashCode()));
        result = ((result * 31) + ((this.pressureMb == null) ? 0 : this.pressureMb.hashCode()));
        result = ((result * 31) + ((this.lastUpdatedEpoch == null) ? 0 : this.lastUpdatedEpoch.hashCode()));
        result = ((result * 31) + ((this.tempC == null) ? 0 : this.tempC.hashCode()));
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
        return ((((((((((((((((((((((((this.tempF == rhs.tempF) || ((this.tempF != null) && this.tempF.equals(rhs.tempF))) && ((this.precipMm == rhs.precipMm) || ((this.precipMm != null) && this.precipMm.equals(rhs.precipMm)))) && ((this.uv == rhs.uv) || ((this.uv != null) && this.uv.equals(rhs.uv)))) && ((this.feelslikeC == rhs.feelslikeC) || ((this.feelslikeC != null) && this.feelslikeC.equals(rhs.feelslikeC)))) && ((this.gustMph == rhs.gustMph) || ((this.gustMph != null) && this.gustMph.equals(rhs.gustMph)))) && ((this.gustKph == rhs.gustKph) || ((this.gustKph != null) && this.gustKph.equals(rhs.gustKph)))) && ((this.windDir == rhs.windDir) || ((this.windDir != null) && this.windDir.equals(rhs.windDir)))) && ((this.pressureIn == rhs.pressureIn) || ((this.pressureIn != null) && this.pressureIn.equals(rhs.pressureIn)))) && ((this.precipIn == rhs.precipIn) || ((this.precipIn != null) && this.precipIn.equals(rhs.precipIn)))) && ((this.isDay == rhs.isDay) || ((this.isDay != null) && this.isDay.equals(rhs.isDay)))) && ((this.cloud == rhs.cloud) || ((this.cloud != null) && this.cloud.equals(rhs.cloud)))) && ((this.lastUpdated == rhs.lastUpdated) || ((this.lastUpdated != null) && this.lastUpdated.equals(rhs.lastUpdated)))) && ((this.condition == rhs.condition) || ((this.condition != null) && this.condition.equals(rhs.condition)))) && ((this.windMph == rhs.windMph) || ((this.windMph != null) && this.windMph.equals(rhs.windMph)))) && ((this.visKm == rhs.visKm) || ((this.visKm != null) && this.visKm.equals(rhs.visKm)))) && ((this.windKph == rhs.windKph) || ((this.windKph != null) && this.windKph.equals(rhs.windKph)))) && ((this.humidity == rhs.humidity) || ((this.humidity != null) && this.humidity.equals(rhs.humidity)))) && ((this.feelslikeF == rhs.feelslikeF) || ((this.feelslikeF != null) && this.feelslikeF.equals(rhs.feelslikeF)))) && ((this.windDegree == rhs.windDegree) || ((this.windDegree != null) && this.windDegree.equals(rhs.windDegree)))) && ((this.visMiles == rhs.visMiles) || ((this.visMiles != null) && this.visMiles.equals(rhs.visMiles)))) && ((this.pressureMb == rhs.pressureMb) || ((this.pressureMb != null) && this.pressureMb.equals(rhs.pressureMb)))) && ((this.lastUpdatedEpoch == rhs.lastUpdatedEpoch) || ((this.lastUpdatedEpoch != null) && this.lastUpdatedEpoch.equals(rhs.lastUpdatedEpoch)))) && ((this.tempC == rhs.tempC) || ((this.tempC != null) && this.tempC.equals(rhs.tempC))));
    }

}
