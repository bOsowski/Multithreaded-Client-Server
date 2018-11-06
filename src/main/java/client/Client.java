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


  public static void main(String args[]) {
    new Client();
  }

  private Client() {
    final int textFieldColumnCount = 30;
    int id = getIdFromUser();

    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;

    //Connect to the server and attempt to authenticate the user.
    try{
      socket = new Socket("localhost", 8000);
      dos = new DataOutputStream(socket.getOutputStream());
      dis = new DataInputStream(socket.getInputStream());

      dos.writeInt(id);
      boolean authenticationSuccess = dis.readBoolean();

      if(authenticationSuccess){
        JOptionPane.showMessageDialog(this, "You have successfully authenticated.");
      }
      else{
        JOptionPane.showMessageDialog(this, "You are not a registered student, bye.");
        return;
      }
    }catch (IOException e){
      JOptionPane.showMessageDialog(this, "Failed to communicate with the server. Exiting");
      return;
    }

    //Create the parent panel
    setLayout(new BorderLayout());
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setSize(new Dimension(500, 500));
    add(panel);

    //Create the input area
    NumberFormat format = NumberFormat.getInstance();
    NumberFormatter formatter = new NumberFormatter(format);
    formatter.setValueClass(Double.class);
    formatter.setMinimum(null);
    formatter.setMaximum(Double.MAX_VALUE);
    formatter.setAllowsInvalid(false);
    JFormattedTextField inputField = new JFormattedTextField(formatter);
    inputField.setColumns(textFieldColumnCount);
    setUpLabelledTextField(panel, "     Input: ", inputField);

    //Create the output area
    JTextArea consoleLog = new JTextArea();
    consoleLog.setEditable(false);
    consoleLog.setColumns(textFieldColumnCount);
    consoleLog.setRows(2);
    setUpLabelledTextField(panel, "Response: ", consoleLog);

    //Create a button.
    Button button = new Button("Calculate");
    panel.add(button);
    button.addActionListener(e -> {
        try{
          double radius = (Double)inputField.getValue();
          dos.writeDouble(radius);
          consoleLog.setText("Given radius = "+ radius + ".\n" + "Circle area = " + dis.readDouble()+".");
        }catch (NullPointerException nullptr){
          JOptionPane.showMessageDialog(this, "Invalid Input.");
        }
        catch (IOException e1) {
          JOptionPane.showMessageDialog(this, "Error communicating with the server.");
        }
    });

    //Typical JFrame settings.
    setTitle("Client");
    setSize(800, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  /**
   * Utility method which labels the given component.
   *
   * @param parent the parent to which the constructed panel will be added
   * @param labelText the field label visible to the left of the JComponent element
   * @param component The JComponent to label.
   */
  private void setUpLabelledTextField(JPanel parent, String labelText, JComponent component){
    JPanel panel = new JPanel();
    panel.setSize(parent.getWidth(), 10);
    JLabel inputLabel = new JLabel(labelText);
    panel.add(inputLabel);
    panel.add(component);
    parent.add(panel);
  }

  /**
   * This method will keep asking the user for a number until a valid input is received.
   * @return The user's input as integer.
   */
  private int getIdFromUser(){
    String idString;

    do{
      idString = JOptionPane.showInputDialog(this, "Please enter your student number.", null);
    }
    while(!idString.matches("[0-9]+"));

    return Integer.parseInt(idString);
  }
}
