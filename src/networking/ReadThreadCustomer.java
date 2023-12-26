package networking;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Platform;
import mobilefood.customer.CustomerMain;
import mobilefood.customer.CustomerManager;
import mobilefood.passableobjects.Message;
import mobilefood.passableobjects.Order;
import mobilefood.restaurant.Food;
import mobilefood.restaurant.Restaurant;

public class ReadThreadCustomer implements Runnable {
    private Thread thr;
    private CustomerManager manager;
    private CustomerMain main;

    public ReadThreadCustomer(CustomerManager manager,CustomerMain main) {
        this.manager = manager;
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while(true)
            {
                Object o = manager.getSocketWrapper().read();
                if (o instanceof Order) {
                    Order obj = (Order) o;

                    if(obj.getType().equals("addToCart"))
                    {
                        addData(manager.getAddedToCart(), obj.getOrders(), obj.getReceiver());
                    }

                    if(obj.getType().equals("addToPendingCustomer"))
                    {
                        addData(manager.getPendingOrder(), obj.getOrders(), obj.getReceiver());
                    }

                    if(obj.getType().equals("addToConfirmed"))
                    {
                        addData(manager.getConfirmedOrder(), obj.getOrders(), obj.getReceiver());
                    }

                    if(obj.getType().equals("orderToBeConfirmed") || obj.getType().equals("orderToBeCancelled"))
                    {
                        deleteData(manager.getAddedToCart(),obj.getId(),obj.getReceiver());

                        if(obj.getType().equals("orderToBeConfirmed"))
                        {
                            addData(manager.getPendingOrder(), obj.getOrders(), obj.getReceiver());
                        }
                    }

                    if(obj.getType().equals("orderToBeDeleted"))
                    {
                        Food food = null;
                        for(Food F : manager.getAddedToCart().get(obj.getReceiver()).get(obj.getId()).keySet())
                        {
                            for(Food f : obj.getOrders().keySet())
                            {
                                if(F.equals(f))
                                {
                                    food = F;
                                }
                            }
                        }
                        
                        manager.getAddedToCart().get(obj.getReceiver()).get(obj.getId()).remove(food);
                        HashMap<Integer,HashMap<Food,Integer>> foodCountMap = manager.getAddedToCart().get(obj.getReceiver());
                        if(foodCountMap.get(obj.getId()).isEmpty())
                        {
                            deleteData(manager.getAddedToCart(), obj.getId(), obj.getReceiver());
                        }
                    }

                    if(obj.getType().equals("confirmedOrder"))
                    {
                        addData(manager.getConfirmedOrder(), obj.getOrders(), obj.getReceiver());
                        deleteData(manager.getPendingOrder(), obj.getId(), obj.getReceiver());
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (main.getPage().equals("Cart"))
                            {
                                try {
                                    main.showCartPage(manager.getRestaurantById(manager.getRestaurantIdByName(obj.getReceiver())));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (main.getPage().equals("Order1")) {
                                try {
                                    main.showOrderPage(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (main.getPage().equals("Order2")) {
                                try {
                                    main.showOrderPage(2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (main.getPage().equals("Order3")) {
                                try {
                                    main.showOrderPage(3);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                if(o instanceof Restaurant)
                {
                    manager.restaurantLoading((Restaurant)o);
                }
                if(o instanceof Food)
                {
                    manager.addFood((Food)o);
                }
                if(o instanceof Message)
                {
                    Message obj = (Message) o;
                    manager.addChat(obj.getRestaurantName(), obj.getSender(), obj.getMessage());
                    Restaurant restaurant = manager.getRestaurantById(manager.getRestaurantIdByName(obj.getRestaurantName()));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (main.getPage().equals("Texting")) {
                                try {
                                    main.showTextingPage(restaurant);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if(main.getPage().equals("Chat List")) {
                                try {
                                    main.showChatPage(main.getRestaurantName());;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                manager.getSocketWrapper().closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addData(HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> list, HashMap<Food,Integer> order, String key)
    {
        if(!list.containsKey(key))
        {
            list.put(key, new HashMap<>());
        }
        HashMap<Integer,HashMap<Food,Integer>> map = list.get(key);
        int index = map.size()+1;
        map.put(index,order);
    }

    private void deleteData(HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> list, int id, String key)
    {
        if(!list.containsKey(key))
        {
            return;
        }
        for(int index : list.get(key).keySet())
        {
            if(index>id)
            {
                list.get(key).put(index-1,list.get(key).get(index));
            }
        }
        list.get(key).remove(list.get(key).size());
    }
}
