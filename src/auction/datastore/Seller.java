package auction.datastore;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Seller {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Key seller;
    
//    @Persistent
//    private String sellerID;

    @Persistent
    private String item;

    @Persistent
    private Date date;

    @Persistent
    private String url;
    
    @Persistent
    private String price;
    
    public Seller(Key seller, String item, Date date, String url, String price) {
        this.seller = seller;
        //this.sellerID = seller.getUserId();
        this.item = item;
        this.date = date;
        this.url = url;
        this.price = price;
    }

    public Key getKey() {
        return key;
    }

    public Key getSeller() {
        return seller ;
    }
    
//    public String getSellerID() {
//    	return sellerID;
//    }

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
    
    public void setSeller(Key seller) {
        this.seller = seller;
    }

//    public void setSellerID(String sellerID) {
//        this.sellerID = sellerID;
//    }

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