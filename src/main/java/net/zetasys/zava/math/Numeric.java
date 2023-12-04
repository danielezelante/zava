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
package net.zetasys.zava.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;

/**
 *
 * @author zeldan
 */
public class Numeric
{

    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal THOUSAND = new BigDecimal(1000);

    public static BigDecimal computeVAT(BigDecimal val, BigDecimal vatpc, int decimaldigits)
    {
        return val.multiply(vatpc).setScale(decimaldigits).divide(vatpc.add(HUNDRED), RoundingMode.HALF_EVEN);
    }

    public static MonetaryAmount computeVAT(MonetaryAmount val, BigDecimal vatpc)
    {
        return val.multiply(vatpc).divide(vatpc.add(HUNDRED));
    }

    public static MonetaryAmount max(MonetaryAmount x, MonetaryAmount y)
    {
        return x.isGreaterThanOrEqualTo(y) ? x : y;
    }

    public static MonetaryAmount min(MonetaryAmount x, MonetaryAmount y)
    {
        return x.isLessThanOrEqualTo(y) ? x : y;
    }

    public static Money max(Money x, Money y)
    {
        return x.isGreaterThanOrEqualTo(y) ? x : y;
    }

    public static Money min(Money x, Money y)
    {
        return x.isLessThanOrEqualTo(y) ? x : y;
    }

    public static int mm2pt(int x)
    {
        return x * 720 / 254;
    }

    public static  int mm2pt(double x)
    {
        return (int) (x * 720.0 / 254.0 + 0.5);
    }

    private Numeric()
    {
    }
}
