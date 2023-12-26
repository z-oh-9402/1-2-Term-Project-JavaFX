package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import mobilefood.Server;
import mobilefood.passableobjects.Message;
import mobilefood.passableobjects.Order;
import mobilefood.restaurant.Food;

public class ReadThreadServer implements Runnable {
    private Thread thr;
    private SocketWrapper socketWrapper;
    private HashMap<String,List<SocketWrapper>> customerMap;
    private HashMap<String,List<SocketWrapper>> restaurantMap;
    private HashMap<String,HashMap<String,List<Pair<String,String>>>> chatListCustomer;
    private HashMap<String,HashMap<String,List<Pair<String,String>>>> chatListRestaurant;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> addedToCart;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> pendingOrderCustomer;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> pendingOrderRestaurant;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> confirmedOrder;

    public ReadThreadServer(Server server,SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
        customerMap = server.getCustomerMap();
        restaurantMap = server.getRestaurantMap();
        chatListCustomer = server.getChatListCustomer();
        chatListRestaurant = server.getChatListRestaurant();
        addedToCart = server.getAddedToCart();
        pendingOrderCustomer = server.getPendingOrderCustomer();
        pendingOrderRestaurant = server.getPendingOrderRestaurant();
        confirmedOrder = server.getConfirmedOrder();
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = socketWrapper.read();
                if (o instanceof Order) {
                    Order obj = (Order) o;
                    if(obj.getType().equals("orderToBeConfirmed") || obj.getType().equals("confirmedOrder"))
                    {
                        if(restaurantMap.containsKey(obj.getReceiver()))
                        {
                            for(SocketWrapper nu : restaurantMap.get(obj.getReceiver()))
                            {
                                nu.write(obj);
                            }
                        }
                    }
                    if(customerMap.containsKey(obj.getSender()))
                    {
                        if(customerMap.containsKey(obj.getSender()))
                        {
                            for(SocketWrapper nu : customerMap.get(obj.getSender()))
                            {
                                nu.write(obj);
                            }
                        }
                        
                    }

                    if(obj.getType().equals("addToCart"))
                    {
                        addData(addedToCart, obj.getOrders(), obj.getSender(), obj.getReceiver());
                    }

                    if(obj.getType().equals("orderToBeConfirmed") || obj.getType().equals("orderToBeCancelled"))
                    {
                        deleteData(addedToCart,obj.getId(), obj.getSender(), obj.getReceiver());

                        if(obj.getType().equals("orderToBeConfirmed"))
                        {
                            addData(pendingOrderCustomer, obj.getOrders(), obj.getSender(), obj.getReceiver());
                            addData(pendingOrderRestaurant, obj.getOrders(), obj.getReceiver(), obj.getSender());
                        }
                    }

                    if(obj.getType().equals("orderToBeDeleted"))
                    {
                        Food food = null;
                        for(Food F : addedToCart.get(obj.getSender()).get(obj.getReceiver()).get(obj.getId()).keySet())
                        {
                            for(Food f : obj.getOrders().keySet())
                            {
                                if(F.equals(f))
                                {
                                    food = F;
                                }
                            }
                        }
                        
                        addedToCart.get(obj.getSender()).get(obj.getReceiver()).get(obj.getId()).remove(food);
                        HashMap<Integer,HashMap<Food,Integer>> foodCountMap = addedToCart.get(obj.getSender()).get(obj.getReceiver());
                        if(foodCountMap.get(obj.getId()).isEmpty())
                        {
                            deleteData(addedToCart, obj.getId(), obj.getSender(), obj.getReceiver());
                        }
                    }

                    if(obj.getType().equals("confirmedOrder"))
                    {
                        addData(confirmedOrder, obj.getOrders(), obj.getSender(), obj.getReceiver());
                        deleteData(pendingOrderCustomer, obj.getId(),obj.getSender(),obj.getReceiver());
                        deleteData(pendingOrderRestaurant, obj.getId(), obj.getReceiver(), obj.getSender());
                    }
                }
                if(o instanceof Message)
                {
                    Message obj = (Message) o;
                    HashMap<String,List<Pair<String,String>>> chat = new HashMap<>();
                    if(chatListCustomer.containsKey(obj.getCustomerName()))
                    {
                        chat = chatListCustomer.get(obj.getCustomerName());

                    }
                    if(chat.containsKey(obj.getRestaurantName()))
                    {
                        chat.get(obj.getRestaurantName()).add(new Pair<>(obj.getSender(),obj.getMessage()));
                    }
                    else
                    {
                        List<Pair<String,String>> list = new ArrayList<>();
                        list.add(new Pair<>(obj.getSender(),obj.getMessage()));
                        chat.put(obj.getRestaurantName(),list);
                    }
                    chatListCustomer.put(obj.getCustomerName(),chat);

                    HashMap<String,List<Pair<String,String>>> chat1 = new HashMap<>();
                    if(chatListRestaurant.containsKey(obj.getRestaurantName()))
                    {
                        chat1 = chatListRestaurant.get(obj.getRestaurantName());

                    }
                    if(chat1.containsKey(obj.getCustomerName()))
                    {
                        chat1.get(obj.getCustomerName()).add(new Pair<>(obj.getSender(),obj.getMessage()));
                    }
                    else
                    {
                        List<Pair<String,String>> list = new ArrayList<>();
                        list.add(new Pair<>(obj.getSender(),obj.getMessage()));
                        chat1.put(obj.getCustomerName(),list);
                    }
                    chatListRestaurant.put(obj.getRestaurantName(),chat1);

                    List<SocketWrapper> nu = new ArrayList<>();
                    if(obj.getSender().equals(obj.getRestaurantName()))
                    {
                        nu = customerMap.get(obj.getCustomerName());
                    }
                    if(obj.getSender().equals(obj.getCustomerName()))
                    {
                        nu = restaurantMap.get(obj.getRestaurantName());
                    }
                    for(SocketWrapper socket : nu) 
                    {
                        socket.write(obj);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                socketWrapper.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addData(HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> list, HashMap<Food,Integer> order, String firstKey, String secondKey)
    {
        if(!list.containsKey(firstKey))
        {
            list.put(firstKey, new HashMap<>());
        }
        if(!list.get(firstKey).containsKey(secondKey))
        {
            list.get(firstKey).put(secondKey, new HashMap<>());
        }
        HashMap<Integer,HashMap<Food,Integer>> map = list.get(firstKey).get(secondKey);
        int index = map.size()+1;
        map.put(index,order);
    }

    private void deleteData(HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> list, int id, String firstKey, String secondKey)
    {
        if(!list.containsKey(firstKey))
        {
            return;
        }
        if(!list.get(firstKey).containsKey(secondKey))
        {
            return;
        }
        for(int index : list.get(firstKey).get(secondKey).keySet())
        {
            if(index>id)
            {
                list.get(firstKey).get(secondKey).put(index-1,list.get(firstKey).get(secondKey).get(index));
            }
        }
        list.get(firstKey).get(secondKey).remove(list.get(firstKey).get(secondKey).size());
    }
}
