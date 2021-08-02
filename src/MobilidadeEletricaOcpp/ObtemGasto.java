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

    // ADC contagem de canais
    public static short ADC_CHANNEL_COUNT = 8;  // MCP3004=4, MCP3008=8

    /**
     * Leia os dados via barramento SPI do chip MCP3002.
     *
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public static void read() throws IOException, InterruptedException {
        for (short channel = 0; channel < ADC_CHANNEL_COUNT; channel++) {
            int conversion_value = getConversionValue(channel);
            System.out.println(String.format(" | %04d", conversion_value)); // imprimir 4 dígitos com zeros à esquerda
        }
        System.out.println(" |\r");
        Thread.sleep(250);
    }

    /**
     * Comunique-se com o chip ADC via SPI para obter o valor de conversão de
     * ponta única para um canal especificado.
     *
     * @param channel canal de entrada analógica no chip ADC
     * @return valor de conversão para o canal de entrada analógica especificado
     * @throws IOException
     *
     *
     */
    public static int getConversionValue(short channel) throws IOException {

        // crie um buffer de dados e inicialize uma carga útil de solicitação de conversão
        byte data[] = new byte[]{
            (byte) 0b00000001, // primeiro byte, começa o bit
            (byte) (0b10000000 | (((channel & 7) << 4))), // segundo byte transmitido -> (SGL / DIF = 1, D2 = D1 = D0 = 0)
            (byte) 0b00000000 //terceiro byte transmitido ... não importa
        };

        // enviar solicitação de conversão para chip ADC via canal SPI
        byte[] result = spi.write(data);

        // calcular e retornar o valor de conversão dos bytes de resultado
        int value = (result[1] << 8) & 0b1100000000; //mesclar dados [1] e dados [2] para obter o resultado de 10 bits
        value |= (result[2] & 0xff);
        return value;
    }

    public static void init() throws IOException, InterruptedException {
        // Este exemplo de SPI está usando a interface Pi4J SPI para se comunicar com
        // a interface de hardware SPI conectada a um chip MCP3004 / MCP3008 AtoD.
        //
        // Certifique-se de que o SPI esteja ativado em seu Raspberry Pi por meio do
        // utilitário raspi-config na opção de menu avançado.
        //
        // cria instância de objeto SPI para SPI para comunicação
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED, // velocidade spi padrão de 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); //modo spi padrão 0
        try {
            read();
        } catch (InterruptedException ex) {
            System.out.println("error => " + ex);
        }

        Thread.sleep(1000);
    }
}
