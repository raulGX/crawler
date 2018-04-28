package request;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.URL;

public class Request {
    private int redirectCount = 0;
    public static int REDIRECTS_BEFORE_ABANDON = 5;
    private RequestResponse readHeader(BufferedReader in) {
        int contentLength = 0;
        int responseCode = 0;
        String location = "";
        try {
            String line = in.readLine();
            responseCode = Integer.parseInt(line.split(" ")[1]);
            while ((line = in.readLine()).length() != 0) {
                String[] row = line.split(": ");
                if (row[0].equals("Content-Length")) {
                    contentLength = Integer.parseInt(row[1]);
                }
                if (row[0].equals("Location")) {
                    location = row[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RequestResponse(responseCode, contentLength, location);
    }
    private String getBody(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while (( line = in.readLine())!= null) {
            sb.append(line);
            if (line.contains("/html>")) {
                break;
            }
        }
        return sb.toString();
    }
    public String httpRequest(String host, String path, int port) throws Exception {
        host = host.trim();
        path = path.trim();
        InputStream in;
        OutputStream out;
        InputStreamReader inputStream;
        BufferedReader bufferedreader;
        if (host.contains("https")) {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            URL url = new URL(host);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(sslsocketfactory);
            in = conn.getInputStream();
            inputStream = new InputStreamReader(in);
            bufferedreader = new BufferedReader(inputStream);
            return getBody(bufferedreader);
        }
        else {
            String header = "GET " + path + " HTTP/1.1\r\nHost: " + host + "\r\nUser-Agent: CLIENTRIW\r\nConnection: close\r\n\r\n";
            String ip;

            if (host.equals("127.0.0.1")) {
                ip = "127.0.0.1";
            }
            else {
                ip = java.net.InetAddress.getByName(host).getHostAddress(); // todo replace with homemade function
            }

            Socket sock = new Socket(ip, port);
            in = sock.getInputStream();
            out = sock.getOutputStream();
            inputStream = new InputStreamReader(in);
            bufferedreader = new BufferedReader(inputStream);
            DataOutputStream output = new DataOutputStream(out);
            output.write(header.getBytes());
            output.flush();
        }

        RequestResponse res = readHeader(bufferedreader);
        if (res.shouldRedirect()) {
            if (redirectCount >= REDIRECTS_BEFORE_ABANDON) {
                throw new Exception("spidertrapped");
            }
            redirectCount++;
            String redirectLocation = res.getRedirectLocation();
            if (redirectLocation.contains("http")) {
                return httpRequest(redirectLocation,"/", port);
            }
            return httpRequest(host, redirectLocation, port);
        }
        return getBody(bufferedreader);
    }
}
