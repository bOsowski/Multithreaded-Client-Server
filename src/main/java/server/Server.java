package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Student:              Bartosz Osowski
 * Student Number:       20072283
 * Date:                 26/10/2018
 * Class Description: Starting point of the server application.
 *
 * Requirements:
 *     The server only accepts requests from registered students.
 *     Invalid logins will result in an appropriate message being
 *     sent to the client and the socket is closed.
 **/
public class Server {

  public static void main(String args[]) throws SQLException {
    new Server();
  }

  Connection conn;

  public Server() throws SQLException {
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assign2?user=root&password=");
    System.out.println("Successfully connected to the database!");

    try {
      ServerSocket serverSocket = new ServerSocket(8000);
      while(true){
        Socket socket = serverSocket.accept();  //no progression past this will be made until a connection is made.
        Worker worker = new Worker(socket, conn);
        new Thread(worker).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
