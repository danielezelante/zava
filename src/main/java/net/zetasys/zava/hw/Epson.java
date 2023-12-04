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
package net.zetasys.zava.hw;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.zetasys.zava.lang.UniAscii;

/**
 *
 * @author zeldan
 */
public class Epson
{

    protected final OutputStream s;

    public Epson(OutputStream s)
    {
        this.s = s;
    }

    public void reset() throws IOException
    {
        escape('@');
    }

    public void cr() throws IOException
    {
        s.write(0x0D);
    }

    public void lf() throws IOException
    {
        s.write(0x0A);
    }

    public void ff() throws IOException
    {
        s.write(0x0C);
    }

    protected void escape(char k) throws IOException
    {
        s.write(0x1B);
        s.write(k);
    }

    public void cpi10() throws IOException
    {
        escape('P');
    }

    public void cpi12() throws IOException
    {
        escape('M');
    }

    public void cpi15() throws IOException
    {
        escape('g');
    }

    public void bold(boolean x) throws IOException
    {
        escape(x ? 'E' : 'F');
    }

    public void write(String x) throws IOException
    {
        s.write(UniAscii.downgrade(x).getBytes(StandardCharsets.US_ASCII));
    }

    public void writeln(String x) throws IOException
    {
        write(x);
        cr();
        lf();
    }

    public void writeln180(String x, int a180) throws IOException
    {
        write(x);
        cr();
        advance180(a180);
    }

    public void uel() throws IOException
    {
        s.write(0x1B);
        write("%-12345X");
    }

    public void uelESCP() throws IOException
    {
        uel();
        writeln("@PJL COMMENT ESC/P ");
        writeln("@PJL ENTER LANGUAGE = ESCP ");
    }

    public void leftMargin(int x) throws IOException
    {
        escape('l');
        s.write(x);
    }

    public void setPageLengthLines(int x) throws IOException
    {
        escape('C');
        s.write(x);
    }

    public void setPageLengthInches(int x) throws IOException
    {
        escape('C');
        s.write(0);
        s.write(x);
    }

    public void setStep180(int x) throws IOException
    {
        escape('3');
        s.write(x);
    }

    public void condensed() throws IOException
    {
        s.write(0x0F);
    }

    public void advance180(int x) throws IOException
    {
        escape('J');
        s.write(x);
    }

    public void bel() throws IOException
    {
        s.write(0x07);
    }

    public void load() throws IOException
    {
        //escape((char)0x19);
        //s.write('B');
        // automatico ??
    }

}
