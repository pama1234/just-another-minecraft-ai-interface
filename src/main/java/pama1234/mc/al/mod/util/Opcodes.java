package pama1234.mc.al.mod.util;

public interface Opcodes{
  public static final byte nop=0,
    keyDown=1,
    keyUp=2,
    charTyped=3,
    mouseDown=4,
    mouseUp=5,
    mouseMove=6,
    mouseScroll=7;
  public interface MouseCode{
    public static final byte button_left=0,button_right=1,button_center=2;
  }
  public interface KeyCode{
    public static final byte key_tab=0,
      num_1=1,
      num_2=2,
      num_3=3,
      num_4=4,
      num_5=5,
      num_6=6,
      num_7=7,
      num_8=8,
      num_9=9,
      key_Q=10,
      key_E=11,
      key_T=12,
      key_P=13,
      key_W=14,
      key_S=15,
      key_A=16,
      key_D=17,
      key_F=18,
      key_L=19,
      key_shift=20,
      key_ctrl=21,
      key_space=22;
    public static final byte key_esc=23,
      key_f1=24,
      key_f2=25,
      key_f3=26,
      key_f4=27,
      key_f5=28,
      key_f6=29,
      key_f7=30,
      key_f11=31,
      key_X=32,
      key_C=33,
      key_slash=34;
  }
}
