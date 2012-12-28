package com.us.fountainhead.gifnoc.server.entity;

import com.us.fountainhead.gifnoc.server.validate.ValidationError;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Responsible for testing basic field level validation
 */
public class EnvironmentValidationTest {
    
    private static EnvironmentValidationTestHelper helper;
    
    public EnvironmentValidationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper = new EnvironmentValidationTestHelper();
    }

    @Test
    public void testHelperExists() {
        assertNotNull(helper);
    }
}