package in.co.techm.gardenautomation.service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DripIrrigationService {
    private static final Logger log = LoggerFactory.getLogger(DripIrrigationService.class);
    private final GpioController gpioController;

    @Autowired
    public DripIrrigationService(GpioController gpioController) {
        this.gpioController = gpioController;
    }

    public void turnOn() throws InterruptedException {
        final GpioPinDigitalOutput ledPin7 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_07);
        ledPin7.high();
        Thread.sleep(10000);
        ledPin7.low();
    }
}
