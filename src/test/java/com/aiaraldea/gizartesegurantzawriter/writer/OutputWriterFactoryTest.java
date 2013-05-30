package com.aiaraldea.gizartesegurantzawriter.writer;

import junit.framework.TestCase;

/**
 *
 * @author inaki
 */
public class OutputWriterFactoryTest extends TestCase {

    public OutputWriterFactoryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getOutputWriter method, of class OutputWriterFactory.
     */
    public void testGetOutputWriterDefault() {
        System.out.println("getOutputWriter");
        String collectionName = "dummy";
        OutputWriter result = OutputWriterFactory.getOutputWriter(collectionName);
        assertEquals(MongoDBWriter.class, result.getClass());
    }

    public void testGetOutputWriterSimple() {
        System.out.println("getOutputWriter");
        System.setProperty(OutputWriterFactory.PROPERTY_NAME, OutputWriterFactory.PROPERTY_VALUE_SIMPLE);
        String collectionName = "dummy";
        OutputWriter result = OutputWriterFactory.getOutputWriter(collectionName);
        assertEquals(SimpleOutputWriter.class, result.getClass());
    }
}
