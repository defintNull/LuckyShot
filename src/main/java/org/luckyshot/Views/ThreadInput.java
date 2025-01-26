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
            instance.start();
        }
        return instance;
    }

    private void start() {
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
        if(e.getKeyCode() != NativeKeyEvent.VC_SPACE && e.getKeyCode() != NativeKeyEvent.VC_TAB && e.getKeyCode() != 0) {
            setCursorPos(36, 2);
            System.out.println(e.getKeyCode());
            System.out.println(NativeKeyEvent.VC_BACKSPACE);
            setCursorPos(35, 3 + buffer.size());
            buffer.add(e.getKeyChar());
            System.out.print(e.getKeyChar());
        }
    }

    public void printBuffer() {
        if(buffer.contains(' ')) {
            int p = buffer.indexOf(' ');
            setCursorPos(35, 3 + p);
            int s = buffer.size();
            for(int i = p; i < s; i++) {
                System.out.print(" ");
                buffer.removeLast();
            }
            setCursorPos(35, 3 + p);
        } else {
            setCursorPos(35, 3 + xPos);
            for (int i = xPos; i < buffer.size(); i++) {
                System.out.print(buffer.get(i));
            }
        }
        xPos = buffer.size();

//        if(!buffer.contains(' ')) {
//            setCursorPos(35, 3 + xPos);
//            for (int i = xPos; i < buffer.size(); i++) {
//                System.out.print(buffer.get(i));
//            }
//        } else {
//            System.out.print("B");
//            setCursorPos(35, 3 + buffer.size());
//            for (int i = buffer.size() - 1; i < xPos; i++) {
//                System.out.print("A");
//            }
//        }
    }

    public ArrayList<Character> getBuffer() {
        return buffer;
    }

    public void close() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (Exception e) {
            systemError();
            System.exit(1);
        }
    }
}
