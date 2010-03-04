package auction;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Email {//tracks users - Buyers and Sellers
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private String email;
    
    public Email(String e) {
    	email = e;
    }
    public Key getKey() { 
    	return key; 
    }
    public String getEmail() { 
    	return email; 
    }
    public void setEmail(String e) {
    	this.email = e;
    }
}