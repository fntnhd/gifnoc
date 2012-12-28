package com.us.fountainhead.gifnoc.server.entity;

import com.us.fountainhead.gifnoc.server.validate.ValidationError;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Responsible for testing basic field level validation
 */
public class ApplicationValidationTest {
    
    private static ApplicationValidationTestHelper helper;
    
    public ApplicationValidationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper = new ApplicationValidationTestHelper();
    }

    @Test
    public void testHelperExists() {
        assertNotNull(helper);
    }
}