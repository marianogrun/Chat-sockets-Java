package grunblatt.sockets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

        public static void main(String[] args){

            MarcoServidor mimarco= new MarcoServidor();

            mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }
}

class MarcoServidor extends JFrame implements Runnable {
    private JTextArea areatexto;

    public MarcoServidor(){

        setBounds(1200,300,280,350);
        JPanel milamina = new JPanel();

        milamina.setLayout(new BorderLayout());

        areatexto= new JTextArea();

        milamina.add(areatexto,BorderLayout.CENTER);

        add(milamina);

        setVisible(true);

        Thread mihilo=new Thread(this);

        mihilo.start();
    }

    @Override
    public void run() {
        //System.out.println("Estoy a la escucha");

        try {
            ServerSocket server= new ServerSocket(9000);

            while(true) {
                Socket socket = server.accept();

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                String mensajeTexto = inputStream.readUTF();
                areatexto.append("\n" + mensajeTexto);

                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

