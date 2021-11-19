package pama1234.mc.al.mod.util.socket;

import static pama1234.mc.al.mod.util.socket.SendMsgThread.getTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Client extends Thread{
    Socket socket;
  ByteBuffer buffer=ByteBuffer.allocate(256);
  public Client(String host,int port) {
    try {
      socket=new Socket(host,port);
    }catch(UnknownHostException e) {
      e.printStackTrace();
    }catch(IOException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void run() {
    if(socket==null) return;
    try {
      acceptServer();
    }catch(IOException e) {
      e.printStackTrace();
    }
  }
  public void acceptServer() throws IOException {
    SendMsgThread thread=new SendMsgThread(socket);
    thread.start();
    try {
      startLoop(socket,buffer);
    }catch(SocketException e) {
            System.out.println(getTime()+e);
    }finally {
            thread.interrupt();
      socket.close();
    }
  }
  public static void startLoop(Socket socket,ByteBuffer buffer) throws IOException {
    ReadableByteChannel channel=Channels.newChannel(socket.getInputStream());
    System.out.println(getTime()+"启动读取线程");
    InputStream stream=socket.getInputStream();
    int len=0;
    while((len=channel.read(buffer))!=-1) {
      buffer.flip();
      System.out.println(getTime()+getIP(socket)+" -> "+new String(buffer.array(),0,len,"UTF-8"));
      buffer.clear();
    }
    stream.close();
    System.out.println(getTime()+"停止读取线程");
  }
  public static String getIP(Socket socket) {
    return socket.getInetAddress()+":"+socket.getPort();
  }
  public static void main(String[] args) {
    new Client("127.0.0.1",1234).start();
  }
}