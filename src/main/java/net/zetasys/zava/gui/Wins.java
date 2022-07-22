/*
 * YAL zeldan
 */
package net.zetasys.zava.gui;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 *
 * @author zeldan
 */
public class Wins
{
    public static void escapeClose(JFrame w)
    {
        w.getRootPane().registerKeyboardAction(
            e -> w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING)),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    public static void escapeClose(JDialog w)
    {
        w.getRootPane().registerKeyboardAction(
            e -> w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING)),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private Wins()
    {
    }
    
}
