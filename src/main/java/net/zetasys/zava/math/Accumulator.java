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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class Accumulator
{

    final TreeMap<Long, BigDecimal> data = new TreeMap<>();
    private TreeMap<Long, BigDecimal> sums;

    private boolean computed = false;
    private boolean computedLinear = false;

    private BigDecimal minValue;
    private BigDecimal maxValue;

    private BigDecimal integral;

    private Real linearM;
    private Real linearQ;
    final Unit ux;
    final Unit uy;

    public Accumulator(Unit ux, Unit uy)
    {
        this.ux = ux;
        this.uy = uy;
    }

    void invalidate()
    {
        this.computed = false;
        this.computedLinear = false;
    }

    public void add(long x, BigDecimal value)
    {
        invalidate();
        final BigDecimal y = data.get(x);
        data.put(x, y == null ? value : value.add(y));
    }

    public BigDecimal getsum(long x)
    {
        compute();
        if (this.sums == null)
            return null;
        final Entry<Long, BigDecimal> e = this.sums.floorEntry(x);
        return e != null ? e.getValue() : BigDecimal.ZERO;
    }

    protected void compute()
    {
        if (this.computed)
            return;

        this.integral = BigDecimal.ZERO;

        this.sums = new TreeMap<>();
        if (this.data.isEmpty())
        {
            this.computed = true;
            return;
        }

        this.maxValue = BigDecimal.ZERO;
        this.minValue = BigDecimal.ZERO;

        BigDecimal p = BigDecimal.ZERO;

        Long x0 = null;

        for (Map.Entry<Long, BigDecimal> e : this.data.entrySet())
        {
            final long x1 = e.getKey();
            if (x0 != null)
                integral = integral.add(p.multiply(BigDecimal.valueOf(x1 - x0)));
            x0 = x1;

            p = p.add(e.getValue());
            this.sums.put(e.getKey(), p);
            if (this.maxValue.compareTo(p) < 0)
                this.maxValue = p;
            if (this.minValue.compareTo(p) > 0)
                this.minValue = p;

        }

        this.computed = true;

    }

    public long getFirst()
    {
        return this.data.firstKey();
    }

    public long getLast()
    {
        return this.data.lastKey();
    }

    public BigDecimal getIntegral()
    {
        computeLinear();
        return this.integral;
    }

    public Real getM()
    {
        computeLinear();
        return this.linearM;
    }

    public Real getQ()
    {
        computeLinear();
        return this.linearQ;
    }

    void computeLinear()
    {
        if (this.computedLinear)
            return;
        compute();
        if (!is())
        {
            this.linearM = null;
            this.linearQ = null;
            return;
        }
        final SimpleRegression sr = new SimpleRegression(true);

        final long xbase = getFirst();
        final long xlast = getLast();

        for (long x = xbase; x <= xlast; ++x)
            sr.addData(x, getsum(x).doubleValue());

        this.linearM = Real.byVD(sr.getSlope(), sr.getSlopeStdErr(), getUnitM());
        this.linearQ = Real.byVD(sr.getIntercept(), sr.getInterceptStdErr(), getUnitY());

        this.computedLinear = true;
    }

    public BigDecimal getMin()
    {
        compute();
        return this.minValue;
    }

    public BigDecimal getMax()
    {
        compute();
        return this.maxValue;
    }

    public Unit getUnitX()
    {
        return this.ux;
    }

    public Unit getUnitY()
    {
        return this.uy;
    }

    public Unit getUnitM()
    {
        return Unit.quotient(getUnitY(), getUnitX());
    }

    public boolean is()
    {
        return !this.data.isEmpty();

    }

}
