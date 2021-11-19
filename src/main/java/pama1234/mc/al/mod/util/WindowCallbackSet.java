package pama1234.mc.al.mod.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWDropCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowPosCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

public class WindowCallbackSet{
  public GLFWDropCallbackI dropCallback;
  //---
  public GLFWWindowPosCallbackI windowPosCallback;
  public GLFWWindowSizeCallbackI windowSizeCallback;
  public GLFWWindowFocusCallbackI windowFocusCallback;
  public GLFWCursorEnterCallbackI cursorEnterCallback;
  //---
  public GLFWFramebufferSizeCallbackI framebufferSizeCallback;
  public WindowCallbackSet(GLFWDropCallbackI dropCallback,GLFWWindowPosCallbackI windowPosCallback,GLFWWindowSizeCallbackI windowSizeCallback,GLFWWindowFocusCallbackI windowFocusCallback,GLFWCursorEnterCallbackI cursorEnterCallback,GLFWFramebufferSizeCallbackI framebufferSizeCallback) {
    this.dropCallback=dropCallback;
    //---
    this.windowPosCallback=windowPosCallback;
    this.windowSizeCallback=windowSizeCallback;
    this.windowFocusCallback=windowFocusCallback;
    this.cursorEnterCallback=cursorEnterCallback;
    //---
    this.framebufferSizeCallback=framebufferSizeCallback;
  }
  public static void setCallback(long window,WindowCallbackSet in) {
    GLFW.glfwSetDropCallback(window,in.dropCallback);
    //---
    GLFW.glfwSetWindowPosCallback(window,in.windowPosCallback);
    GLFW.glfwSetWindowSizeCallback(window,in.windowSizeCallback);
    GLFW.glfwSetWindowFocusCallback(window,in.windowFocusCallback);
    GLFW.glfwSetCursorEnterCallback(window,in.cursorEnterCallback);
    //---
    GLFW.glfwSetFramebufferSizeCallback(window,in.framebufferSizeCallback);
  }
  public static void removeCallback(long window) {
    GLFW.glfwSetDropCallback(window,null);
    //---
    GLFW.glfwSetWindowPosCallback(window,null);
    GLFW.glfwSetWindowSizeCallback(window,null);
    GLFW.glfwSetWindowFocusCallback(window,null);
    GLFW.glfwSetCursorEnterCallback(window,null);
    //---
    GLFW.glfwSetFramebufferSizeCallback(window,null);
  }
}
