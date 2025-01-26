package org.luckyshot.Views;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.ArrayList;

public class ThreadInput extends View implements NativeKeyListener {
    private ArrayList<Character> buffer = new ArrayList<Character>();
    private static ThreadInput instance;
    private int xPos = 0;
    int c = 0;

    private ThreadInput() {

    }

    public static ThreadInput getInstance() {
        if (instance == null) {
            instance = new ThreadInput();
        }
        return instance;
    }

    public void start() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            systemError();
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(instance);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if(e.getKeyCode() == NativeKeyEvent.VC_BACKSPACE) {
            if(!buffer.isEmpty()) {
                buffer.removeLast();
                setCursorPos(35, 3 + buffer.size());
                System.out.print(" ");
                setCursorPos(35, 3 + buffer.size());
            }
        }

        if(e.getKeyCode() == NativeKeyEvent.VC_ENTER) {
            close();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        if(e.getKeyChar() != '\b') {
            setCursorPos(35, 3 + buffer.size());
            buffer.add(e.getKeyChar());
            System.out.print(e.getKeyChar());
        }
    }

    public ArrayList<Character> getBuffer() {
        return buffer;
    }

    public void close() {
        try {
            GlobalScreen.unregisterNativeHook();
            GlobalScreen.removeNativeKeyListener(instance);
        } catch (Exception e) {
            systemError();
            System.exit(1);
        }
    }
}
