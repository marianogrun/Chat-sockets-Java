package grunblatt.sockets;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
    }

}

class LaminaMarcoCliente extends JPanel{

    public LaminaMarcoCliente (){

        nick= new JTextField(5);

        add(nick);

        JLabel texto =new JLabel ("-CHAT-");

        add(texto);

        ip=new JTextField(8);

        add(ip);

        campoChat= new JTextArea(12,20);

        add(campoChat);

        campo1 = new JTextField(20);
        add(campo1);
        miBoton=new JButton("Enviar");

        EnviaTexto mievento=new EnviaTexto();

        miBoton.addActionListener(mievento);

        add(miBoton);

    }

    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println(campo1.getText());

            try {
                Socket clientSocket =new Socket("localhost",9000); // 192.168.0.131

                ConnexionPackage connexionPackage = new ConnexionPackage();

                connexionPackage.setNick(nick.getText());

                connexionPackage.setIp(ip.getText());

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
        }
    }

    private JTextField campo1, nick, ip;

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
