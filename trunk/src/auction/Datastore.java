package auction;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


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





