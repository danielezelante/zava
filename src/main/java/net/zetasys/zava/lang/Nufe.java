/*
 * YAL zeldan
 */
package net.zetasys.zava.lang;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;

/**
 *
 * @author zeldan
 */
public class Nufe
{

    public static BigDecimal negate(BigDecimal x)
    {
        return x == null ? x : x.negate();
    }
    

    static public BigDecimal invert(BigDecimal x)
    {
        return x == null ? x : BigDecimal.ONE.divide(x);
    }
    
    
    
       
    static public MonetaryAmount negate(MonetaryAmount x)
    {
        return x == null ? x : x.negate();
    }
    
    
        
    static public Money moneyOf(BigDecimal v, CurrencyUnit c)
    {
        return v == null ? null : Money.of(v, c);
    }
    
    
    static public Money moneyOfZ(BigDecimal v, CurrencyUnit c)
    {
        return Money.of(v == null ? BigDecimal.ZERO : v , c);
    }
    
    
    static public Money moneyOf(BigDecimal v, String c)
    {
        return v == null ? null : Money.of(v, c);
    }
    
    
    public static String format(Date date, DateFormat df)
    {
        return date == null ? null : df.format(date);
    }

    public static Date parse(String s, DateFormat df) throws ParseException
    {
        return s == null ? null : df.parse(s);
    }
    
    
    protected Nufe() {}
}
