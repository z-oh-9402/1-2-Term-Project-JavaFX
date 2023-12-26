package mobilefood.customer;

import java.io.Serializable;
import java.util.HashMap;

import mobilefood.restaurant.Food;

public class Customer implements Serializable {
    private String username;
    private String password;
    private HashMap<Integer,HashMap<Food,Integer>> order;

    public Customer(String username,String password)
    {
        this.username = username;
        this.password = password;
        order = new HashMap<>();
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public void addOrder(Food food,int quantity)
    {
        HashMap<Food,Integer> foodToCount = new HashMap<>();
        foodToCount.put(food,quantity);
        order.put(food.getRestaurantId(),foodToCount);
    }
}
