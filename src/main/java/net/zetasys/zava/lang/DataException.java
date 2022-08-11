
package net.zetasys.zava.lang;

/**
 *
 * @author zeldan
 */
public class DataException extends Exception
{
    protected final Object data;
    
    public DataException(Object data)
    {
        this.data = data;
    }
    
    public Object getData()
    {
        return this.data;
    }
    
}
