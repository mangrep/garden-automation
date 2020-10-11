package in.co.techm.gardenautomation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DripRequest {
    private final boolean turnOn;
    private final DripName dripName;
    private final long timeInMin;

    @JsonCreator
    public DripRequest(@JsonProperty("turnOn") final boolean turnOn,
                       @JsonProperty("dripName") final DripName dripName,
                       @JsonProperty("timeInMin") final long timeInMin) {
        this.turnOn = turnOn;
        this.dripName = dripName;
        this.timeInMin = timeInMin;
    }

    public boolean isTurnOn() {
        return turnOn;
    }

    public DripName getDripName() {
        return dripName;
    }

    public long getTimeInMin() {
        return timeInMin;
    }
}
