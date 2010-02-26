package auction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import java.net.URI;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Seller {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String seller;

    @Persistent
    private String item;

    @Persistent
    private Date date;

    @Persistent
    private String url;
    
    @Persistent
    private String price;
    
    public Seller(String seller, String item, Date date, String url, String price) {
        this.seller = seller;
        this.item = item;
        this.date = date;
        this.url = url;
        this.price = price;
    }

    public Key getKey() {
        return key;
    }

    public String getSeller() {
        return seller ;
    }

    public String getItem() {
        return item;
    }

    public Date getDate() {
        return date;
    }

    public String getUrl(){
    	return url;
    }
    
    public String getPrice(){
    	return price;
    }
    
    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setUrl(String url) {
    	this.url = url;
    }
    
    public void setPrice(String price) {
    	this.price = price;
    }
}