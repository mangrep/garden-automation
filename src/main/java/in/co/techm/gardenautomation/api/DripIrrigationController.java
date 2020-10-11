package in.co.techm.gardenautomation.api;

import in.co.techm.gardenautomation.api.manager.DripIrrigationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static in.co.techm.gardenautomation.configuration.Constants.ERROR_MESSAGE;
import static in.co.techm.gardenautomation.configuration.Constants.GENERIC_SERVER_ERROR_MSG;
import static in.co.techm.gardenautomation.configuration.Constants.GENERIC_SUCCESS_RESPONSE;

@Controller
public class DripIrrigationController {
    private final DripIrrigationManager manager;

    @Autowired
    public DripIrrigationController(final DripIrrigationManager manager) {
        this.manager = manager;
    }

    @ResponseBody
    @GetMapping(value = "/drip", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity turnOn(final HttpServletRequest request) {
        try {
            manager.turnOn();
            return ResponseEntity.ok(GENERIC_SUCCESS_RESPONSE);
        } catch (final InterruptedException e) {
            return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, GENERIC_SERVER_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
