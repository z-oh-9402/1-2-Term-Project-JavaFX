package mobilefood.passableobjects;

import java.io.Serializable;
import java.util.HashMap;

import mobilefood.restaurant.Food;

public class Order implements Serializable{
    private int id;
    private String type;
    private String sender;
    private String receiver;
    private HashMap<Food,Integer> foodListAndCount;

    public Order(int id,String type,String sender,String receiver,HashMap<Food,Integer> foodListAndCount)
    {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.foodListAndCount = foodListAndCount;
    }

    public int getId()
    {
        return id;
    }

    public String getType()
    {
        return type;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public String getSender()
    {
        return sender;
    }

    public HashMap<Food,Integer> getOrders()
    {
        return foodListAndCount;
    }
}
