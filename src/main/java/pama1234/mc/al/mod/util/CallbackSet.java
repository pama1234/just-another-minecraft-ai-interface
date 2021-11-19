package pama1234.mc.al.mod.util;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

public class CallbackSet{
  public GLFWKeyCallbackI keyCallback;
  public GLFWCharModsCallbackI charModsCallback;
  //---
  public GLFWCursorPosCallbackI cursorPosCallback;
  public GLFWMouseButtonCallbackI mouseButtonCallback;
  public GLFWScrollCallbackI scrollCallback;
  //---
  public long window;
  public boolean shift,ctrl,alt;
  public int modeCode;
  public CallbackSet(
    GLFWKeyCallbackI keyCallback,GLFWCharModsCallbackI charModsCallback,
    GLFWCursorPosCallbackI cursorPosCallback,GLFWMouseButtonCallbackI mouseButtonCallback,
    GLFWScrollCallbackI scrollCallback) {
    this.keyCallback=keyCallback;
    this.charModsCallback=charModsCallback;
    this.cursorPosCallback=cursorPosCallback;
    this.mouseButtonCallback=mouseButtonCallback;
    this.scrollCallback=scrollCallback;
  }
  public void invoke(byte[] in,int pos) {
    switch(in[pos]) {
      case 1:
        keyPressed(in[pos+1]);
        break;
      case 2:
        keyReleased(in[pos+1]);
        break;
      case 3:
        keyTyped((char)(in[pos+1]<<4+in[pos+2]));
        break;
      case 4:
        mousePresed(in[pos+1]);
        break;
      case 5:
        mouseReleased(in[pos+1]);
        break;
      case 6:
        mouseMoved(in[pos+1],in[pos+2]);
        break;
      case 7:
        mouseScroll(in[pos+1]);
        break;
    }
  }
  public void invoke(ByteBuffer in,int pos) {
    switch(in.get(pos)) {
      case 1:
        keyPressed(in.get(pos+1));
        break;
      case 2:
        keyReleased(in.get(pos+1));
        break;
      case 3:
        keyTyped(in.getChar(pos+1));
        break;
      case 4:
        mousePresed(in.get(pos+1));
        break;
      case 5:
        mouseReleased(in.get(pos+1));
        break;
      case 6:
        mouseMoved(in.get(pos+1),in.get(pos+2));
        break;
      case 7:
        mouseScroll(in.get(pos+1));
        break;
    }
  }
  public void mousePresed(int button) {
    mouseButtonCallback.invoke(window,button,GLFW.GLFW_PRESS,modeCode);
  }
  public void mouseReleased(int button) {
    mouseButtonCallback.invoke(window,button,GLFW.GLFW_RELEASE,modeCode);
  }
  public void mouseMoved(int x,int y) {
    cursorPosCallback.invoke(window,x,y);
  }
  public void mouseScroll(int count) {
    scrollCallback.invoke(window,0,count);
  }
  public void key(int code,boolean type) {
    refreshModCode(code,type);
    keyCallback.invoke(window,code,GLFW.glfwGetKeyScancode(code),type?GLFW.GLFW_PRESS:GLFW.GLFW_RELEASE,modeCode);
  }
  public void keyPressed(int code) {
    refreshModCode(code,true);
    keyCallback.invoke(window,code,GLFW.glfwGetKeyScancode(code),GLFW.GLFW_PRESS,modeCode);
  }
  public void keyReleased(int code) {
    refreshModCode(code,false);
    keyCallback.invoke(window,code,GLFW.glfwGetKeyScancode(code),GLFW.GLFW_RELEASE,modeCode);
  }
  public void keyTyped(char code) {
    charModsCallback.invoke(window,code,modeCode);
  }
  public void refreshModCode(int code,boolean flag) {
    boolean refresh=false;
    if(code==GLFW.GLFW_KEY_LEFT_CONTROL||code==GLFW.GLFW_KEY_RIGHT_CONTROL) {
      refresh=true;
      ctrl=flag;
    }else if(code==GLFW.GLFW_KEY_LEFT_SHIFT||code==GLFW.GLFW_KEY_RIGHT_SHIFT) {
      refresh=true;
      shift=flag;
    }else if(code==GLFW.GLFW_KEY_LEFT_ALT||code==GLFW.GLFW_KEY_RIGHT_ALT) {
      refresh=true;
      alt=flag;
    }
    if(refresh) modeCode=(shift?GLFW.GLFW_MOD_SHIFT:0)+
      (ctrl?GLFW.GLFW_MOD_CONTROL:0)+
      (alt?GLFW.GLFW_MOD_ALT:0);
  }
  public static void setCallback(long window,CallbackSet in) {
    GLFW.glfwSetKeyCallback(window,in.keyCallback);
    GLFW.glfwSetCharModsCallback(window,in.charModsCallback);
    GLFW.glfwSetCursorPosCallback(window,in.cursorPosCallback);
    GLFW.glfwSetMouseButtonCallback(window,in.mouseButtonCallback);
    GLFW.glfwSetScrollCallback(window,in.scrollCallback);
  }
}
