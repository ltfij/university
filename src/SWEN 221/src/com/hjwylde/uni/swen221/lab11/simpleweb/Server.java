package com.hjwylde.uni.swen221.lab11.simpleweb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Code for Laboratory 11, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

/**
 * The following class implements a very simple single threaded web server. A web server responds to
 * HTTP requests over a socket by loading and transmitting back the requested file.
 * 
 * @author djp
 */
public class Server {
    
    private int port; // port to respond on
    private String root; // root of all html pages
    
    // private byte[] file_buffer = new byte[1024];
    
    public Server(int port, String root) {
        this.port = port;
        this.root = root;
    }
    
    /**
     * The following method is responsible for processing a single HTTP request command.
     * 
     * @param s the socket over which the request will be communicated.
     */
    public void processRequest(Socket s) {
        synchronized (s) {
            try {
                String request = Server.readRequest(s);
                Server.log("RECEIVED REQUEST: " + request.length() + " bytes");
                String httpCommand = Server.stripHttpGetCommand(request);
                String page = httpCommand.split(" ")[1];
                if (page.equals("/"))
                    // auto convert empty page request into index page.
                    page = "/index.html";
                
                // Determine the file name, by appending the root. Note, we need to ensure that the
                // right
                // "separator" is used for path names. For example, on windows the separate char is
                // "\", whilst on UNIX it is "/".  However, all HTTP get commands use "/".
                String filename = (root + page)
                    .replace('/', File.separatorChar);
                // Now, check if file exists
                if (new File(filename).exists())
                    // Yes, it exists!!
                    Server.sendFile(filename, s);
                else
                    // log("File doesn't exist: " + new File(filename).getAbsolutePath());
                    // No, the file doesn't exist.
                    Server.send404(page, s);
                
            } catch (IOException e) {
                Server.log("I/O Error - " + e);
            }
        }
    }
    
    public void run() {
        try (ServerSocket ss = new ServerSocket(port)) {
            Socket s;
            
            while (true) {
                s = ss.accept();
                new Thread(new RequestJob(s)).start();
            }
        } catch (IOException e) {
            // something bad happened
            Server.log(e.getMessage());
        }
    }
    
    /**
     * This method reads all possible data from the socket and returns it.
     */
    public static String readRequest(Socket s) throws IOException {
        Reader input = new InputStreamReader(new BufferedInputStream(
            s.getInputStream()));
        String request = "";
        char[] buf = new char[1024];
        int nread;
        
        // Read from socket until nothing left.
        do {
            nread = input.read(buf);
            String in = new String(buf, 0, nread);
            request += in;
        } while (nread == 1024);
        
        return request;
    }
    
    public static void send404(String page, Socket s) throws IOException {
        PrintStream output = new PrintStream(s.getOutputStream());
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html; charset=UTF-8\n");
        output.println("<h1>Not Found</h1>\n\n" + "The requested URL " + page
            + " was not found on this server.\n");
    }
    
    /**
     * Transmit file to socket in 1024 byte chunks.
     */
    public static void sendFile(String filename, Socket s) throws IOException {
        try (OutputStream output = s.getOutputStream();
            PrintStream pout = new PrintStream(output);
            FileInputStream input = new FileInputStream(filename);) {
            
            Server.log("Getting file stream: " + filename + " : " + s);
            Server.log("Sending file: " + filename + " : " + s);
            pout.println("HTTP/1.1 200 OK");
            if (filename.endsWith("jpg"))
                pout.println("Content-Type: image/jpeg; charset=UTF-8\n");
            else
                pout.println("Content-Type: text/html; charset=UTF-8\n");
            pout.flush();
            
            byte[] file_buffer = new byte[1024];
            
            int n;
            while ((n = input.read(file_buffer)) > 0) {
                output.write(file_buffer, 0, n);
                // I have introduced the following artificial delay specifically to throttle the
                // rate of
                // transmission. This is necessary since the server will be running on the same
                // machine as
                // the web-browser and, hence, will be extremely fast compared with normal. Thus,
                // the
                // pause
                // helps us to see the real problem.
                // Removing this line is not how to solve this lab :)
                Server.pause(25);
            }
        }
    }
    
    /**
     * This method looks for the HTTP GET command, and returns that; or, null if none was found.
     */
    public static String stripHttpGetCommand(String request) throws IOException {
        BufferedReader r = new BufferedReader(new StringReader(request));
        String line = "";
        while ((line = r.readLine()) != null)
            if (line.startsWith("GET"))
                // found the get command
                return line;
        return null;
    }
    
    protected static synchronized void log(String message) {
        System.out.println(message);
    }
    
    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            
        }
    }
    
    private class RequestJob implements Runnable {
        
        private Socket s;
        
        public RequestJob(Socket s) {
            this.s = s;
        }
        
        @Override
        public void run() {
            // Ok, if we get here, then we got a connection
            Server.log("ACCEPTED CONNECTION FROM: " + s);
            
            processRequest(s);
            
            // Finally, close the socket!
            try {
                s.close();
            } catch (IOException e) {
                Server.log(e.getMessage());
            }
        }
    }
}