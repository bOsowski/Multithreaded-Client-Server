package client;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.NumberFormat;

/**
 * Student:              Bartosz Osowski
 * Student Number:       20072283
 * Date:                 26/10/2018
 * Class Description: Starting point of the client application.
 *
 * Requirements:
 *     The client enters their StudentID and submits request to the Server.
 *     The server creates a new thread for the client and validates that the
 *     Student exists in a database table.
 **/
public class Client extends JFrame {


  public static void main(String args[]){
    new Client();
  }

  private Client(){


    // Place text area on the frame
    this.setTitle("Client");
    setLayout(new BorderLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setSize(new Dimension(500, 500));
    add(panel);


    NumberFormat format = NumberFormat.getInstance();
    NumberFormatter formatter = new NumberFormatter(format);
    formatter.setValueClass(Double.class);
    formatter.setMinimum(0);
    formatter.setMaximum(Double.MAX_VALUE);
    formatter.setAllowsInvalid(false);

    JFormattedTextField inputField = new JFormattedTextField();

    panel.add(inputField);

    Button button = new Button("Calculate");
    panel.add(button);


    try {
      Socket socket = new Socket("localhost", 8000);
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      button.addActionListener(e -> {
        try {
          dos.writeDouble(Double.parseDouble(inputField.getText()));
          System.out.println(dis.readDouble());
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      });

    } catch (IOException e) {
      e.printStackTrace();
    }



    setTitle("Server");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!
  }
}
