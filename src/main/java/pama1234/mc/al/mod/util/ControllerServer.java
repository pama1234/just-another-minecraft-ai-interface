package pama1234.mc.al.mod.util;

import static pama1234.mc.al.mod.util.socket.SendMsgThread.getTime;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import pama1234.mc.al.mod.MainMod;
import pama1234.mc.al.mod.util.socket.Client;

public class ControllerServer extends Thread{
  public ServerSocket server;
  public Socket socket;
  public ByteBuffer buffer=ByteBuffer.allocate(256);
  public ControllerProxy proxy;
  //---
  public ReadableByteChannel inChannel;
  public WritableByteChannel outChannel;
  public int length;
  public ControllerServer(int port) {
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
    try {
      System.out.println(getTime()+"等待客户端连接...");
      socket=server.accept();
      System.out.println(getTime()+Client.getIP(socket)+" 连接成功...");
      startLoop();
    }catch(SocketException e) {
      System.out.println(getTime()+e);
    }finally {
      if(socket!=null) socket.close();
    }
  }
  public boolean readFromChannel() throws IOException {
    return (length=inChannel.read(buffer))!=-1;
  }
  public void tick() {}
  public void startLoop() throws IOException {
    // System.out.println(getTime()+"启动读取线程");
    inChannel=Channels.newChannel(socket.getInputStream());
    outChannel=Channels.newChannel(socket.getOutputStream());
    outChannel.write(MainMod.screenshotBuffer);
    MainMod.updateScreenshot=true;
    while(readFromChannel()) {
      buffer.flip();
      read();
      buffer.clear();
      //---
      outChannel.write(MainMod.screenshotBuffer);
      MainMod.updateScreenshot=true;
    }
    inChannel.close();
    outChannel.close();
    // System.out.println(getTime()+"停止读取线程");
  }
  public void read() {
    proxy.updateInput(buffer);
  }
}
