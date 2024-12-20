/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package top.deeke.jni.v8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class NodeJSTest {

    private NodeJS nodeJS;

    @Before
    public void setup() {
        if (skipTest()) {
            return;
        }

        nodeJS = NodeJS.createNodeJS();
    }

    @After
    public void tearDown() {
        if (skipTest()) {
            return;
        }

        nodeJS.release();
    }

    private static boolean skipTest() {
        return !V8.isNodeCompatible();
    }

    private final static String skipMessage = "Skipped test (Node.js features not included in native library)";

    @Test
    public void testCreateNodeJS() {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        assertNotNull(nodeJS);
    }

    @Test
    public void testSingleThreadAccess_Require() throws InterruptedException {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        final boolean[] result = new boolean[] { false };
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    nodeJS.require(File.createTempFile("temp", ".js"));
                } catch (Error e) {
                    result[0] = e.getMessage().contains("Invalid V8 thread access");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();
        t.join();

        assertTrue(result[0]);
    }

    @Test
    public void testGetVersion() {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        String result = nodeJS.getNodeVersion();

        assertEquals("7.4.0", result);
    }

    @Test
    public void testSingleThreadAccess_HandleMessage() throws InterruptedException {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        final boolean[] result = new boolean[] { false };
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    nodeJS.handleMessage();
                } catch (Error e) {
                    result[0] = e.getMessage().contains("Invalid V8 thread access");
                }
            }
        });
        t.start();
        t.join();

        assertTrue(result[0]);
    }

    @Test
    public void testSingleThreadAccess_IsRunning() throws InterruptedException {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        final boolean[] result = new boolean[] { false };
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    nodeJS.isRunning();
                } catch (Error e) {
                    result[0] = e.getMessage().contains("Invalid V8 thread access");
                }
            }
        });
        t.start();
        t.join();

        assertTrue(result[0]);
    }

    @Test
    public void testExecuteNodeScript_Startup() throws IOException {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        nodeJS.release();
        File testScript = createTemporaryScriptFile("global.passed = true;", "testScript");

        nodeJS = NodeJS.createNodeJS(testScript);
        runMessageLoop();

        assertEquals(true, nodeJS.getRuntime().getBoolean("passed"));
        testScript.delete();
    }

    @Test
    public void testExecNodeScript() throws IOException {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        nodeJS.release();
        File testScript = createTemporaryScriptFile("global.passed = true;", "testScript");

        nodeJS = NodeJS.createNodeJS();
        nodeJS.exec(testScript);
        runMessageLoop();

        assertEquals(true, nodeJS.getRuntime().getBoolean("passed"));
        testScript.delete();
    }

    @Test
    public void testExecuteNodeScript_viaRequire() throws IOException {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        nodeJS.release();
        File testScript = createTemporaryScriptFile("global.passed = true;", "testScript");

        nodeJS = NodeJS.createNodeJS();
        nodeJS.require(testScript).close();
        runMessageLoop();

        assertEquals(true, nodeJS.getRuntime().getBoolean("passed"));
        testScript.delete();
    }

    @Test
    public void testExports() throws IOException {
        assumeFalse(skipMessage, skipTest()); // conditional skip
        nodeJS.release();
        File testScript = createTemporaryScriptFile("exports.foo=7", "testScript");

        nodeJS = NodeJS.createNodeJS();
        V8Object exports = nodeJS.require(testScript);
        runMessageLoop();

        assertEquals(7, exports.getInteger("foo"));
        exports.close();

    }

    private void runMessageLoop() {
        while (nodeJS.isRunning()) {
            nodeJS.handleMessage();
        }
    }

    private static File createTemporaryScriptFile(final String script, final String name) throws IOException {
        File tempFile = File.createTempFile(name, ".js.tmp");
        PrintWriter writer = new PrintWriter(tempFile, "UTF-8");
        try {
            writer.print(script);
        } finally {
            writer.close();
        }
        return tempFile;
    }


}
