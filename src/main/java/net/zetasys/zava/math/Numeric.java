/*
 * Copyright 2018 by Daniele Zelante <zeldan@zeldan.net>.
 */
package net.zetasys.zava.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;



/**
 *
 * @author zeldan
 */
public class Numeric
{

    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal THOUSAND = new BigDecimal(1000);
 
    public static BigDecimal computeVAT(BigDecimal val, BigDecimal vatpc, int decimaldigits)
    {
        return val.multiply(vatpc).setScale(decimaldigits).divide(vatpc.add(HUNDRED), RoundingMode.HALF_EVEN);
    }
    
    public static MonetaryAmount computeVAT(MonetaryAmount val, BigDecimal vatpc)
    {
        return val.multiply(vatpc).divide(vatpc.add(HUNDRED));
    }
    
    
    public static MonetaryAmount max(MonetaryAmount x, MonetaryAmount y)
    {
        return x.isGreaterThanOrEqualTo(y) ? x : y;
    }
    
    public static MonetaryAmount min(MonetaryAmount x, MonetaryAmount y)
    {
        return x.isLessThanOrEqualTo(y) ? x : y;
    }
    
    public static Money max(Money x, Money y)
    {
        return x.isGreaterThanOrEqualTo(y) ? x : y;
    }
    
    public static Money min(Money x, Money y)
    {
        return x.isLessThanOrEqualTo(y) ? x : y;
    }
    
 
    
    private Numeric() {}
}
