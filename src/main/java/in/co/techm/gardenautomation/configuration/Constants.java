package in.co.techm.gardenautomation.configuration;

import java.util.Collections;
import java.util.Map;

public final class Constants {
    private Constants() {
    }

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String GENERIC_SERVER_ERROR_MSG = "Something went wrong. Please try again";
    public static final String SUCCESS = "success";
    public static final String STATUS = "status";
    public static final Map<String, String> GENERIC_SUCCESS_RESPONSE = Collections.singletonMap(STATUS, SUCCESS);
}
