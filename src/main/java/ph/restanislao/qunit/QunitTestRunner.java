/*
* Copyright 2012 ph.restanislao
*/
package ph.restanislao.qunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.Assert;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Run Qunit tests.
 * @goal runTest
 */
public class QunitTestRunner extends AbstractMojo {
    /**
     * Qunit test source file.
     * @parameter
     */
    private String srcTest;

    /**
     * Qunit test source dir.
     * @parameter default-value="${project.basedir}/src/test/javascript"
     */
    private File srcTestDir;

    /**
     * Qunit test filename filter.
     * @parameter default-value="Test.html"
     */
    private String filter;

    /**
     * Maximum execution time in seconds.
     * @parameter default-value="120"
     */
    private Integer execTime;

    /**
     * Qunit test runner implementation.
     * First, it pulls all the files from a given qunit source directory
     * and then each file will be executed using HtmlUnit.
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Executing Qunit Test Runner...");
        getLog().info("Qunit test source directory: " + srcTestDir);

        List<File> list = new ArrayList<File>();
        getFiles(srcTestDir, list, filter);

        getLog().info("Test(s) to be executed: " + list.size());

        for (File testFile : list) {
            executeTest(testFile.getAbsolutePath());
        }

        getLog().info("Finished Qunit Test Runner execution...");
    }

    /**
     * Execute Qunit test.
     * @param test
     * @throws MojoFailureException
     */
    private void executeTest(String test) throws MojoFailureException {
        getLog().info("Running Qunit Test >>> " + test);
        String url = "file://" + test;
        try {
            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
            final HtmlPage page = webClient.getPage(url);
            Thread.sleep(5000);

            int timeLeft = execTime;
            while (!page.asText().contains("Tests completed")) {
                Thread.sleep(1000);
                timeLeft--;
                if (timeLeft == 0) {
                    Assert.fail("Tests took longer than " + execTime + " s to execute.");
                }
            }

            DomNodeList domNodeList = page.querySelectorAll("li.fail");
            Assert.assertEquals("Qunit test failed! " + "Please check " + test, 0, domNodeList.size()/2);

            getLog().info("Finished running Qunit Test >>> " + test);
        } catch (Exception e) {
            throw new MojoFailureException("Failed to execute Qunit test runner! ");
        }
    }

    /**
     * Pull all the test files
     * @param folder - serves as root directory where it starts to get the test files.
     * @param list - stores legible test file
     * @param filter - filters the file based on file name which matches the string filter.
     */
    private void getFiles(File folder, List<File> list, String filter) {
        folder.setReadOnly();
        File[] files = folder.listFiles();
        for(int j = 0; j < files.length; j++) {
            if(files[j].isDirectory()) {
                getFiles(files[j], list, filter);
            }

            if ((filter != null && files[j].getName().contains(filter))
                        || filter == null) {
                    list.add(files[j]);
            }
        }
    }
}
