import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket sClient;
        DatagramPacket dpin;
        DatagramPacket dpout;
        int dim_buffer = 100;
        InetAddress ia;
        InetSocketAddress isa;
        String input;
        String fromSrv = "";
        InputStreamReader tastiera;
        boolean ack = false;
        BufferedReader br;
        byte[] buffer;

        try{
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia,50786); //modifcare in base alla porta del server
            sClient = new DatagramSocket();
            tastiera = new InputStreamReader(System.in);
            br = new BufferedReader(tastiera);
            while (true) {
                try {
                    do {
                        System.out.println("Inserisci nome utente da registrare");
                        input = br.readLine();
                        buffer = input.getBytes();
                        dpout = new DatagramPacket(buffer, buffer.length);
                        dpout.setSocketAddress(isa);
                        sClient.send(dpout);

                        buffer = new byte[dim_buffer];
                        dpin = new DatagramPacket(buffer, 0, buffer.length);
                        sClient.receive(dpin);
                        fromSrv = new String(buffer, 0, dpin.getLength());
                        System.out.println("Da server: " + fromSrv);
                        if(fromSrv.equals("Utente registrato con successo")){
                            ack = true;
                        }
                    }while(!ack);

                    //Riceve da server informazioni sul contenuto
                    buffer = new byte[dim_buffer];
                    dpin = new DatagramPacket(buffer, 0, buffer.length);
                    sClient.receive(dpin);
                    fromSrv = new String(buffer, 0, dpin.getLength());
                    System.out.println("Da server: " + fromSrv);

                    //Inserisce e manda al server il valore con cui modificare il contenuto
                    System.out.println("Inserisci il valore della modifica del contenuto da modificare:");
                    input = br.readLine();
                    buffer = input.getBytes();
                    dpout = new DatagramPacket(buffer, buffer.length);
                    dpout.setSocketAddress(isa);
                    sClient.send(dpout);

                    //Riceve da server esito finale modifica
                    buffer = new byte[dim_buffer];
                    dpin = new DatagramPacket(buffer, 0, buffer.length);
                    sClient.receive(dpin);
                    fromSrv = new String(buffer, 0, dpin.getLength());
                    System.out.println("Da server: " + fromSrv);




                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
