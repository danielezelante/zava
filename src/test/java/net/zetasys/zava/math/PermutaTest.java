/*
 * YAL zeldan
 */
package net.zetasys.zava.math;

import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PermutaTest
{

    private static final Logger LOG = Logger.getLogger(PermutaTest.class.getName());

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    public PermutaTest()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of get method, of class Permuta.
     */
    @Test
    public void testGet()
    {
        Permuta instance = new Permuta(new int[]
        {
            0, 1, 2
        });
        int[] expResult = new int[]
        {
            0, 1, 2
        };
        int[] result = instance.get();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of apply method, of class Permuta.
     */
    @Test
    public void testApply()
    {
        byte[] x = new byte[]
        {
            22, 33, 44
        };
        Permuta instance = new Permuta(new int[]
        {
            2, 1, 0
        });
        byte[] expResult = new byte[]
        {
            44, 33, 22
        };
        byte[] result = instance.apply(x);
        assertArrayEquals(expResult, result);

    }

    /**
     * Test of unapply method, of class Permuta.
     */
    @Test
    public void testUnapply()
    {
        byte[] x = new byte[]
        {
            44, 33, 22
        };
        Permuta instance = new Permuta(new int[]
        {
            2, 1, 0
        });
        byte[] expResult = new byte[]
        {
            22, 33, 44
        };
        byte[] result = instance.unapply(x);
        assertArrayEquals(expResult, result);
    }

}
