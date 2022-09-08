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
