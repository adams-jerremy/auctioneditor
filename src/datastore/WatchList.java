package datastore;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class WatchList {//buyers watch list
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
