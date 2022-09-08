/*
Copyright 2022 Daniele Zelante  <zeldan@zetasys.net>

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
