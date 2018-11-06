package server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.spi.ToolProvider;

/**
 * Student:              Bartosz Osowski
 * Student Number:       20072283
 * Date:                 26/10/2018
 * Class Description:
 **/
public class Worker implements Runnable{

    private Socket socket;
    private InetAddress ipAddress;


    public Worker(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            while(true){
                System.out.println("Worker working.");
                double circleRadius = dis.readDouble();
                double circleArea = calculateCircleArea(circleRadius);
                System.out.println("Given circle radius = "+circleRadius);
                System.out.println("Calculated circle area = "+circleArea+"\n");
                dos.writeDouble(circleArea);
            }
        } catch (IOException e) {
            System.out.println(e + " on socket " + socket);
        }
    }

    private double calculateCircleArea(double circleRadius){
        //Area = PI * r^2
        return Math.PI * Math.pow(circleRadius, 2);
    }
}
