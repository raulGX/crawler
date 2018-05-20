package crawler;

public class Domain {
    private String ip;
    private int ttl;
    private boolean allowRobots;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public boolean isAllowRobots() {
        return allowRobots;
    }

    public void setAllowRobots(boolean allowRobots) {
        this.allowRobots = allowRobots;
    }

    public Domain(String ip, int ttl, boolean allowRobots) {
        this.ip = ip;
        this.ttl = ttl;
        this.allowRobots = allowRobots;
    }
}
