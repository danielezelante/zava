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
import java.time.Instant;
import javax.money.CurrencyUnit;

/**
 *
 * @author zeldan
 */
public class TVAcc
{

    static final Unit UX = new Unit(DayMoneyDomain.INSTANCE, new int[]
    {
        1, 0
    });
    static final Unit UY = new Unit(DayMoneyDomain.INSTANCE, new int[]
    {
        0, 1
    });

    protected final CurrencyUnit curry;
    protected final Accumulator a;
    protected final Instant now;
    protected final long offset;
    protected Instant since;

    public TVAcc(final Instant now, CurrencyUnit curry)
    {
        this.curry = curry;
        this.a = new Accumulator(UX, UY);
        this.now = now;
        this.offset = now.getEpochSecond();
        this.since = null;
    }

    protected long todays(final Instant t)
    {
        return (t.getEpochSecond() - this.offset) / (24 * 3600);
    }

    public void add(final Instant t, final BigDecimal v)
    {
        this.a.add(todays(t), v);
        if (since == null || t.isBefore(since))
            since = t;
    }

    public Instant getSince()
    {
        return since;
    }

    public Real getSlope()
    {
        return a.getM();
    }

    public Real getInter()
    {
        final BigDecimal v = a.getsum(0);
        final Real q = a.getQ();
        return v != null && q != null ? Real.byVQ(v.doubleValue(), 0, UY).subtract(q) : null;
    }

    public long getFirst() // internal value x
    {
        return a.getFirst();
    }

    public long getLast()
    {
        return a.getLast();
    }

    public BigDecimal getsum(long x)
    {
        return a.getsum(x);
    }

    public CurrencyUnit getCurrency()
    {
        return this.curry;
    }

    public BigDecimal getIntegral()
    {
        return a.getIntegral();
    }

    public boolean is()
    {
        return a.is();
    }
}
