package in.co.techm.gardenautomation.api.manager;

import in.co.techm.gardenautomation.dto.DripRequest;
import in.co.techm.gardenautomation.dto.exception.InvalidPinConfiguration;
import in.co.techm.gardenautomation.dto.exception.InvalidRequestException;
import in.co.techm.gardenautomation.service.IrrigationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class DripIrrigationManager {
    private static final Logger log = LoggerFactory.getLogger(DripIrrigationManager.class);
    private static final long MAX_DRIP_ON_TIME_IN_MIN = 15;
    private final IrrigationService irrigationService;
    private ExecutorService pool;

    @Autowired
    public DripIrrigationManager(IrrigationService irrigationService) {
        this.irrigationService = irrigationService;
    }

    @PostConstruct
    void init() {
        pool = Executors.newFixedThreadPool(2);
    }

    public void turnOn(final DripRequest dripRequest) throws InvalidPinConfiguration, InvalidRequestException {
        if (dripRequest.getTimeInMin() > MAX_DRIP_ON_TIME_IN_MIN || dripRequest.getTimeInMin() < 0) {
            throw new InvalidRequestException(String.format("Max drip on time is %s", MAX_DRIP_ON_TIME_IN_MIN));
        }
        irrigationService.turnOnDrip(dripRequest.getDripName());
        final Executor executor = CompletableFuture.delayedExecutor(dripRequest.getTimeInMin(), TimeUnit.SECONDS, pool);
        CompletableFuture.runAsync(() -> {
            try {
                irrigationService.turnOffDrip(dripRequest.getDripName());
            } catch (final Exception e) {
                log.error("Unable to stop drip  {} ", dripRequest.getDripName(), e);
            }
        }, executor);
    }
}
