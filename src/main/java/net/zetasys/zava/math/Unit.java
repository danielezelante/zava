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

import java.util.Objects;

public class Unit
{

    static Unit quotient(Unit a, Unit b)
    {
        if (a.d != b.d)
            throw new InvalidDomainError();
        final int[] ak = a.k;
        final int[] bk = b.k;
        final int z = ak.length;
        final int[] ck = new int[z];
        for (int j = 0; j < z; ++j)
            ck[j] = ak[j] - bk[j];
        return new Unit(a.d, ck);
    }

    static Unit product(Unit a, Unit b)
    {
        if (a.d != b.d)
            throw new InvalidDomainError();
        final int[] ak = a.k;
        final int[] bk = b.k;
        final int z = ak.length;
        final int[] ck = new int[z];
        for (int j = 0; j < z; ++j)
            ck[j] = ak[j] + bk[j];
        return new Unit(a.d, ck);
    }

    final Domain d;
    final int[] k;

    public Unit(Domain d, int[] k)
    {
        final String[] labels = d.get();

        if (labels.length != k.length)
            throw new InvalidDomainError();

        for (int j = 0; j < labels.length; ++j)
        {
            if (labels[j] == null)
                throw new InvalidDomainError();
            if (labels[j].isEmpty())
                throw new InvalidDomainError();
        }

        this.d = d;
        this.k = k.clone();

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Unit))
            return false;
        final Unit u = (Unit) o;
        return Objects.deepEquals(this.d, u.d)
                && Objects.deepEquals(this.k, u.k);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(d, k);
    }

    @Override
    public String toString()
    {
        final StringBuilder sbn = new StringBuilder();
        final StringBuilder sbd = new StringBuilder();
        final String[] labels = this.d.get();

        int sbnc = 0;
        int sbdc = 0;

        for (int j = 0; j < this.k.length; ++j)
        {
            if (this.k[j] > 0)
            {
                if (sbnc > 0)
                    sbn.append('*');
                sbn.append(labels[j]);
                if (this.k[j] > 1)
                    sbn.append(String.format("%d", this.k[j]));
                ++sbnc;
            }
            if (this.k[j] < 0)
            {
                if (sbdc < 0)
                    sbd.append('*');
                sbd.append(labels[j]);
                if (this.k[j] < -1)
                    sbd.append(String.format("%d", -this.k[j]));
                ++sbdc;
            }
        }

        final String sn = String.format(sbnc > 1 ? "(%s)" : "%s", sbn.toString());
        final String sd = String.format(sbdc > 1 ? "(%s)" : "%s", sbd.toString());

        if (sbnc > 0 && sbdc == 0)
            return sn;
        if (sbnc == 0 && sbdc > 0)
            return String.format("1/%s", sd);

        return String.format("%s/%s", sn, sd);
    }

    static public class InvalidDomainError extends Error
    {

        private static final long serialVersionUID = 1L;

        public InvalidDomainError()
        {
        }
    }
}
