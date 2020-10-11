package in.co.techm.gardenautomation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GpioPinMapping {
    private final DripPinMapping dripMapping;
    private final SprinklerMapping sprinklerMapping;

    @JsonCreator
    public GpioPinMapping(@JsonProperty("dripMapping") final DripPinMapping dripMapping,
                          @JsonProperty("sprinklerMapping") final SprinklerMapping sprinklerMapping) {
        this.dripMapping = dripMapping;
        this.sprinklerMapping = sprinklerMapping;
    }

    public DripPinMapping getDripMapping() {
        return dripMapping;
    }

    public SprinklerMapping getSprinklerMapping() {
        return sprinklerMapping;
    }

    public static class DripPinMapping {
        private final Map<DripName, String> pinMapping;

        @JsonCreator
        public DripPinMapping(final Map<DripName, String> pinMapping) {
            this.pinMapping = pinMapping;
        }

        public Map<DripName, String> getPinMapping() {
            return pinMapping;
        }
    }

    public static class SprinklerMapping {
        private final Map<SprinklerName, String> pinMapping;

        @JsonCreator
        public SprinklerMapping(final Map<SprinklerName, String> pinMapping) {
            this.pinMapping = pinMapping;
        }

        public Map<SprinklerName, String> getPinMapping() {
            return pinMapping;
        }
    }
}
