package crawler;

import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

public class WebCache { // move to mongodb
    private ConcurrentHashMap<String, Domain> cache;

    public WebCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    public Domain getDomainObject(String url) { // add timestamp
        String domain = null;
        try {
            domain = RepEnforcer.getDomainName(url);
            return cache.get(domain);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addDomain(Domain d, String url) {
        try {
            String domainName = RepEnforcer.getDomainName(url);
            cache.put(domainName, d);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
