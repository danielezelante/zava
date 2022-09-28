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

package net.zetasys.zava.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.javamoney.moneta.Money;

/**
 * JDBC SQL wrapper to allow transparent conversion of java-null to SQL-NULL
 * @author zeldan
 */
public class Zql
{

    public static String localizesqlstate(final String s)
    {
        if (s == null)
            return null;
        try
        {
            final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("net/zetasys/db/Bundle"); // NOI18N
            if (bundle == null)
                return String.format("SQLSTATE<%s>", s);
            return bundle.getString("sqlstate." + s.toUpperCase());
        } catch (java.util.MissingResourceException ex)
        {
            System.err.println(String.format("TODO localize sql: %s : %s", s, ex.toString()));
        }
        return String.format("SQLSTATE<%s>", s);

    }

    public static Double getDouble(ResultSet rs, String field) throws SQLException
    {
        final var x = rs.getDouble(field);
        return rs.wasNull() ? null : x;
    }

    /**
     * set Double value in statement, converting java-null to SQL-NULL
     * @param st PreparedStatement to use
     * @param field field index (1-based)
     * @param value value to set (null is allowed)
     * @throws SQLException
     */
    public static void setDouble(PreparedStatement st, int field, Double value) throws SQLException
    {
        if (value == null)
            st.setNull(field, java.sql.Types.DOUBLE);
        else
            st.setDouble(field, value);
    }

    public static void setString(PreparedStatement st, int field, String value) throws SQLException
    {
        if (value == null)
            st.setNull(field, java.sql.Types.VARCHAR);
        else
            st.setString(field, value);
    }

    public static void setInteger(PreparedStatement st, int field, Integer value) throws SQLException
    {
        if (value == null)
            st.setNull(field, java.sql.Types.INTEGER);
        else
            st.setInt(field, value);
    }

    public static void setBigDecimal(PreparedStatement st, int field, BigDecimal value) throws SQLException
    {
        if (value == null)
            st.setNull(field, java.sql.Types.NUMERIC);
        else
            st.setBigDecimal(field, value.stripTrailingZeros());
    }

    public static void setTimestamp(PreparedStatement st, int field, Timestamp value) throws SQLException
    {
        if (value == null)
            st.setNull(field, java.sql.Types.TIMESTAMP);
        else
            st.setTimestamp(field, value);
    }

    
    // TODO useless, since ResultSet.getString returns null if sql NULL
    public static String getString(ResultSet rs, String field) throws SQLException
    {
        final var x = rs.getString(field);
        return rs.wasNull() ? null : x;
    }

    public static String getString(ResultSet rs, int nfield) throws SQLException
    {
        final var x = rs.getString(nfield);
        return rs.wasNull() ? null : x;
    }

    public static String getStringUnique(ResultSet rs, String field)
        throws SQLException, NotUniqueException
    {
        String s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getString(rs, field);
        }

        return s;
    }

    public static String getStringUnique(ResultSet rs, int nfield)
        throws SQLException, NotUniqueException
    {
        String s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getString(rs, nfield);
        }

        return s;
    }

    public static Timestamp getTimestampUnique(ResultSet rs, String field)
        throws SQLException, NotUniqueException
    {
        Timestamp s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getTimestamp(rs, field);
        }

        return s;
    }

    public static Long getLongUnique(ResultSet rs, int nfield)
        throws SQLException, NotUniqueException
    {
        Long s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getLong(rs, nfield);
        }

        return s;
    }

    public static Long getLongUnique(ResultSet rs, String field)
        throws SQLException, NotUniqueException
    {
        Long s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getLong(rs, field);
        }

        return s;
    }
    
    
    public static Integer getIntegerUnique(ResultSet rs, int nfield)
        throws SQLException, NotUniqueException
    {
        Integer s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getInteger(rs, nfield);
        }

        return s;
    }

    public static Integer getIntegerUnique(ResultSet rs, String field)
        throws SQLException, NotUniqueException
    {
        Integer s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getInteger(rs, field);
        }

        return s;
    }
    
    public static Money getMoney(ResultSet rs, String valorfield, String currencyfield) throws SQLException, BadDataException
    {
        final var valor = getBigDecimal(rs, valorfield);
        if (valor == null)
            return null;
        final var curry = getString(rs, currencyfield);
        if (curry == null)
            throw new BadDataException(null, valorfield, "null");
        return Money.of(valor, curry);
    }

    static BigDecimal pscale(BigDecimal x)
    {
        return x.scale() >= 0 ? x : x.setScale(0);
    }
    
    public static BigDecimal getBigDecimal(ResultSet rs, String field) throws SQLException
    {
        final var x = rs.getBigDecimal(field);
        return rs.wasNull() ? null : pscale(x.stripTrailingZeros());
    }

    public static BigDecimal getBigDecimal(ResultSet rs, int nfield) throws SQLException
    {
        final var x = rs.getBigDecimal(nfield);
        return rs.wasNull() ? null : pscale(x.stripTrailingZeros());
    }

    public static BigDecimal getBigDecimalUnique(ResultSet rs, String field)
        throws SQLException, NotUniqueException
    {
        BigDecimal s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getBigDecimal(rs, field);
        }

        return s;
    }

    public static BigDecimal getBigDecimalUnique(ResultSet rs, int nfield)
        throws SQLException, NotUniqueException
    {
        BigDecimal s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getBigDecimal(rs, nfield);
        }

        return s;
    }

    public static Boolean getBoolean(ResultSet rs, String field) throws SQLException
    {
        final var x = rs.getBoolean(field);
        return rs.wasNull() ? null : x;
    }

    public static Boolean getBooleanUnique(ResultSet rs, String field)
        throws SQLException, NotUniqueException
    {
        Boolean s = null;
        while (rs.next())
        {
            if (s != null)
                throw new NotUniqueException(null, null);
            s = getBoolean(rs, field);
        }

        return s;
    }

    public static Timestamp getTimestamp(ResultSet rs, String field) throws SQLException
    {
        final var x = rs.getTimestamp(field);
        return rs.wasNull() ? null : x;
    }

    public static Integer getInteger(ResultSet rs, String field) throws SQLException
    {
        final var x = rs.getInt(field);
        return rs.wasNull() ? null : x;
    }
    
     public static Integer getInteger(ResultSet rs, int nfield) throws SQLException
    {
        final var x = rs.getInt(nfield);
        return rs.wasNull() ? null : x;
    }

    public static Long getLong(ResultSet rs, String field) throws SQLException
    {
        final var x = rs.getLong(field);
        return rs.wasNull() ? null : x;
    }

    public static Long getLong(ResultSet rs, int nfield) throws SQLException
    {
        final var x = rs.getLong(nfield);
        return rs.wasNull() ? null : x;
    }

    public static java.sql.Date importDate(java.util.Date date)
    {
        return date == null ? null
            : java.sql.Date.valueOf(
                LocalDateTime.ofInstant(
                    date.toInstant(), ZoneId.systemDefault()
                ).toLocalDate()
            );

    }

    protected Zql()
    {
    }

}
