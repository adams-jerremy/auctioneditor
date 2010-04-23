package auction;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class AuctionApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

        // Defines only one route
       // router.attachDefault(HelloWorldResource.class);
        router.attach("/sellers/{seller}", SellerResource.class);
        router.attach("/items/create", ItemsResource.class);
        router.attach("/items/read", ItemsResource.class);
        router.attach("/items/item_id/update", ItemsResource.class);
        router.attach("/items/item_id/delete", ItemsResource.class);
        router.attach("/items/item_id/read", ItemsResource.class);
        return router;
    }
}