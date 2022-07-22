/*
 * Copyright 2016 by zeldan.
 */
package net.zetasys.zava.math;

import java.text.DecimalFormat;


public class Real
{

	public static Real byVQ(double v, double q, Unit u)
	{
		return new Real(v, q, u);
	}
	public static Real byVD(double v, double d, Unit u)
	{
		return new Real(v, d*d, u);
	}
    final protected double v;
    final protected double q;
    final protected Unit u;
    
    protected Real(double v, double q, Unit u)
    {
        this.v = v;
        this.q = q;
        this.u = u;
    }
    
    
    
    public double getV() {return this.v;}
    public double getD() {return Math.sqrt(this.q);}
    public Unit getU() {return this.u;}
    
    @Override
    public String toString()
    {
        return String.format("%f ~%f %s", getV(),getD(), getU().toString());
    }
    
    public String format(final DecimalFormat nf)
    {
       return String.format("%s ~%s %s", nf.format(getV()), nf.format(getD()), getU().toString()); 
    }
    
    public Real negate()
    {
        return new Real(-this.v, this.q, this.u);
    }
    
    public Real add(final Real x) 
    {
        if (!this.u.equals(x.u)) throw new Unit.InvalidDomainError();
        return new Real(this.v + x.v, this.q + x.q, this.u);
    }
    
    public Real subtract(final Real x) 
    {
        return this.add(x.negate());
    }
    
    public Real multiply(double k)
    {
        return new Real(this.v * k, this.q * (k*k), this.u);
    }
    
    public Real multiply(final Real a) 
    {
        return new Real(
            this.v * a.v, 
            this.v * this.v * a.q + a.v * a.v * this.q,
            Unit.product(this.u, a.u));
    }
    
    public boolean isFinite()
    {
        return Double.isFinite(this.v) && Double.isFinite(this.q);
    }
}
