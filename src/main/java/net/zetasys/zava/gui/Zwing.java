/*
 * YAL zeldan
 */
package net.zetasys.zava.gui;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author zeldan
 */
public class Zwing
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

    public static void showImage(JLabel label, byte[] data) throws IOException
    {
        if (data != null)
        {
            final var lw = label.getWidth();
            final var lh = label.getHeight();
            final var bim = ImageIO.read(new ByteArrayInputStream(data));
            final var xlw = lw * bim.getHeight() > lh * bim.getWidth() ? -lw : lw;
            final var xlh = lw * bim.getHeight() < lh * bim.getWidth() ? -lh : lh;
            
            label.setIcon(
                new ImageIcon(bim.getScaledInstance(xlw, xlh, BufferedImage.SCALE_SMOOTH)
                )
            );
        }
        else
            label.setIcon(null);
        
    }
    
    
    public static void autoNatural(final JTextField g)
    {
        g.setHorizontalAlignment(JTextField.RIGHT);
        SimpleDocumentListener.add(g, e ->
        {
            autoNaturalListener(g);
        });
    }

    public static void autoInteger(final JTextField g)
    {
        g.setHorizontalAlignment(JTextField.RIGHT);
        SimpleDocumentListener.add(g, e ->
        {
            autoIntegerListener(g);
        });
    }

    private static void autoNaturalListener(final JTextField g)
    {
        final String x = g.getText();
        final int xz = x.length();
        final StringBuilder xb = new StringBuilder(xz);
        for (int xn = 0; xn < xz; ++xn)
        {
            final char xk = x.charAt(xn);
            if (xk >= '0' && xk <= '9')
                xb.append(xk);
        }
        final String y = xb.toString();
        if (!y.equals(x))
            java.awt.EventQueue.invokeLater(() ->
            {
                g.setText(y);
            });
    }

    private static void autoIntegerListener(final JTextField g)
    {
        final String x = g.getText();
        final int xz = x.length();
        final StringBuilder xb = new StringBuilder(xz);
        for (int xn = 0; xn < xz; ++xn)
        {
            final char xk = x.charAt(xn);
            if (xk >= '0' && xk <= '9')
                xb.append(xk);
            if (xn == 0 && (xk == '+' || xk == '-'))
                xb.append(xk);

        }
        final String y = xb.toString();
        if (!y.equals(x))
            java.awt.EventQueue.invokeLater(() ->
            {
                g.setText(y);
            });
    }

    
    
    private Zwing()
    {
    }
    
}
