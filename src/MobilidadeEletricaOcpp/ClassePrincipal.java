package MobilidadeEletricaOcpp;

import java.util.Scanner;

/**
 *
 * @author leticiamtt
 */
public class ClassePrincipal {

    public static void main(String[] args) {

        ControleLampada lampada = new ControleLampada();

        int numero = -1;
        Scanner leitor = new Scanner(System.in);

        while (numero != 2) {
            System.out.println("Insira 1 para LIGAR ou 0 para DESLIGAR");

            numero = leitor.nextInt();

            if (numero == 1) {
                lampada.PinoAlto();
            } else if (numero == 0) {
                lampada.PinoBaixo();
            }
        }

        //Deligando GPIO
        lampada.DesligarGPIO();
    }
}
