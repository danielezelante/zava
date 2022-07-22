/*
 * Copyright 2016 by zeldan.
 */
package net.zetasys.zava.gui;



import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;


public class Convert
{
    protected static final System.Logger LOG = System.getLogger(MethodHandles.lookup().lookupClass().getPackageName());
    
   

    public static String nullFormat(Date date, DateFormat df)
    {
        return date == null ? null : df.format(date);
    }

    public static Date nullParse(String s, DateFormat df) throws ParseException
    {
        return s == null ? null : df.parse(s);
    }

    protected Convert() {
    }
    
}
