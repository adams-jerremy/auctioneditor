package auction;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
class Email {//tracks users - Buyers and Sellers
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private String email;
    public Email(String e) { email = e;}
    public Key getKey() { return key; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e;}
}

@PersistenceCapable
class Item { //temporary table for tracking items - needs to be expanded for seller page
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private String name;
    @Persistent
    private Key seller;
    public Item(String n,Key s) { name = n;seller = s;}
    public Key getKey() { return key; }
    public String getName() { return name; }
    public void setName(String e) { this.name = e;}
    public Key getSeller() { return seller; }
    public void setSeller(Key k) { this.seller = k;}
}


@PersistenceCapable
class WatchList {//buyers watch list
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private Key buyer;
    @Persistent
    private Key item;
    public WatchList(Key b, Key i) { buyer = b;item = i;}
    public Key getKey() { return key; }
    public Key getBuyer() { return buyer; }
    public void setBuyer(Key k) { this.buyer = k;}
    public Key getItem() { return item; }
    public void setItem(Key k) { this.item = k;}
}
@PersistenceCapable
class Bid {//saved bid history
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

