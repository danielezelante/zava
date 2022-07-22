/*
 * YAL zeldan
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
