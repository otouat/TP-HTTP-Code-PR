///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

	static final String INDEX_FILE="index.html";
	static final String FILE_NF="404.html";
	
	
	
  /**
   * WebServer constructor.
   */
  protected void start() {
    ServerSocket s;
    

    System.out.println("Webserver starting up on port 3000");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(3000);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        // read from the client by the input stream on the socket
        BufferedReader in = new BufferedReader(new InputStreamReader(
            remote.getInputStream()));
        // get from the output stream for the client
        PrintWriter out = new PrintWriter(remote.getOutputStream());
        // get binary output from the output stream for the client
        BufferedOutputStream dataOut = new BufferedOutputStream(remote.getOutputStream());
        File file;
        int lenght;
        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        String str = ".";
        String header="";
        while (!str.equals("")) {
            str = in.readLine();
            header+=str;
        }

        System.out.println(header);
        StringTokenizer parser=new StringTokenizer(header);
        //we parse the request
        String request=parser.nextToken().toUpperCase();
        System.out.println(request);
        
        //we get the file
        String fileRequested=parser.nextToken().toLowerCase();
        System.out.println(fileRequested);
        
        if(fileRequested.endsWith("/")) {
    		fileRequested=INDEX_FILE;
    		file=new File(fileRequested);
        	lenght=(int) file.length();
    	}
        else {
        	String trueFileRequested=fileRequested.substring(1);
        	file=new File(trueFileRequested);
        	lenght=(int) file.length();
        }
        
    	
    	
    	
    	
    	String typeOfContent=getType(fileRequested);
        if(request.equals("GET")) {
        	
        	byte[] data=readData(file,lenght);
        	// Send the headers
        	out.println("HTTP/1.0 200 OK");
        	out.println("Server: Bot");
        	out.println("Content-Type: "+typeOfContent);
            out.println("Content-lenght: "+lenght);
        	out.println("");
        	out.flush();
        	
        	// Send the requested file
        	dataOut.write(data,0,lenght);
        	dataOut.flush();
        }
        // Send the response
        
        
        // this blank line signals the end of the headers
        
        // Send the HTML page
        
        remote.close();
      } catch (Exception e) {
        System.err.println("Error: " + e);
      }
    }
  }

  private byte[] readData(File file, int fileLenght) throws IOException {
	  FileInputStream in=null;
	  byte[] data;
	  try {
		in=new FileInputStream(file);
		data=new byte[fileLenght];
		in.read(data);
	  } finally {
		if(in!=null) {
			in.close();
		}
	}
	
	return data;
}

private String getType(String fileRequested) {
	if(fileRequested.endsWith(".html")) {
		return "text/html";
	}
	if(fileRequested.endsWith(".jpg")) {
		return "image/jpg";
	}
	if(fileRequested.endsWith(".mp3")) {
		return "audio/mp3";
	}
	if(fileRequested.endsWith(".png")) {
		return "image/png";
	}
	return null;
}

/**
   * Start the application.
   * 
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
