package request;


public class App {

    public static void main(String[] args) throws Exception {
        Request req = new Request();
        System.out.println(req.httpRequest("news.ycombinator.com", "/", 80));
//        System.out.println(req.httpRequest("www.ac.tuiasi.ro", "/", 80));
//        System.out.println(req.httpRequest("127.0.0.1", "/spidertrap", 3000));
    }
}
