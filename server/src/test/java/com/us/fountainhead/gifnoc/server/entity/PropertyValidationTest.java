package com.us.fountainhead.gifnoc.server.entity;

import com.us.fountainhead.gifnoc.server.validate.ValidationError;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Responsible for testing basic field level validation
 */
public class PropertyValidationTest {
    
    private static PropertyValidationTestHelper helper;
    
    public PropertyValidationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper = new PropertyValidationTestHelper();
    }

    @Test
    public void testHelperExists() {
        assertNotNull(helper);
    }
}