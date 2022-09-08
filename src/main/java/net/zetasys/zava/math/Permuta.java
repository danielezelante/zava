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


/**
 *
 * @author zeldan
 */
public class Permuta
{
    final int[] data;

    public Permuta(int n)
    {
        this.data = new int[n];
        for (int j=0; j<n; ++j) this.data[j] = j;
    }

    public Permuta(int[] k) 
    {
        boolean[] c = new boolean[k.length];
        for (int j = 0; j<c.length; ++j) c[j] = false;
        for (int q : k)
        {  
            if (q >= c.length) 
                throw new IllegalArgumentException(String.format("out of range: %d", q));
            if (c[q])
                throw new IllegalArgumentException(String.format("duplicated: %d", q));
            c[q] = true;
        }
        
        for (int j = 0; j<c.length; ++j) 
            if (!c[j])
                throw new IllegalArgumentException(String.format("missing: %d", j));
        
        this.data = k.clone();
    }
    
    public int[] get()
    {
        return this.data.clone();
    }
 
    public byte[] apply(final byte[] x)
    {
        if (this.data.length != x.length)
            throw new IllegalArgumentException(String.format("permuta=%d data=%d", this.data.length, x.length));
        final byte[] y = new byte[x.length];
        for (int j=0; j<x.length; ++j)
            y[this.data[j]] = x[j];
        return y;
    }
 
    public byte[] unapply(final byte[] x)
    {
        if (this.data.length != x.length)
            throw new IllegalArgumentException(String.format("permuta=%d data=%d", this.data.length, x.length));
        final byte[] y = new byte[x.length];
        for (int j=0; j<x.length; ++j)
            y[j] = x[this.data[j]];
        return y;
    }
    
}
