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
import java.awt.Font;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.javamoney.moneta.format.MonetaryAmountDecimalFormatBuilder;

/**
 *
 * @author zeldan
 */
public class MoneyRenderer extends DefaultTableCellRenderer
{

    static final long serialVersionUID = 1L;
    private static final System.Logger LOG = System.getLogger(MethodHandles.lookup().lookupClass().getPackageName());
    static final HashMap<Pair<JTable, Integer>, Object> orgheaders = new HashMap<>();

    final CurrencyUnit defCurrency;
    final boolean lastistot;
    int style = 0;

    public MoneyRenderer(CurrencyUnit def, boolean lastistot)
    {
        this.defCurrency = def;
        this.lastistot = lastistot;

    }

    public MoneyRenderer(CurrencyUnit def)
    {
        this(def, false);
    }

    public void setFontStyle(int style)
    {
       this.style = style;
    }
    
    
    @Override
    public void setValue(Object value)
    {
        String svalue = null;
        try
        {
            if (value != null)
            {
                if (value instanceof MonetaryAmount money)
                {
                    final var currency = money.getCurrency();
                    final int digits = currency.getDefaultFractionDigits();
                    final String pattern
                            = (currency.equals(this.defCurrency)
                            ? ""
                            : "[" + currency.getCurrencyCode() + "] ")
                            + ((digits == 0)
                                    ? "#,##0"
                                    : "#,##0." + StringUtils.repeat("0", digits))
                            + ";"
                            + (currency.equals(this.defCurrency)
                            ? ""
                            : "[" + currency.getCurrencyCode() + "] ")
                            + ((digits == 0)
                                    ? "-#,##0"
                                    : "-#,##0." + StringUtils.repeat("0", digits));

                    final var fmt
                            = MonetaryAmountDecimalFormatBuilder.of(pattern).withCurrencyUnit(money.getCurrency()).build();

                    svalue = fmt.format(money);

                }

                if (value instanceof BigDecimal)
                {
                    final int digits = defCurrency.getDefaultFractionDigits();

                    final var fmt = DecimalFormat.getNumberInstance();
                    fmt.setGroupingUsed(true);
                    fmt.setMaximumFractionDigits(digits);
                    fmt.setMinimumFractionDigits(digits);
                    fmt.setMinimumIntegerDigits(1);
                    fmt.setRoundingMode(RoundingMode.HALF_EVEN);

                    svalue = fmt.format(value);
                }

            }
        } catch (IllegalArgumentException ex)
        {
            LOG.log(System.Logger.Level.WARNING, "setValue", ex);
        }
        //setHorizontalAlignment(JLabel.RIGHT);
        super.setValue(svalue);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        final Font f = getFont();
        int s = f.getStyle() | this.style;
             
        if (lastistot && row == table.getRowCount() - 1)
            s |= Font.BOLD;
            
        setFont(f.deriveFont(s));

        return this;
    }

    public void apply(JTable table, int[] columns)
    {
        setHorizontalAlignment(SwingConstants.RIGHT);

        for (int column : columns)
        {
            final var col = table.getColumnModel().getColumn(column);
            col.setCellRenderer(this);
            final var key = Pair.of(table, column);
            orgheaders.putIfAbsent(key, col.getHeaderValue().toString());
            final var oh = orgheaders.get(key);
            if (oh instanceof String ohs)
            {
                final var sqp = ohs.lastIndexOf(" [");
                if (sqp >= 0)
                    ohs = ohs.substring(0, sqp);
                col.setHeaderValue(String.format("%s [%s]", ohs, defCurrency.getCurrencyCode()));
            }
            if (lastistot && table.getRowSorter() != null)
                throw new IllegalStateException("cannot sort with total");
            Table.doNaturalOrder(table, column);
        }
        Table.justifyHeaders(table);
    }
}
