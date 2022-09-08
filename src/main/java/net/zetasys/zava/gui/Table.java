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

import java.util.Arrays;
import java.util.Comparator;
import javax.swing.DefaultRowSorter;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Table
{

    static public void right(JTable table, int[] ndxa)
    {
        final TableColumnModel cm = table.getColumnModel();
        final DefaultTableCellRenderer x = new DefaultTableCellRenderer();
        x.setHorizontalAlignment(JLabel.RIGHT);

        for (int n : ndxa)
            cm.getColumn(n).setCellRenderer(x);

        justifyHeaders(table);
    }

    static public void justifyHeaders(JTable table)
    {
        /*
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
            {
                final Component c2 = dummy.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (c2 instanceof JLabel)
                {
                    final JLabel lc2 = (JLabel)c2;

                    var cr = table.getColumnModel().getColumn(col).getCellRenderer();
                    if (cr instanceof DefaultTableCellRenderer)
                    {
                        var dtcr = (DefaultTableCellRenderer)cr;
                        lc2.setHorizontalAlignment(dtcr.getHorizontalAlignment());
                    } 
                }
                return c2;
            }
            private final JTable dummy = new JTable();
        });   
         */

    }

    static public void widths(JTable table, int[] w)
    {
        final TableColumnModel cm = table.getColumnModel();
        if (w.length != cm.getColumnCount())
            throw new RuntimeException("column count mismatch");

        final int tk = cm.getTotalColumnWidth();
        final int tw = Arrays.stream(w).sum();

        for (int j = 0; j < w.length; ++j)
            cm.getColumn(j).setPreferredWidth(w[j] * tk / tw);
    }

    static public void doNaturalOrder(JTable t, int k)
    {
        final var s = (DefaultRowSorter) t.getRowSorter();
        if (s != null)
            s.setComparator(k, Comparator.naturalOrder());
    }

    private Table()
    {
    }

}
