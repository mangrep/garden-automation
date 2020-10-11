package in.co.techm.gardenautomation.api;

import in.co.techm.gardenautomation.api.manager.DripIrrigationManager;
import in.co.techm.gardenautomation.dto.DripRequest;
import in.co.techm.gardenautomation.dto.exception.InvalidPinConfiguration;
import in.co.techm.gardenautomation.dto.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

import static in.co.techm.gardenautomation.configuration.Constants.ERROR_MESSAGE;
import static in.co.techm.gardenautomation.configuration.Constants.GENERIC_SERVER_ERROR_MSG;
import static in.co.techm.gardenautomation.configuration.Constants.GENERIC_SUCCESS_RESPONSE;

@Controller
public class DripIrrigationController {
    private static final Logger log = LoggerFactory.getLogger(DripIrrigationController.class);
    private final DripIrrigationManager manager;

    @Autowired
    public DripIrrigationController(final DripIrrigationManager manager) {
        this.manager = manager;
    }

    @ResponseBody
    @PostMapping(value = "/drip", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity turnOn(@RequestBody final DripRequest dripRequest) {
        try {
            manager.turnOn(dripRequest);
            return ResponseEntity.ok(GENERIC_SUCCESS_RESPONSE);
        } catch (final InvalidRequestException e) {
            log.info("Unable to start drip {}", e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (final InvalidPinConfiguration e) {
            log.error("Unable to start drip", e);
            return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, GENERIC_SERVER_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
