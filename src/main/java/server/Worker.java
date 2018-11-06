package server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Student:              Bartosz Osowski
 * Student Number:       20072283
 * Date:                 26/10/2018
 * Class Description:
 **/
public class Worker implements Runnable{

  private Socket socket;
  private InetAddress ipAddress;
  private Connection db;


  public Worker(Socket socket, Connection connection){
    this.socket = socket;
    this.db = connection;
  }

  @Override
  public void run() {
    try {
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      BufferedReader br = new BufferedReader(new InputStreamReader(dis));

      String id = br.readLine();
      boolean authenticated = isUserValid(id);
      dos.writeBoolean(authenticated);

      while(authenticated){
          System.out.println("Worker working.");
          double circleRadius = dis.readDouble();
          double circleArea = calculateCircleArea(circleRadius);
          System.out.println("Given circle radius = "+circleRadius);
          System.out.println("Calculated circle area = "+circleArea+"\n");
          dos.writeDouble(circleArea);
      }
    } catch (IOException | SQLException e) {
      System.out.println(e + " on socket " + socket);
    }
  }

  private boolean isUserValid(String id) throws SQLException {
    String statement = "select count(SID) from myStudents where STUD_ID = '"+id+"';";
    ResultSet rs = db.prepareStatement(statement).executeQuery();
    if(rs.getInt(1) == 1){
      System.out.println("Successfully authenticated user with id '"+ id + "'.");
      return true;
    }

    System.out.println("Failed to authenticate user with id '"+ id + "'.");
    return false;
  }

  private double calculateCircleArea(double circleRadius){
    //Area = PI * r^2
    return Math.PI * Math.pow(circleRadius, 2);
  }
}
