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
        if (this.c == 0)
            throw new IllegalStateException("dec at 0");
        --this.c;
        if (this.c == 0)
            this.a.act();
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
