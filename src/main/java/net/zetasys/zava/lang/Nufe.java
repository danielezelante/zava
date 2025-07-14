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

    public static BigDecimal invert(BigDecimal x)
    {
        return x == null ? x : BigDecimal.ONE.divide(x);
    }

    public static MonetaryAmount negate(MonetaryAmount x)
    {
        return x == null ? x : x.negate();
    }
    
    public static Money negate(Money x)
    {
        return x == null ? x : x.negate();
    }

    public static Money moneyOf(BigDecimal v, CurrencyUnit c)
    {
        return v == null ? null : Money.of(v, c);
    }

    public static Money moneyOfZ(BigDecimal v, CurrencyUnit c)
    {
        return Money.of(v == null ? BigDecimal.ZERO : v, c);
    }

    public static Money moneyOf(BigDecimal v, String c)
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

    protected Nufe()
    {
    }
}
