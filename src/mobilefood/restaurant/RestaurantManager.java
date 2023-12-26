package mobilefood.restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import networking.SocketWrapper;

public class RestaurantManager {
    private Restaurant restaurant;
    private HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> orders;
    private SocketWrapper socketWrapper;
    private HashMap<String,List<Pair<String,String>>> chatList;

    RestaurantManager(Restaurant restaurant,SocketWrapper socketWrapper)
    {
        this.restaurant = restaurant;
        this.socketWrapper = socketWrapper;
        orders = new HashMap<>();
        chatList = new HashMap<>();
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }

    public HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> getOrders()
    {
        return orders;
    }

    public SocketWrapper getSocketWrapper()
    {
        return socketWrapper;
    }

    public HashMap<String,List<Pair<String,String>>> getchatList()
    {
        return chatList;
    }

    public void addChat(String customerName,String sender,String message)
    {
        if(chatList.containsKey(customerName))
        {
            chatList.get(customerName).add(new Pair<>(sender, message));
        }
        else
        {
            List<Pair<String,String>> list = new ArrayList<>();
            list.add(new Pair<>(sender,message));
            chatList.put(customerName,list);
        }
    }

    public void increaseOrderCount(Food F,int c)
    {
        List<Food> list = restaurant.getMenu().get(F.getCategory());
        for(Food item : list)
        {
            if(item.equals(F))
            {
                int count = item.getAllTimeOrderCount();
                item.setAllTimeOrderCount(count+c);
                break;
            }
        }
    }
}
