package in.co.techm.gardenautomation.daemon;

import in.co.techm.gardenautomation.api.manager.DripIrrigationManager;
import in.co.techm.gardenautomation.dto.DripName;
import in.co.techm.gardenautomation.dto.DripRequest;
import in.co.techm.gardenautomation.dto.exception.InvalidPinConfiguration;
import in.co.techm.gardenautomation.dto.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class Drip implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Drip.class);
    private static final String DRIP_ON_TIME = "06:00:00";
    private final DripIrrigationManager dripIrrigationManager;
    private final DripRequest dripRequest;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private long lastExecutedOn;

    @Autowired
    public Drip(final DripIrrigationManager dripIrrigationManager) {
        this.dripIrrigationManager = dripIrrigationManager;
        this.dripRequest = new DripRequest(true, DripName.FLOWER, 15);
        this.lastExecutedOn = 0;
    }

    @PostConstruct
    void init() {
        final long initialDelay;
        int i = LocalTime.now().compareTo(LocalTime.parse(DRIP_ON_TIME));
        if (i <= 0) {
            LocalTime l1 = LocalTime.parse(DRIP_ON_TIME);
            LocalTime l2 = LocalTime.now();
            initialDelay = ChronoUnit.MINUTES.between(l2, l1);
        } else {
            initialDelay = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay().plus(6, ChronoUnit.HOURS), ChronoUnit.MINUTES);
        }
        log.info("initial delay is {} minutes", initialDelay);
        scheduler.scheduleWithFixedDelay(this, initialDelay, TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES);
//        scheduler.scheduleWithFixedDelay(this, 0, 10, TimeUnit.SECONDS); //for testing
    }

    @Override
    public void run() {
        log.info("Drip start request received {}", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").format(LocalDateTime.now()));
        try {
            dripIrrigationManager.turnOnDrip(dripRequest);
            lastExecutedOn = System.currentTimeMillis();
        } catch (final InvalidPinConfiguration | InvalidRequestException e) {
            log.error("Unable to turn on drip", e);//todo handle error
        }
        log.info("Watering done {}", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").format(LocalDateTime.now()));
    }
}
