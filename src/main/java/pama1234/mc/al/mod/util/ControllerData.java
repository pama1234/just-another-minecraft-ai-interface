package pama1234.mc.al.mod.util;

public class ControllerData {
  public boolean key_q,key_f,key_shift,key_ctrl,key_space,key_tab,button_left,button_right,button_center;
  public int hotbar,move_ws,move_ad,ui_eltp;
  public float mouse_dx,mouse_dy,mouse_scroll;
  public void set(ControllerData in) {
    hotbar=in.hotbar;
    key_q=in.key_q;
    key_f=in.key_f;
    move_ws=in.move_ws;
    move_ad=in.move_ad;
    key_shift=in.key_shift;
    key_ctrl=in.key_ctrl;
    key_space=in.key_space;
    key_tab=in.key_tab;
    ui_eltp=in.ui_eltp;
    //---
    mouse_dx=in.mouse_dx;
    mouse_dy=in.mouse_dy;
    mouse_scroll=in.mouse_scroll;
    button_left=in.button_left;
    button_center=in.button_center;
    button_right=in.button_right;
  }
}