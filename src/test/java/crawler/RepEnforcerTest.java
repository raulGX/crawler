package crawler;

import junit.framework.TestCase;

import java.net.URISyntaxException;

public class RepEnforcerTest extends TestCase {

    public void testGetDomainName() {
        String startUrl = "http://news.ycombinator.com";
        try {
            System.out.println(RepEnforcer.allowDomainRobots(startUrl));
//          System.out.println(RepEnforcer.getDomainName(startUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}