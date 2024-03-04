/*
Copyright 2022-2024 Daniele Zelante  <zeldan@zetasys.net>

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

package net.zetasys.zava.db;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.money.CurrencyUnit;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.javamoney.moneta.Money;

/**
 *
 * @author zeldan
 */
public class Office
{

    public static int exportTableX(JTable t, XSSFSheet s, int rowoffset, CurrencyUnit defaultCurrency)
    {
        final var dcm = t.getColumnModel();
        final int[] columns = new int[dcm.getColumnCount()];
        for (int j = 0; j < columns.length; ++j)
            columns[j] = j;
        return exportTableX(t, columns, s, rowoffset, defaultCurrency);
    }

    public static int exportTableX(JTable t, int[] columns, XSSFSheet s, int rowoffset, CurrencyUnit defaultCurrency)
    {
        final var wb = s.getWorkbook();

        final var row0 = s.createRow(rowoffset);
        final var dcm = t.getColumnModel();

        final var boldFont = wb.createFont();
        boldFont.setBold(true);
        final var boldStyle = wb.createCellStyle();
        boldStyle.setFont(boldFont);

        final var timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat((short) 0x16);

        final var moneyStyle = wb.createCellStyle();
        {
            final var df = wb.createDataFormat();
            moneyStyle.setDataFormat(df.getFormat("\"VAL\" #,##0.000"));
            moneyStyle.setDataFormat((short) 0x08);
            moneyStyle.setAlignment(HorizontalAlignment.RIGHT);
        }

        final var csmap = new HashMap<String, CellStyle>();
        {
            final XSSFCellStyle hs = wb.createCellStyle();
            final var df = wb.createDataFormat();
            final var kode = "USD";
            hs.setDataFormat(defaultCurrency != null && kode.equals(defaultCurrency.getCurrencyCode())
                    ? df.getFormat("#,##0.00")
                    : df.getFormat(String.format("\"%s\" #,##0.00", kode)));
            hs.setAlignment(HorizontalAlignment.RIGHT);
            csmap.put(kode, hs);
        }
        {
            final XSSFCellStyle hs = wb.createCellStyle();
            final var df = wb.createDataFormat();
            final var kode = "PYG";
            hs.setDataFormat(defaultCurrency != null && kode.equals(defaultCurrency.getCurrencyCode())
                    ? df.getFormat(" #,##0")
                    : df.getFormat(String.format("\"%s\" #,##0", kode)));
            hs.setAlignment(HorizontalAlignment.RIGHT);
            csmap.put(kode, hs);
        }

        final var integerStyle = wb.createCellStyle();
        integerStyle.setDataFormat((short) 0x01);
        integerStyle.setAlignment(HorizontalAlignment.RIGHT);

        for (int j = 0; j < columns.length; ++j)
        {
            final var colndx = columns[j];
            if (colndx < 0 || colndx >= dcm.getColumnCount())
                throw new IllegalArgumentException("column out of range");
            final var cell = row0.createCell(j);
            cell.setCellValue(dcm.getColumn(colndx).getHeaderValue().toString());
            cell.setCellStyle(boldStyle);
        }

        final var dtm = t.getModel();
        for (int vr = 0; vr < t.getRowCount(); ++vr)
        {
            final int r = t.convertRowIndexToModel(vr);
            final var rowx = s.createRow(vr + 1 + rowoffset);

            for (int j = 0; j < columns.length; ++j)
            {
                final var colndx = columns[j];
                if (colndx < 0 || colndx >= dcm.getColumnCount())
                    throw new IllegalArgumentException("column out of range");
                final Object o = dtm.getValueAt(r, columns[j]);
                final var cell = rowx.createCell(j);

                if (o != null)
                    if (o instanceof Timestamp ot)
                    {
                        cell.setCellValue(ot.toLocalDateTime());
                        cell.setCellStyle(timeStyle);
                    } 
                    else if (o instanceof BigDecimal ot)
                    {
                        cell.setCellValue(ot.doubleValue());
                    }
                    else if (o instanceof Double ot)
                    {
                        cell.setCellValue(ot);
                    }
                    else if (o instanceof Long ot)
                    {
                        cell.setCellValue(ot);
                        cell.setCellStyle(integerStyle);
                    } else if (o instanceof Money ox)
                    {
                        cell.setCellValue(ox.getNumberStripped().doubleValue());
                        final var style = csmap.get(ox.getCurrency().getCurrencyCode());
                        cell.setCellStyle(style == null ? moneyStyle : style);
                    } else
                        cell.setCellValue(o.toString());
            }
        }

        for (int j = 0; j < dtm.getColumnCount(); ++j)
            s.autoSizeColumn(j);

        return rowoffset + 1 + dtm.getRowCount();
    }

    private Office()
    {
    }

}
