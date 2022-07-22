/*
 * YAL zeldan
 */
package net.zetasys.zava.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author zeldan
 */
public class Button
{
    public static void setHotKey(JButton button, String key, ActionListener listener)
    {
        setHotKey(button, KeyStroke.getKeyStroke(key),  listener);
        button.setText(String.format("%s [%s]", button.getText(), key));
    }
    
    public static void setHotKey(JButton button, KeyStroke key, ActionListener listener)
    {
        setHotKey(button, key,  new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                listener.actionPerformed(evt);
            }
        });
    }
    
    public static void setHotKey(JButton button, KeyStroke key, AbstractAction action)
    {
        final String mapKey = key.toString();
        final InputMap inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(key, mapKey);
        button.getActionMap().put(mapKey, action);
    }

    private Button()
    {
    }
}
