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
import java.lang.System.Logger.Level;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author zeldan
 */
public class DecimalRenderer extends DefaultTableCellRenderer
{

    final static long serialVersionUID = 1L;
    private static final System.Logger LOG = System.getLogger(MethodHandles.lookup().lookupClass().getPackageName());

    final int decdigits;
    final boolean showall;
    final char decimal_sep;

    public DecimalRenderer(int decdigits)
    {
        this(decdigits, true);
    }

    public DecimalRenderer(int decdigits, boolean showall)
    {
        this.decdigits = decdigits;
        this.showall = showall;
        this.decimal_sep = ','; // TODO configurabile
    }

    public String decsepToPoint(String s)
    {
        final var st = s.trim();
        final var z = st.length();
        final var sb = new StringBuilder(z);
        for (var j = 0; j < z; ++j)
        {
            final char k = st.charAt(j);
            if (Character.isDigit(k))
                sb.append(k);
            if (j == 0 && (k == '+' || k == '-'))
                sb.append(k);
            if (k == this.decimal_sep)
                sb.append('.');
        }

        return sb.toString();
    }

    @Override
    public void setValue(Object value)
    {
        String svalue = null;
        try
        {
            if (value != null)
            {
                if (value instanceof BigDecimal)
                {
                    final var fmt = NumberFormat.getNumberInstance();
                    fmt.setGroupingUsed(true);
                    fmt.setMaximumFractionDigits(decdigits);
                    fmt.setMinimumFractionDigits(showall ? decdigits : 0);
                    fmt.setMinimumIntegerDigits(1);
                    fmt.setRoundingMode(RoundingMode.HALF_EVEN);

                    svalue = fmt.format(value);
                }

                if (value instanceof Double)
                {
                    final var fmt = NumberFormat.getNumberInstance();
                    fmt.setGroupingUsed(false);
                    fmt.setMaximumFractionDigits(decdigits);
                    fmt.setMinimumFractionDigits(decdigits);
                    fmt.setMinimumIntegerDigits(1);
                    fmt.setRoundingMode(RoundingMode.HALF_EVEN);

                    svalue = fmt.format(value);
                }

                if (value instanceof String s)
                {
                    final var n = s.isBlank() ? null : new BigDecimal(decsepToPoint(s.strip()));
                    final var fmt = NumberFormat.getNumberInstance();
                    fmt.setGroupingUsed(true);
                    fmt.setMaximumFractionDigits(decdigits);
                    fmt.setMinimumFractionDigits(showall ? decdigits : 0);
                    fmt.setMinimumIntegerDigits(1);
                    fmt.setRoundingMode(RoundingMode.HALF_EVEN);
                    svalue = fmt.format(n);
                }

            }
        } catch (IllegalArgumentException ex)
        {
            LOG.log(Level.WARNING, "setValue", ex);
        }
        //setHorizontalAlignment(JLabel.RIGHT);
        super.setValue(svalue);
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
        setHorizontalAlignment(JLabel.RIGHT);

        for (int column : columns)
        {
            final var col = table.getColumnModel().getColumn(column);
            col.setCellRenderer(this);

            Table.doNaturalOrder(table, column);
        }
        Table.justifyHeaders(table);
    }
}
