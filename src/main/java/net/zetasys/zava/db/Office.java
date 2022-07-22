/*
 * YAL zeldan
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
        final var wb = s.getWorkbook();
        
        final var row0 = s.createRow(rowoffset);
        final var dcm = t.getColumnModel();
        
        final var boldFont = wb.createFont();
        boldFont.setBold(true);
        final var boldStyle = wb.createCellStyle();
        boldStyle.setFont(boldFont);
        
        final var timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat((short)0x16);
        
        final var moneyStyle = wb.createCellStyle();
        {
            final var df = wb.createDataFormat();
            moneyStyle.setDataFormat(df.getFormat("\"VAL\" #,##0.000"));
            moneyStyle.setDataFormat((short)0x08);
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
            hs.setDataFormat(defaultCurrency != null &&  kode.equals(defaultCurrency.getCurrencyCode())
                ? df.getFormat(" #,##0")
                : df.getFormat(String.format("\"%s\" #,##0", kode)));
            hs.setAlignment(HorizontalAlignment.RIGHT);
            csmap.put(kode, hs);
        }
        
        final var integerStyle = wb.createCellStyle();
        integerStyle.setDataFormat((short)0x01);
        integerStyle.setAlignment(HorizontalAlignment.RIGHT);
        
        for (int j=0; j<dcm.getColumnCount(); ++j)
        {
            final var cell = row0.createCell(j);
            cell.setCellValue(dcm.getColumn(j).getHeaderValue().toString());
            cell.setCellStyle(boldStyle);
        }
        final var dtm = t.getModel();
        for (int vr = 0; vr<t.getRowCount(); ++vr)
        {
            final int r = t.convertRowIndexToModel(vr);
            final var rowx = s.createRow(vr + 1 + rowoffset);
            for (int j=0; j<dtm.getColumnCount(); ++j)
            {
                final Object o = dtm.getValueAt(r, j);
                final var cell = rowx.createCell(j);
                
                if (o != null)
                {
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
                    }
                    else if (o instanceof Money ox)
                    {
                        cell.setCellValue(ox.getNumberStripped().doubleValue());
                        final var style = csmap.get(ox.getCurrency().getCurrencyCode());
                        cell.setCellStyle(style == null ? moneyStyle : style);
                    }
                    else
                        cell.setCellValue(o.toString()); 
                }    
            }
        }
        
        for (int j=0; j< dtm.getColumnCount(); ++j)
            s.autoSizeColumn(j);
        
        return rowoffset + 1 + dtm.getRowCount();
    }
    

    private Office() {}
    
    
  
}
