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

import java.awt.Component;
import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author zeldan
 */
public class TimeRenderer extends DefaultTableCellRenderer
{

    final static long serialVersionUID = 1;
    private static final System.Logger LOG = System.getLogger(MethodHandles.lookup().lookupClass().getPackageName());

    final DateFormat df;

    public TimeRenderer(DateFormat df)
    {
        this.df = df;
    }

    @Override
    public void setValue(Object value)
    {
        try
        {
            super.setValue(value != null ? df.format(value) : value);
        } catch (IllegalArgumentException ex)
        {
            LOG.log(System.Logger.Level.WARNING, "setValue", ex);
            super.setValue(value);
        }
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return this;
    }

    public void apply(JTable table, int[] columns)
    {
        setHorizontalAlignment(JLabel.LEFT);

        for (int column : columns)
        {
            final var col = table.getColumnModel().getColumn(column);
            col.setCellRenderer(this);
            Table.doNaturalOrder(table, column);
        }
        Table.justifyHeaders(table);
    }

    //private static final System.Logger LOG = System.getLogger(MethodHandles.lookup().lookupClass().getPackageName());
}
