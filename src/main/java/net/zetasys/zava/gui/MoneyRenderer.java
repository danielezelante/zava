/*
 * Copyright 2018 by Daniele Zelante <zeldan@zeldan.net>.
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
import javax.swing.JLabel;
import javax.swing.JTable;
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

    final static long serialVersionUID = 1L;
    private static final System.Logger LOG = System.getLogger(MethodHandles.lookup().lookupClass().getPackageName());
    static final HashMap<Pair<JTable, Integer>, Object> orgheaders = new HashMap<>();

    final CurrencyUnit defCurrency;
    final boolean lastistot;

    public MoneyRenderer(CurrencyUnit def, boolean lastistot)
    {
        this.defCurrency = def;
        this.lastistot = lastistot;

    }

    public MoneyRenderer(CurrencyUnit def)
    {
        this(def, false);
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
        if (lastistot && row == table.getRowCount() - 1)
        {
            final Font f = getFont();
            setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        }
        return this;
    }

    public void apply(JTable table, int[] columns)
    {
        setHorizontalAlignment(JLabel.RIGHT);

        for (int column : columns)
        {
            final var col = table.getColumnModel().getColumn(column);
            col.setCellRenderer(this);
            final var key = Pair.of(table, column);
            orgheaders.putIfAbsent(key, col.getHeaderValue().toString());
            col.setHeaderValue(String.format("%s [%s]", orgheaders.get(key), defCurrency.getCurrencyCode()));
            if (lastistot && table.getRowSorter() != null)
                throw new IllegalStateException("cannot sort with total");
            Table.doNaturalOrder(table, column);
        }
        Table.justifyHeaders(table);
    }
}
