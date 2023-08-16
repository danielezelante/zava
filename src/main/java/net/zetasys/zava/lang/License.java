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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import net.zetasys.zava.math.Permuta;
import org.apache.commons.codec.binary.Base32;

public class License
{

    public static String genkey(byte[] data, byte[] salt, Permuta pe) throws NoSuchAlgorithmException
    {
        if (data.length != 9)
            throw new IllegalArgumentException(String.format("data length must be 9 (actual: %d)", data.length));
        final int[] p = pe.get();
        if (p.length != 25)
            throw new IllegalArgumentException(String.format("Permuta length must be 25 (actual: %d)", p.length));

        final byte[] salad = new byte[data.length + salt.length];
        System.arraycopy(data, 0, salad, 0, data.length);
        System.arraycopy(salt, 0, salad, data.length, salt.length);

        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte[] saladigest = md5.digest(salad);
        final byte[] keyraw = new byte[data.length + saladigest.length];
        System.arraycopy(data, 0, keyraw, 0, data.length);
        System.arraycopy(saladigest, 0, keyraw, data.length, saladigest.length);
        final byte[] keyperm = pe.apply(keyraw);

        final Base32 b32 = new Base32();
        final String key32 = b32.encodeToString(keyperm);

        final StringBuilder sb = new StringBuilder(key32.length());
        for (int j = 0; j < key32.length(); ++j)
        {
            final char k = key32.charAt(j);
            if (j != 0 && j % 5 == 0)
                sb.append('-');
            sb.append(k);
        }

        return sb.toString();

    }

    public static String genkey(long k, byte[] salt, Permuta pe) throws NoSuchAlgorithmException
    {
        long q = k;
        final byte[] data = new byte[9];
        for (int j = 0; j < 8; ++j)
        {
            data[j] = (byte) (q & 0xFF);
            q >>= 8;
        }
        data[8] = 0;
        return genkey(data, salt, pe);
    }

    public static byte[] validateKey(final String s, byte[] salt, Permuta pe) throws NoSuchAlgorithmException
    {
        final int sl = s.length();
        if (sl != 47)
            return null;
        final StringBuilder sb = new StringBuilder(sl);
        for (int j = 0; j < sl; ++j)
        {
            final char k = s.charAt(j);
            if (j % 6 == 5)
            {
                if (k != '-')
                    return null;
            } else
                sb.append(k);
        }

        final Base32 b32 = new Base32();
        final byte[] keyperm = b32.decode(sb.toString());
        final byte[] keyraw = pe.unapply(keyperm);
        final byte[] data = new byte[9];
        System.arraycopy(keyraw, 0, data, 0, data.length);
        final byte[] saladigest = new byte[keyraw.length - data.length];
        System.arraycopy(keyraw, data.length, saladigest, 0, saladigest.length);

        final byte[] salad = new byte[data.length + salt.length];
        System.arraycopy(data, 0, salad, 0, data.length);
        System.arraycopy(salt, 0, salad, data.length, salt.length);
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte[] saladigestcheck = md5.digest(salad);

        if (!Arrays.equals(saladigest, saladigestcheck))
            return null;
        return data;

    }

    public static Long validateKeyLong(final String s, byte[] salt, Permuta pe) throws NoSuchAlgorithmException
    {
        final byte[] data = validateKey(s, salt, pe);
        if (data == null)
            return null;
        long q = 0;
        for (int j = 7; j >= 0; --j)
        {
            q <<= 8;
            q |= data[j] & 0xFF;
        }
        if (data[8] != 0)
            return null;
        return q;
    }

    private License()
    {
    }

}
