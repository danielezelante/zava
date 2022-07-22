/*
 * Copyright 2018 by Daniele Zelante <zeldan@zeldan.net>.
 */
package net.zetasys.zava.lang;




public class Action
{
    protected Able a;
    protected int c;
    protected Object tag;
    
    public Action(Able a)
    {
        this.a = a;
        this.c = 0;
        this.tag = null;
    }
    
    protected void inc()
    {
        ++this.c;
    }
    
    protected void dec()
    {
        if (this.c == 0) throw new IllegalStateException("dec at 0");
        --this.c;
        if (this.c == 0) this.a.act();
    }
    
        
    void setTag(final Object tag)
    {
        this.tag = tag;
    }
    public interface Able
    {
        void act();
    }
    
    public class Request implements AutoCloseable
    {
        public Request()
        {
            Action.this.inc();
        }
        
        @Override
        public void close()
        {
            Action.this.dec();
        }
        
        public void setTag(final Object tag)
        {
            Action.this.setTag(tag);
        }
    }
    
    
}
