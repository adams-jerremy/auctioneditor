package auction.datastore;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Bid {//saved bid history
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private Key buyer;
    @Persistent
    private Key item;
    @Persistent
    private int bid;
    @Persistent
    private Date time;
    public Bid(Key b, Key i,int price, Date t) { buyer = b;item = i;bid = price; time =t;}
    public Key getKey() { return key; }
    public Key getBuyer() { return buyer; }
    public void setBuyer(Key k) { this.buyer = k;}
    public Key getItem() { return item; }
    public void setItem(Key k) { this.item = k;}
    public int getBid() { return bid; }
    public void setBid(int b) { this.bid = b;}
    public Date getTime() { return time; }
    public void setTime(Date t) { this.time = t;}
}