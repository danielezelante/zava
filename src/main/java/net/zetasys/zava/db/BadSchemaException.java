/*
 * YAL zeldan
 */
package net.zetasys.zava.db;

/**
 *
 * @author zeldan
 */
public class BadSchemaException extends Exception
{
    final protected String table;
    final protected String field;
    final protected String text;
    
    public BadSchemaException(String table, String field, String text)
    {
        this.table = table;
        this.field = field;
        this.text = text;
    }
    
    @Override
    public String toString()
    {
        return String.format("BadSchemaException: table=%s field=%s text=%s", 
            table, field, text);
    }
}
