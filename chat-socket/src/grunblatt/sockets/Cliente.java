package grunblatt.sockets;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
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
        JLabel texto =new JLabel ("CLIENTE");

        add(texto);

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
                Socket clientSocket =new Socket("localhost",9000);

                DataOutputStream stream=new DataOutputStream(clientSocket.getOutputStream());

                stream.writeUTF(campo1.getText());

                stream.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private JTextField campo1;
    private JButton miBoton;
}
