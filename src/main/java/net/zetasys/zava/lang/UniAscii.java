/*
 * YAL zeldan
 */
package net.zetasys.zava.lang;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author zeldan
 */
public class UniAscii
{
    protected UniAscii() {}
    
    static public String downgrade(String a)
    {
        final var x = StringUtils.stripAccents(a);
        final var z = x.length();
        final var y = new StringBuilder(z);
        
        for (int j=0; j<z; ++j)
        {
            final var k = x.charAt(j);
            switch (k)
            {
                case '\u00AD' :
                case '\u2010' :
                case '\u2011' :
                case '\u2012' :
                case '\u2013' :
                case '\u2014' :
                case '\u2015' :
                    y.append('-');
                    break;
                    
                default: y.append(k);
            }
            
            
        }
        
        return y.toString();
    }
}
