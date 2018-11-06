package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

  public static void main(String args[]) {
    new Server();
  }

  public Server() {

    try {
      ServerSocket serverSocket = new ServerSocket(8000);
      while(true){
        Socket socket = serverSocket.accept();  //no progression past this will be made until a connection is made.
        Worker worker = new Worker(socket);
        new Thread(worker).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
