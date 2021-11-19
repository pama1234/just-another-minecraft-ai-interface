package pama1234.mc.al.mod;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import pama1234.mc.al.mod.util.ControllerServer;
import pama1234.mc.al.mod.util.CallbackSet;
import pama1234.mc.al.mod.util.WindowCallbackSet;

@OnlyIn(Dist.CLIENT)
@Mod(MainMod.MOD_NAME)
public class MainMod{
  public static final String MOD_NAME="examplemod";
  public static final Logger LOGGER=LogManager.getLogger();
  public static final int graphicsWidth=640,graphicsHeight=360;
  //---
  public Minecraft mc;
  public long window;
  //---
  public CallbackSet defaultCtrl,modCtrl;
  public WindowCallbackSet windowCallbackSet;
  //---
  public static ByteBuffer screenshotBuffer=ByteBuffer.allocate(graphicsWidth*graphicsHeight*3);
  public static boolean updateScreenshot;
  //---
  public Cursor cursor=new Cursor();
  public static final int[] colors=new int[] {0x7fff8080,0x7f8080ff,0x7f80ff80,0x7fffffff};
  public class Cursor extends AbstractGui{
    public LinkedList<Integer> buttonStack=new LinkedList<Integer>();
    public void render(MatrixStack stack,int mouseX,int mouseY) {
      boolean flag=buttonStack.isEmpty();
      final int color=flag?colors[3]:colors[buttonStack.getLast()];
      hLine(stack,mouseX-(flag?4:3),mouseX+(flag?4:3),mouseY,color);
      vLine(stack,mouseX,mouseY-(flag?5:4),mouseY+(flag?5:4),color);
    }
  }
  //---
  public static MainMod instance;
  public static boolean robotCtrl,grabMouse;
  public static ControllerServer ctrlServer;
  public MainMod() {
    instance=this;
    IEventBus modEventBus=FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::onCommonSetup);
    modEventBus.addListener(this::onClientSetup);
    MinecraftForge.EVENT_BUS.register(this);
    //    InputMappings::grabOrReleaseMouse
  }
  public static void grabOrReleaseMouse(long windowIn,int type,double x,double y) {
    grabMouse=type!=0;
    if(!robotCtrl) {
      GLFW.glfwSetCursorPos(windowIn,x,y);
      GLFW.glfwSetInputMode(windowIn,208897,type);
    }
    // LOGGER.info("MainMod.grabOrReleaseMouse()"+type);
  }
  public void onCommonSetup(FMLCommonSetupEvent e) {}
  public void onClientSetup(FMLClientSetupEvent e) {
    mc=e.getMinecraftSupplier().get();
    window=mc.getWindow().getWindow();
    //---
    windowCallbackSet=new WindowCallbackSet(
      GLFW.glfwSetDropCallback(window,null),
      GLFW.glfwSetWindowPosCallback(window,null),
      GLFW.glfwSetWindowSizeCallback(window,null),
      GLFW.glfwSetWindowFocusCallback(window,null),
      GLFW.glfwSetCursorEnterCallback(window,null),
      GLFW.glfwSetFramebufferSizeCallback(window,null));
    WindowCallbackSet.setCallback(window,windowCallbackSet);
    modCtrl=new CallbackSet(
      this::keyCallback,
      this::charModsCallback,
      this::cursorPosCallback,
      this::mouseButtonCallback,
      this::scrollCallback);
    defaultCtrl=new CallbackSet(
      GLFW.glfwSetKeyCallback(window,null),
      GLFW.glfwSetCharModsCallback(window,null),
      GLFW.glfwSetCursorPosCallback(window,null),
      GLFW.glfwSetMouseButtonCallback(window,null),
      GLFW.glfwSetScrollCallback(window,null));
    CallbackSet.setCallback(window,defaultCtrl);
    GLFW.glfwSetWindowFocusCallback(window,null);
    //---
    // screenshotBuffer=ByteBuffer.allocate(graphicsWidth*graphicsHeight*3);
    ctrlServer=new ControllerServer(1234);
  }
  @SubscribeEvent
  public void onServerAboutToStart(FMLServerAboutToStartEvent e) {
    replaceCallback();
  }
  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent e) {
    serverStart();
  }
  @SubscribeEvent
  public void onServerStarted(FMLServerStartedEvent e) {
    serverStart();
  }
  @SubscribeEvent
  public void onServerStopping(FMLServerStoppingEvent e) {
    serverStop();
  }
  @SubscribeEvent
  public void onServerStopped(FMLServerStoppedEvent e) {
    serverStop();
  }
  public void serverStart() {
    Framebuffer renderTarget=mc.getMainRenderTarget();
    renderTarget.width=graphicsWidth;
    renderTarget.height=graphicsHeight;
    renderTarget.viewWidth=graphicsWidth;
    renderTarget.viewHeight=graphicsHeight;
    //---
    //    MainWindow mainWindow=mc.getWindow();
    //    mainWindow.setGuiScale(1);
    //---
    GLFW.glfwSetWindowSize(window,graphicsWidth,graphicsHeight);
    GLFW.glfwSetWindowAttrib(window,GLFW.GLFW_RESIZABLE,GLFW.GLFW_FALSE);
    replaceCallback();
    windowCallbackSet.windowSizeCallback.invoke(window,graphicsWidth,graphicsHeight);
    windowCallbackSet.framebufferSizeCallback.invoke(window,graphicsWidth,graphicsHeight);
    windowCallbackSet.windowFocusCallback.invoke(window,true);
  }
  public void serverStop() {
    GLFW.glfwSetWindowAttrib(window,GLFW.GLFW_RESIZABLE,GLFW.GLFW_TRUE);
    revertCallback();
  }
  public void revertCallback() {
    robotCtrl=false;
    CallbackSet.setCallback(window,defaultCtrl);
    WindowCallbackSet.setCallback(window,windowCallbackSet);
  }
  public void replaceCallback() {
    robotCtrl=true;
    CallbackSet.setCallback(window,modCtrl);
    WindowCallbackSet.removeCallback(window);
  }
  @SubscribeEvent
  public void onPlayerTickEvent(PlayerTickEvent e) {
    updateScreenshot=true;
  }
  //  long time;
  @SubscribeEvent
  public void onRenderTickEvent(RenderTickEvent e) {
    if(e.phase!=Phase.END) return;
    if(!updateScreenshot) return;
    updateScreenshot=false;
    ctrlServer.tick();
    updateScreenshot();
    //    LOGGER.info(System.currentTimeMillis()-time);
    //    time=System.currentTimeMillis();
  }
  public void updateScreenshot() {
    RenderSystem.readPixels(0,0,graphicsWidth,graphicsHeight,GL11.GL_RGB,GL11.GL_UNSIGNED_BYTE,screenshotBuffer);
  }
  // public static ByteBuffer cloneBuffer(ByteBuffer original) {
  //   ByteBuffer clone=ByteBuffer.allocate(original.capacity());
  //   original.rewind();
  //   clone.put(original);
  //   original.rewind();
  //   return clone;
  // }
  // public void saveScreenshot() {
  //   saveScreenshot(cloneBuffer(screenshotBuffer));
  // }
  // public void saveScreenshot(ByteBuffer buf) {
  //   try {
  //     File file=new File("D:\\pama1234\\screenshot\\g"+System.currentTimeMillis()+".bytes");
  //     final boolean append=false;
  //     try(FileOutputStream fileOutputStream=new FileOutputStream(file,append)) {
  //       try(FileChannel channel=fileOutputStream.getChannel()) {
  //         buf.flip();
  //         channel.write(buf);
  //       }
  //     }
  //   }catch(IOException e) {
  //     e.printStackTrace();
  //   }
  // }
  @SubscribeEvent
  public void onPostDrawScreenEvent(DrawScreenEvent.Post e) {
    cursor.render(e.getMatrixStack(),e.getMouseX(),e.getMouseY());
  }
  //
  //------
  //
  void keyCallback(long window,int key,int scancode,int action,int mods) {
    if(window==this.window&&
      action==GLFW.GLFW_PRESS&&
      key=='M') revertCallback();
    // LOGGER.info("key "+key+" "+scancode+" "+action+" "+mods);
  }
  void charModsCallback(long window,int codepoint,int mods) {
    // LOGGER.info("char "+codepoint+" "+mods);
  }
  void cursorPosCallback(long window,double xpos,double ypos) {}
  void mouseButtonCallback(long window,int button,int action,int mods) {
    // LOGGER.info("mouse "+button+" "+action+" "+mods);
    InputMappings.grabOrReleaseMouse(0,0,0,0);
  }
  void scrollCallback(long window,double xoffset,double yoffset) {}
  //
  //------
  //
  @SubscribeEvent
  public void onKeyInputEvent(KeyInputEvent e) {
    if(e.getAction()==GLFW.GLFW_PRESS&&
      e.getKey()=='M') replaceCallback();
  }
  @SubscribeEvent
  public void onMouseInputEvent(MouseInputEvent e) {
    if(e.getAction()==GLFW.GLFW_PRESS) {
      cursor.buttonStack.addLast(e.getButton());
    }else if(e.getAction()==GLFW.GLFW_RELEASE) {
      if(cursor.buttonStack.size()>0) cursor.buttonStack.removeIf(Integer.valueOf(e.getButton())::equals);
    }
  }
}
