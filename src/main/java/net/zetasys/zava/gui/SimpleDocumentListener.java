// YAL zeldan

package net.zetasys.zava.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;


@FunctionalInterface
public interface SimpleDocumentListener extends DocumentListener
{
    void update(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        update(e);
    }
    @Override
    default void removeUpdate(DocumentEvent e) {
        update(e);
    }
    @Override
    default void changedUpdate(DocumentEvent e) {
        update(e);
    }

    static void add(final JTextComponent tc, SimpleDocumentListener sdl)
    {
        tc.getDocument().addDocumentListener(sdl);  
    }
    
}