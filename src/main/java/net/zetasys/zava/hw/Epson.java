
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
    
    
    public void CR() throws IOException
    {
        s.write(0x0D);
    }
    
    public void LF() throws IOException
    {
        s.write(0x0A);
    }
    
    public void FF() throws IOException
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
        CR(); LF();
    }

    public void uel() throws IOException
    {
        s.write(0x1B);
        write("%-12345X");    
    }
    
    
    public void uel_escp() throws IOException
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
    
    public void setLengthLines(int x) throws IOException
    {
        escape('C');
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
    
    public void BEL() throws IOException
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
