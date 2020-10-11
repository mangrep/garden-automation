package in.co.techm.gardenautomation;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import org.apache.http.Header;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
public class BlinkingLed {

    public static void main(String[] args) {
        try {
            /** create gpio controller */
            final GpioController gpio = GpioFactory.getInstance();

            final GpioPinDigitalOutput ledPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
            final GpioPinDigitalOutput ledPin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
            final GpioPinDigitalOutput ledPin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
            final GpioPinDigitalOutput ledPin4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
            final GpioPinDigitalOutput ledPin5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
            final GpioPinDigitalOutput ledPin6 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
            final GpioPinDigitalOutput ledPin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
            final GpioPinDigitalOutput ledPin8 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08);
            final GpioPinDigitalOutput ledPin9 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09);

            System.out.println("blink set");
            /** Blink every second */
            ledPin.blink(1, 1, TimeUnit.SECONDS);
            ledPin2.blink(1, 1, TimeUnit.SECONDS);
            ledPin3.blink(1, 1, TimeUnit.SECONDS);
            ledPin4.blink(1, 1, TimeUnit.SECONDS);
            ledPin5.blink(1, 1, TimeUnit.SECONDS);
            ledPin6.blink(1, 1, TimeUnit.SECONDS);

            ledPin8.blink(1, 1, TimeUnit.SECONDS);
            ledPin9.blink(1, 1, TimeUnit.SECONDS);

            ledPin7.high();
            Thread.sleep(10000);
            ledPin7.low();

            while (true) {
                LocalDateTime now;
                try {
                    now = getCurrentTime();
                } catch (final Exception e) {
                    System.out.println(e);
                    now = LocalDateTime.now();
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static LocalDateTime getCurrentTime() throws IOException {
        int timeout = 2000;
        LocalDateTime localDateTime = LocalDateTime.now();
        InetAddress[] addresses = InetAddress.getAllByName("www.google.com");
        for (InetAddress address : addresses) {
            if (address.isReachable(timeout)) {
                localDateTime = getTime1();
                break;
            }
        }
        return localDateTime;
    }

    private static LocalDateTime getTime1() throws IOException {
        System.out.println("get time method called");
        try (final CloseableHttpClient client = getHttpClient()) {
            final HttpGet httpGet = new HttpGet("https://www.google.com/");
            try (final CloseableHttpResponse execute = client.execute(httpGet)) {
                final Header date = execute.getFirstHeader("date");
                final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));
                return LocalDateTime.parse(date.getValue(), timeFormatter);
            }
        }
    }

    private static LocalDateTime getTime2() throws IOException {
        System.out.println("get time method called");
        try (final CloseableHttpClient client = getHttpClient()) {
            final HttpGet httpGet = new HttpGet("https://api.techm.co.in/api/v1/ifsc/CBIN0283571");
            try (final CloseableHttpResponse execute = client.execute(httpGet)) {
                final Header date = execute.getFirstHeader("date");
                final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));
                return LocalDateTime.parse(date.getValue(), timeFormatter);
            }
        }
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
    }
}
