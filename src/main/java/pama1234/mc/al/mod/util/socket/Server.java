package pama1234.mc.al.mod.util.socket;

import static pama1234.mc.al.mod.util.socket.SendMsgThread.getTime;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Server extends Thread{
  ServerSocket server;
  Socket socket;
  ByteBuffer buffer=ByteBuffer.allocate(256);
  public Server(int port) {
    try {
      server=new ServerSocket(port);
    }catch(IOException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void run() {
    while(true) try {
      acceptClient();
    }catch(IOException e) {
      e.printStackTrace();
    }
  }
  public void acceptClient() throws IOException {
    SendMsgThread thread=null;
    try {
      System.out.println(getTime()+"等待客户端连接...");
      socket=server.accept();
      thread=new SendMsgThread(socket);
      thread.start();
      System.out.println(getTime()+Client.getIP(socket)+" 连接成功...");
      Client.startLoop(socket,buffer);
          }catch(SocketException e) {
            System.out.println(getTime()+e);
    }finally {
      if(thread!=null) {
        thread.stop=true;
                      }
      if(socket!=null) socket.close();
    }
      }
  public static void main(String[] args) {
    new Server(1234).start();
  }
}
