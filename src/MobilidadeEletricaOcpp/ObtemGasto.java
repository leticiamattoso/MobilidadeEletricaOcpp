package MobilidadeEletricaOcpp;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import java.io.IOException;

/**
 *
 * @author leticiamtt
 */
public class ObtemGasto {

    // SPI device
    public static SpiDevice spi = null;

    // ADC channel count
    public static short ADC_CHANNEL_COUNT = 8;  // MCP3004=4, MCP3008=8

    /**
     * Read data via SPI bus from MCP3002 chip.
     *
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public static void read() throws IOException, InterruptedException {
        for (short channel = 0; channel < ADC_CHANNEL_COUNT; channel++) {
            int conversion_value = getConversionValue(channel);
            System.out.println(String.format(" | %04d", conversion_value)); // print 4 digits with leading zeros
        }
        System.out.println(" |\r");
        Thread.sleep(250);
    }

    /**
     * Communicate to the ADC chip via SPI to get single-ended conversion value
     * for a specified channel.
     *
     * @param channel analog input channel on ADC chip
     * @return conversion value for specified analog input channel
     * @throws IOException
     */
    public static int getConversionValue(short channel) throws IOException {

        // create a data buffer and initialize a conversion request payload
        byte data[] = new byte[]{
            (byte) 0b00000001, // first byte, start bit
            (byte) (0b10000000 | (((channel & 7) << 4))), // second byte transmitted -> (SGL/DIF = 1, D2=D1=D0=0)
            (byte) 0b00000000 // third byte transmitted....don't care
        };

        // send conversion request to ADC chip via SPI channel
        byte[] result = spi.write(data);

        // calculate and return conversion value from result bytes
        int value = (result[1] << 8) & 0b1100000000; //merge data[1] & data[2] to get 10-bit result
        value |= (result[2] & 0xff);
        return value;
    }

    public static void init() throws IOException, InterruptedException {
        // This SPI example is using the Pi4J SPI interface to communicate with
        // the SPI hardware interface connected to a MCP3004/MCP3008 AtoD Chip.
        //
        // Please make sure the SPI is enabled on your Raspberry Pi via the
        // raspi-config utility under the advanced menu option.
        //
        // see this blog post for additional details on SPI and WiringPi
        // http://wiringpi.com/reference/spi-library/
        //
        // see the link below for the data sheet on the MCP3004/MCP3008 chip:
        // http://ww1.microchip.com/downloads/en/DeviceDoc/21294E.pdf        

        // create SPI object instance for SPI for communication
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
        try {
            read();
        } catch (InterruptedException ex) {
            System.out.println("error => " + ex);
        }

        Thread.sleep(1000);
    }
}
