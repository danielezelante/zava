/*
 * YAL zeldan
 */
package net.zetasys.zava.math;

/**
 *
 * @author zeldan
 */
public enum DayMoneyDomain implements Domain
{
    INSTANCE;
    @Override public String[] get() {return new String[] {"day", "money"};}
}
