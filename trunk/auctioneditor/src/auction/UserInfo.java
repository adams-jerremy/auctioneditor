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
public class UserInfo {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private User user;
    
    @Persistent
    private String userid;

    public UserInfo(User user){
        this.user = user;
        this.userid = this.user.getUserId();
    }

    public Key getKey() {
        return key;
    }

    public User getUser() {
        return user;
    }

    
    public void setUser(User user) {
        this.user = user;
    }

}