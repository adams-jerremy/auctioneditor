package auction;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class uploadXML extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world");
     
        String contentType = req.getContentType();
    	if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) 
    		{
    			resp.getWriter().println(contentType);
    		    int lastIndex=contentType.lastIndexOf("=");
    	        String boundary = contentType.substring(lastIndex+1, contentType.length());

    			//DataInputStream in = new DataInputStream(req.getInputStream());
    			InputStream in = req.getInputStream();
    		    BufferedReader r = new BufferedReader(new InputStreamReader(in));
    		    StringBuffer buf = new StringBuffer();
    		    String line;
    		    while ((line = r.readLine())!=null && !line.contains("Content-Type")) {
    		    }
    		    
    		    while((line = r.readLine()) != null && !line.contains(boundary))
    		    {
    		    	buf.append(line);
    		    	resp.getWriter().println(line);
    		    }
    		    String s = buf.toString();    	
    		}
      //File f1 = new File(req.getParameter("uFile"));
       // resp.getWriter().println(f1.length());
        
    }
}