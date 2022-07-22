/*
 * YAL zeldan
 */
package net.zetasys.zava.db;

/**
 *
 * @author zeldan
 */
public class NotUniqueException extends BadSchemaException
{
    public NotUniqueException(String table, String field)
    {
        super(table, field, "not unique");
    }
    
}
