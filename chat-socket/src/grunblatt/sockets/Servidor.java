package grunblatt.sockets;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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
            ServerSocket server= new ServerSocket(9999);

            String nick;
            String ip;
            String mensaje;

            ConnexionPackage receive_package;


            while(true) {
                Socket socket = server.accept();

                ObjectInputStream data_package= new ObjectInputStream(socket.getInputStream());

                receive_package = (ConnexionPackage) data_package.readObject();

                nick= receive_package.getNick();

                ip=receive_package.getIp();

                mensaje=receive_package.getMensaje();


//                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//
//                String mensajeTexto = inputStream.readUTF();
//                areatexto.append("\n" + mensajeTexto);

                if(!mensaje.equals(" online")) {

                    areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);

                    Socket sendMessage = new Socket(ip, 9090);

                    ObjectOutputStream sendPackage = new ObjectOutputStream(sendMessage.getOutputStream());

                    sendPackage.writeObject(receive_package);

                    sendPackage.close();

                    sendMessage.close();

                    socket.close();
                }else {

                    //Search for users online

                    InetAddress location = socket.getInetAddress();

                    String remoteIp = location.getHostAddress();

                    System.out.println("Online: " + remoteIp);

                    //finished
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}

