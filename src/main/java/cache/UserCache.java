package cache;

import controllers.OrderController;
import controllers.UserController;
import model.Order;
import model.User;
import utils.Config;

import java.util.ArrayList;

//TODO: Build this cache and use it - FIX
public class UserCache {

    // List of products
    private ArrayList<User> users;

    // Time cache should live
    private long ttl;

    // Sets when the cache has been created
    private long created;

    public UserCache() {
        this.ttl = Config.getUserTtl();
    }

    public ArrayList<User> getUsers(Boolean forceUpdate) {

        // If we whis to clear cache, we can set force update.
        // Otherwise we look at the age of the cache and figure out if we should update.
        // If the list is empty we also check for new products
        if (forceUpdate
                || ((this.created + this.ttl) >= (System.currentTimeMillis() / 1000L))
                || this.users.isEmpty()) {

            // Get products from controller, since we wish to update.
            ArrayList<User> users = UserController.getUsers();

            // Set products for the instance and set created timestamp
            this.users = users;
            this.created = System.currentTimeMillis() / 1000L;


            // tester om cachen virker, hvis cachen ikke bliver brugt vil nedenstående blive udskrevet mere end en gang.
            System.out.println("This Cache is never used");

        }

        // Return the documents
        return this.users;
    }
}





