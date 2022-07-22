/*
 * YAL zeldan
 */
package net.zetasys.zava.db;

/**
 *
 * @author zeldan
 */
public class BadDataException extends Exception
{
    final protected String table;
    final protected String field;
    final protected String text;
    
    public BadDataException(String table, String field, String text)
    {
        this.table = table;
        this.field = field;
        this.text = text;
    }
}
