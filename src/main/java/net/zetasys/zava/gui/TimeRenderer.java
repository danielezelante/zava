/*
 * Copyright 2018 by Daniele Zelante <zeldan@zeldan.net>.
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
