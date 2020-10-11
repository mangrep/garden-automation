package in.co.techm.gardenautomation.api.manager;

import in.co.techm.gardenautomation.service.DripIrrigationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DripIrrigationManager {
    private static final Logger log = LoggerFactory.getLogger(DripIrrigationManager.class);
    private final DripIrrigationService dripIrrigationService;

    @Autowired
    public DripIrrigationManager(DripIrrigationService dripIrrigationService) {
        this.dripIrrigationService = dripIrrigationService;
    }

    public void turnOn() throws InterruptedException {
        dripIrrigationService.turnOn();
    }
}
