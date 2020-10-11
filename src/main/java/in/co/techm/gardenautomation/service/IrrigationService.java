package in.co.techm.gardenautomation.service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import in.co.techm.gardenautomation.dto.DripName;
import in.co.techm.gardenautomation.dto.GpioPinMapping;
import in.co.techm.gardenautomation.dto.SprinklerName;
import in.co.techm.gardenautomation.dto.exception.InvalidPinConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class IrrigationService implements IIrrigationService {
    private static final Logger log = LoggerFactory.getLogger(IrrigationService.class);
    private final GpioController gpioController;
    private final GpioPinMapping gpioPinMapping;

    @Autowired
    public IrrigationService(final GpioController gpioController, final GpioPinMapping gpioPinMapping) {
        this.gpioController = gpioController;
        this.gpioPinMapping = gpioPinMapping;
    }

    public void turnOn() throws InterruptedException {
        final GpioPinDigitalOutput ledPin7 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_07);
        ledPin7.high();
        Thread.sleep(10000);
        ledPin7.low();
    }

    @Override
    public void turnOnSprinkler(SprinklerName sprinklerName) {
        throw new RuntimeException("Sprinkler not installed");
    }

    @Override
    public void turnOnDrip(final DripName dripName) throws InvalidPinConfiguration {
        final Pin raspPin = getRaspiPin(dripName);
//        final GpioPinDigitalInput gpioPinDigitalInput = provisionPinInput(raspPin);
//        if (gpioPinDigitalInput.isHigh()) {
//            log.info(String.format(" %s drip is already on", dripName.name()));
//            return;
//        }
//        unProvisionPin(gpioPinDigitalInput);
        final GpioPinDigitalOutput gpioPinDigitalOutput = provisionPinOutput(raspPin);
        gpioPinDigitalOutput.high();
        log.info("Drip is on {}", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").format(LocalDateTime.now()));
        unProvisionPin(gpioPinDigitalOutput);
    }

    @Override
    public void turnOffDrip(DripName dripName) throws InvalidPinConfiguration {
        final Pin raspPin = getRaspiPin(dripName);
//        final GpioPinDigitalInput gpioPinDigitalInput = gpioController.provisionDigitalInputPin(raspPin);
//        if (gpioPinDigitalInput.isLow()) {
//            log.info(String.format(" %s drip is already off", dripName.name()));
//            return;
//        }
//        unProvisionPin(gpioPinDigitalInput);
        final GpioPinDigitalOutput gpioPinDigitalOutput = provisionPinOutput(raspPin);
        gpioPinDigitalOutput.low();
        log.info("drip is off {}", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").format(LocalDateTime.now()));
        unProvisionPin(gpioPinDigitalOutput);
    }

    private GpioPinDigitalInput provisionPinInput(final Pin raspPin) {
        final GpioPin provisionedPin = gpioController.getProvisionedPin(raspPin);
        if (provisionedPin != null) {
            gpioController.unprovisionPin(provisionedPin);
        }
        return gpioController.provisionDigitalInputPin(raspPin);
    }

    private GpioPinDigitalOutput provisionPinOutput(final Pin raspPin) {
        final GpioPin provisionedPin = gpioController.getProvisionedPin(raspPin);
        if (provisionedPin != null) {
            gpioController.unprovisionPin(provisionedPin);
        }
        return gpioController.provisionDigitalOutputPin(raspPin);
    }

    private void unProvisionPin(final GpioPinDigitalInput pin) {
        gpioController.unprovisionPin(pin);
    }

    private void unProvisionPin(final GpioPinDigitalOutput pin) {
        gpioController.unprovisionPin(pin);
    }

    private Pin getRaspiPin(final DripName dripName) throws InvalidPinConfiguration {
        switch (dripName) {
            case FLOWER:
            case VEGETABLE:
                log.debug("Gpio pin mapping from config {}", gpioPinMapping.getDripMapping().getPinMapping().get(dripName));
                final Pin pinByName = RaspiPin.getPinByName(gpioPinMapping.getDripMapping().getPinMapping().get(dripName));
                log.debug("RaspPin found for dripName {} pinName {}", dripName.name(), pinByName.getName());
                return pinByName;
            default:
                throw new InvalidPinConfiguration(String.format("Pin configuration missing for %s", dripName.name()));
        }
    }
}
