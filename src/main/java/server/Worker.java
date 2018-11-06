package server;

import com.oracle.tools.packager.IOUtils;

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
            System.out.println(socket.getOutputStream().toString());
            double circleRadius = new DataInputStream(socket.getInputStream()).readDouble();
            double circleArea = calculateCircleArea(circleRadius);
            System.out.println("Given circle radius = "+circleRadius);
            System.out.println("Calculated circle area = "+circleArea);
            new DataOutputStream(socket.getOutputStream()).writeDouble(circleArea);
        } catch (IOException e) {
            System.out.println(e + " on socket " + socket);
        }
    }

    private double calculateCircleArea(double circleRadius){
        //Area = PI * r^2
        return Math.PI * Math.pow(circleRadius, 2);
    }
}
