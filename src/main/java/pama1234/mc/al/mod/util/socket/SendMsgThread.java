package pama1234.mc.al.mod.util.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SendMsgThread extends Thread{
  public Socket socket;
  public boolean stop;
  public Scanner scanner;
  public OutputStream out;
  public SendMsgThread(Socket socket) {
    this.socket=socket;
  }
  @Override
  public void run() {
    try {
      scanner=new Scanner(System.in);
      out=socket.getOutputStream();
      String in="";
      do {
        in=scanner.next();
        if(in.equals("q")) stop=true;
        if(stop) break;
        out.write(in.getBytes("UTF-8"));
        out.flush();
      }while(!stop);
    }
    // catch(SocketException e) {
    //   e.printStackTrace();}
    catch(IOException e) {
      e.printStackTrace();
    }finally {
      if(scanner!=null) scanner.close();
      try {
        out.close();
      }catch(IOException e) {
        e.printStackTrace();
      }
    }
  }
  public static String getTime() {
    Date date=new Date();
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss  ");
    String result=format.format(date);
    return result;
  }
}
