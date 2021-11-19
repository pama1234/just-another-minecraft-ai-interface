package pama1234.mc.al.mod.util;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;

import pama1234.mc.al.mod.MainMod;

public class ControllerProxy{
  public static final float half=1/2f;
  //---
  public float[] input=new float[16];
  // public float[] his=new float[16];
  //---
  ControllerData data=new ControllerData(),his=new ControllerData();
  //---
  public CallbackSet output;
  public ControllerProxy(CallbackSet output) {
    this.output=output;
  }
  public void updateInput(ByteBuffer in) {
    updateInput(in,0);
  }
  public void updateInput(ByteBuffer in,int pos) {
    for(int i=0;i<input.length;i++) {
      // his[i]=input[i];
      input[i]=in.getFloat(pos+i*4);
    }
    // Float.intBitsToFloat(in);
  }
  public void updateOutput() {
    his.set(data);
    compute();
    if(his.hotbar!=data.hotbar) nummberKey(data.hotbar);
    if(his.key_q!=data.key_q) output.key(GLFW.GLFW_KEY_Q,data.key_q);
    if(his.key_f!=data.key_f) output.key(GLFW.GLFW_KEY_F,data.key_f);
    if(his.move_ws!=data.move_ws) {
      if(his.move_ws!=1) output.keyReleased(getMoveWS(his.move_ws));
      if(data.move_ws!=1) output.keyPressed(getMoveWS(data.move_ws));
    }
    if(his.move_ad!=data.move_ad) {
      if(his.move_ad!=1) output.keyReleased(getMoveAD(his.move_ad));
      if(data.move_ad!=1) output.keyPressed(getMoveAD(data.move_ad));
    }
    if(his.key_shift!=data.key_shift) output.key(GLFW.GLFW_KEY_LEFT_SHIFT,data.key_shift);
    if(his.key_ctrl!=data.key_ctrl) output.key(GLFW.GLFW_KEY_LEFT_CONTROL,data.key_ctrl);
    if(his.key_space!=data.key_space) output.key(GLFW.GLFW_KEY_SPACE,data.key_space);
    if(his.key_tab!=data.key_tab) output.key(GLFW.GLFW_KEY_TAB,data.key_tab);
    if(his.ui_eltp!=data.ui_eltp) eltpUpdate(his.ui_eltp,data.ui_eltp);
  }
  public void eltpUpdate(int before,int after) {
    if(!MainMod.grabMouse) pressAndRelease(GLFW.GLFW_KEY_Q);
    switch(after) {
      case 1:
        pressAndRelease(GLFW.GLFW_KEY_E);
        break;
      case 2:
        pressAndRelease(GLFW.GLFW_KEY_L);
        break;
      case 3:
        pressAndRelease(GLFW.GLFW_KEY_T);
        break;
      case 4:
        pressAndRelease(GLFW.GLFW_KEY_P);
        break;
    }
  }
  public int getMoveWS(int in) {
    switch(in) {
      case 0:
        return GLFW.GLFW_KEY_W;
      case 2:
        return GLFW.GLFW_KEY_S;
      default:
        return -1;
    }
  }
  public int getMoveAD(int in) {
    switch(in) {
      case 0:
        return GLFW.GLFW_KEY_A;
      case 2:
        return GLFW.GLFW_KEY_D;
      default:
        return -1;
    }
  }
  public void nummberKey(int pos) {
    switch(pos) {
      case 1:
        pressAndRelease(GLFW.GLFW_KEY_1);
        break;
      case 2:
        pressAndRelease(GLFW.GLFW_KEY_2);
        break;
      case 3:
        pressAndRelease(GLFW.GLFW_KEY_3);
        break;
      case 4:
        pressAndRelease(GLFW.GLFW_KEY_4);
        break;
      case 5:
        pressAndRelease(GLFW.GLFW_KEY_5);
        break;
      case 6:
        pressAndRelease(GLFW.GLFW_KEY_6);
        break;
      case 7:
        pressAndRelease(GLFW.GLFW_KEY_7);
        break;
      case 8:
        pressAndRelease(GLFW.GLFW_KEY_8);
        break;
      case 9:
        pressAndRelease(GLFW.GLFW_KEY_9);
        break;
    }
  }
  public void pressAndRelease(int in) {
    output.keyPressed(in);
    output.keyReleased(in);
  }
  public void compute() {
    data.hotbar=(int)(input[0]*10);
    data.key_q=input[1]>half;
    data.key_f=input[2]>half;
    data.move_ws=(int)(input[3]*3);
    data.move_ad=(int)(input[4]*3);
    data.key_shift=input[5]>half;
    data.key_ctrl=input[6]>half;
    data.key_space=input[7]>half;
    data.key_tab=input[8]>half;
    data.ui_eltp=(int)(input[9]*5);
    //---
    data.mouse_dx=getMouseDis(input[10]);
    data.mouse_dy=getMouseDis(input[11]);
    data.mouse_scroll=getScrollDis(input[12]);
    data.button_left=input[13]>half;
    data.button_center=input[14]>half;
    data.button_right=input[15]>half;
  }
  public static float getScrollDis(float in) {
    return f(in,32);
  }
  public static float getMouseDis(float in) {
    return f(in,6);
  }
  public static float f(float in,float a) {
    if((int)(in*3)==1) return 0;
    in-=half;
    in*=6;
    boolean flag=in<0;
    in+=flag?1:-1;
    if(in>1||in<-1) {
      in+=flag?1:-1;
      in*=a;
    }
    return in;
  }
  // public static void main(String[] args) {
  //   for(int i=0;i<=100;i++) {
  //     float ti=i/100f;
  //     System.out.println(ti+" "+getScrollDis(ti));
  //   }
  // }
}
