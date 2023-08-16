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
    private boolean computed_linear = false;

    private BigDecimal min_value;
    private BigDecimal max_value;

    private BigDecimal integral;

    private Real linear_m;
    private Real linear_q;
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
        this.computed_linear = false;
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

        this.max_value = BigDecimal.ZERO;
        this.min_value = BigDecimal.ZERO;

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
            if (this.max_value.compareTo(p) < 0)
                this.max_value = p;
            if (this.min_value.compareTo(p) > 0)
                this.min_value = p;

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
        compute_linear();
        return this.integral;
    }

    public Real getM()
    {
        compute_linear();
        return this.linear_m;
    }

    public Real getQ()
    {
        compute_linear();
        return this.linear_q;
    }

    void compute_linear()
    {
        if (this.computed_linear)
            return;
        compute();
        if (!is())
        {
            this.linear_m = null;
            this.linear_q = null;
            return;
        }
        final SimpleRegression sr = new SimpleRegression(true);

        final long xbase = getFirst();
        final long xlast = getLast();

        for (long x = xbase; x <= xlast; ++x)
            sr.addData(x, getsum(x).doubleValue());

        this.linear_m = Real.byVD(sr.getSlope(), sr.getSlopeStdErr(), getUnitM());
        this.linear_q = Real.byVD(sr.getIntercept(), sr.getInterceptStdErr(), getUnitY());

        this.computed_linear = true;
    }

    public BigDecimal getMin()
    {
        compute();
        return this.min_value;
    }

    public BigDecimal getMax()
    {
        compute();
        return this.max_value;
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
