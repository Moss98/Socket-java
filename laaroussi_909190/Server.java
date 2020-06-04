import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Server {
    public static void main(String[] args){
        DatagramSocket sSrv;
        DatagramPacket dpout;
        DatagramPacket dpin;
        String nomeCl = "";
        String msgToCl = "";
        String fromCl = "";
        boolean controllo = false;
        boolean registrato = false;
        Utenti contenuto = new Utenti();
        int dim_buffer = 100; //DA MODIFICARE
        byte[] buffer;
        SocketAddress client = null;
        String[] nomiUtenti = {"A","B","C","D","E","F"};
        int pos = 0;

        try {
            sSrv = new DatagramSocket();
            System.out.println("Indirizzo server: " + sSrv.getLocalAddress() +
                    "; Porta: " + sSrv.getLocalPort());
            while(true){
                buffer = new byte[dim_buffer];
                do {
                    try {
                        dpin = new DatagramPacket(buffer, 0, dim_buffer);
                        dpout = new DatagramPacket(buffer, 0, dim_buffer);
                        sSrv.receive(dpin);
                        client = dpin.getSocketAddress();
                        nomeCl = new String(buffer, 0, dpin.getLength());
                        System.out.println("Nome client ricevuto: " + nomeCl);
                        if (Arrays.asList(nomiUtenti).contains(nomeCl)) {
                            for (int i = 0; i < 6; i++) {
                                if (nomiUtenti[i].equals(nomeCl)) {
                                    nomiUtenti[i] = "-";
                                    pos = i;
                                    contenuto.setStato("In modifica");
                                }
                            }
                            msgToCl = "Utente registrato con successo";
                            controllo = true;
                            registrato = true;
                        } else {
                            msgToCl = "Utente gia esistente o non permesso <A-F>";
                            controllo = false;
                        }
                        buffer = msgToCl.getBytes();
                        dpout = new DatagramPacket(buffer, 0, buffer.length);
                        dpout.setSocketAddress(client);
                        sSrv.send(dpout);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (!controllo);

                if (registrato) {
                    String cont = contenuto.getContenuto();
                    try {
                    if (cont.equals("")) {
                        msgToCl = "NIL";
                    } else {
                        String ultimoUser = contenuto.getUltimoUser();
                        msgToCl = "Il valore corrente è: " + cont + "modificato da: "
                                    + ultimoUser + ".";
                    }

                    buffer = msgToCl.getBytes();
                    dpout = new DatagramPacket(buffer, buffer.length);
                    dpout.setSocketAddress(client);
                    sSrv.send(dpout);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        buffer = new byte[dim_buffer];
                        dpin = new DatagramPacket(buffer, 0, dim_buffer);
                        sSrv.receive(dpin);
                        fromCl = new String(buffer, 0, dpin.getLength());
                        System.out.println("Modifico l'utente: " + nomeCl + " con il valore" +
                                fromCl);
                        contenuto.setStato("modificato");
                        contenuto.setContenuto(fromCl);
                        contenuto.setUltimoUser(nomeCl);

                        msgToCl = "Contenuto modificato con successo";
                        buffer = msgToCl.getBytes();
                        dpout = new DatagramPacket(buffer, buffer.length);
                        dpout.setSocketAddress(client);
                        sSrv.send(dpout);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            } catch (SocketException e) {
                e.printStackTrace();
            }

    }
}
