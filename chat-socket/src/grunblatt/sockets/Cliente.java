package grunblatt.sockets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;

public class Cliente {

    public static void main(String[] args){

        MarcoCliente mimarco = new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class MarcoCliente extends JFrame {

    public MarcoCliente(){
        setBounds(600,300,280,350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);

        addWindowListener(new SendOnline());
    }

}

// ----- Online signal ------//
class SendOnline extends WindowAdapter {

    public void windowOpened(WindowEvent e){

        try{

            Socket socket= new Socket("localhost",9999);

            ConnexionPackage connexionPackage= new ConnexionPackage();

            connexionPackage.setMensaje(" online");

            ObjectOutputStream sendPackage= new ObjectOutputStream(socket.getOutputStream());

            sendPackage.writeObject(connexionPackage);

            sendPackage.close();

            socket.close();

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}

class LaminaMarcoCliente extends JPanel implements Runnable{

    public LaminaMarcoCliente (){

        String nick_user = JOptionPane.showInputDialog("Nick: ");

        JLabel n_nick= new JLabel("Nick: ");

        add(n_nick);

        nick= new JLabel();

        nick.setText(nick_user);

        add(nick);

        JLabel texto =new JLabel ("Online: ");

        add(texto);

        ip=new JComboBox();

//        ip.addItem("Usuario 1");
//
//        ip.addItem("Usuario 2");
//
//        ip.addItem("Usuario 3");

        ip.addItem("localhost");

        ip.addItem("192.168.0.17");

        add(ip);

        campoChat= new JTextArea(12,20);

        add(campoChat);

        campo1 = new JTextField(20);
        add(campo1);
        miBoton=new JButton("Enviar");

        EnviaTexto mievento=new EnviaTexto();

        miBoton.addActionListener(mievento);

        add(miBoton);

        Thread mihilo=new Thread(this);

        mihilo.start();

    }

    @Override
    public void run() {
        //System.out.println("Estoy a la escucha");

        try {
            ServerSocket serverClient= new ServerSocket(9091);

            String nick;
            String ip;
            String mensaje;

            ConnexionPackage receive_package;


            while(true) {
                Socket socket = serverClient.accept();

                ObjectInputStream data_package= new ObjectInputStream(socket.getInputStream());

                receive_package = (ConnexionPackage) data_package.readObject();

                nick= receive_package.getNick();

                ip=receive_package.getIp();

                mensaje=receive_package.getMensaje();


//                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//
//                String mensajeTexto = inputStream.readUTF();
//                areatexto.append("\n" + mensajeTexto);

                campoChat.append("\n"+ nick + ": "+ mensaje);

//                Socket sendMessage = new Socket(ip, 9090);
//
//                ObjectOutputStream sendPackage = new ObjectOutputStream(sendMessage.getOutputStream());
//
//                sendPackage.writeObject(receive_package);
//
//                sendMessage.close();

                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println(campo1.getText());

            campoChat.append("\n" + campo1.getText());

            try {
                Socket clientSocket =new Socket("localhost",9999); // 192.168.0.131

                ConnexionPackage connexionPackage = new ConnexionPackage();

                connexionPackage.setNick(nick.getText());

                connexionPackage.setIp(ip.getSelectedItem().toString());

                connexionPackage.setMensaje(campo1.getText());
                

                ObjectOutputStream data_package = new ObjectOutputStream(clientSocket.getOutputStream());

                data_package.writeObject(connexionPackage);

                clientSocket.close();
                

//                DataOutputStream stream=new DataOutputStream(clientSocket.getOutputStream());
//
//                stream.writeUTF(campo1.getText());
//
//                stream.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            campo1.setText("");
        }
    }

    private JTextField campo1;//, ip;

    private JComboBox ip;

    private JLabel nick;

    private JTextArea campoChat;

    private JButton miBoton;
}

class ConnexionPackage implements Serializable {

    private String nick;
    private String ip;
    private String mensaje;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
