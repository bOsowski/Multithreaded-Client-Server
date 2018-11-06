package server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
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
  private Connection db;


  Worker(Socket socket, Connection connection){
    this.socket = socket;
    this.db = connection;
  }

  @Override
  public void run() {
    try {
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

      int id = dis.readInt();
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
      e.printStackTrace();
      System.out.println(" on socket " + socket);
    }
  }

  /**
   *
   * @param id user id.
   * @return boolean value of if the user has been found in the database.
   * @throws SQLException Thrown in case of SQL injection or connection issues.
   */
  private boolean isUserValid(int id) throws SQLException {
    String statement = "select count(SID) from myStudents where STUD_ID = '"+id+"';";
    ResultSet rs = db.prepareStatement(statement).executeQuery();
    rs.next();
    if(rs.getInt(1) == 1){
      System.out.println(
          "Successfully authenticated user with id '"+ id +
          "' connected on "+ socket.getInetAddress() + ":" +
          socket.getPort() + "."
      );
      return true;
    }

    System.out.println("Failed to authenticate user with id '"+ id + "'.");
    return false;
  }

  /**
   * Calculates a circle area from the given radius.
   * @param circleRadius radius of the circle.
   * @return circle area
   */
  private double calculateCircleArea(double circleRadius){
    //Area = PI * r^2
    return Math.PI * Math.pow(circleRadius, 2);
  }
}
