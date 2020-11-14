package MobilidadeEletricaOcpp;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author leticiamtt
 */
public class ControleLampada {

    public GpioController gpio = GpioFactory.getInstance();
    public GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "LAMP");

    public ControleLampada() {
        System.out.println("<--Pi4J--> *INICIANDO GPIO*.");
    }

    public void PinoAlto() {
        // Definir o estado de deligamento para este pino
        this.pin.setShutdownOptions(true, PinState.HIGH);
        this.pin.high();
        System.out.println("--> GPIO estado deve estar: LIGADO");
    }

    public void PinoBaixo() {
        // Definir o estado de ligamento para este pino
        this.pin.setShutdownOptions(true, PinState.LOW);
        this.pin.low();
        System.out.println("--> GPIO estado deve estar: DESLIGADO");
    }

    public void DesligarGPIO() {
        this.gpio.shutdown();
        System.out.println("Deligando GPIO e todos seus pinos....");
    }
}
