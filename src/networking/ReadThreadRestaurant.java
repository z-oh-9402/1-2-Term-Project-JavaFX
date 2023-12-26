package networking;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Platform;
import mobilefood.passableobjects.Message;
import mobilefood.passableobjects.Order;
import mobilefood.restaurant.Food;
import mobilefood.restaurant.RestaurantMain;
import mobilefood.restaurant.RestaurantManager;

public class ReadThreadRestaurant implements Runnable {
    private Thread thr;
    private RestaurantManager manager;
    private RestaurantMain main;

    public ReadThreadRestaurant(RestaurantManager manager,RestaurantMain main) {
        this.main = main;
        this.manager = manager;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = manager.getSocketWrapper().read();
                if (o instanceof Message) {
                    Message obj = (Message) o;
                    manager.addChat(obj.getCustomerName(), obj.getSender(), obj.getMessage());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(main.getPage().equals("Chat List")) {
                                try {
                                    main.showChatListPage(main.getCustomerName());;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                if (o instanceof Order) {
                    Order obj = (Order) o;

                    if(obj.getType().equals("orderToBeConfirmed"))
                    {
                        if(!manager.getOrders().containsKey(obj.getSender()))
                        {
                            manager.getOrders().put(obj.getSender(),new HashMap<>());
                        }
                        HashMap<Integer,HashMap<Food,Integer>> map = manager.getOrders().get(obj.getSender());
                        int index = map.size()+1;
                        map.put(index,obj.getOrders());
                    }

                    if(obj.getType().equals("confirmedOrder"))
                    {
                        if(manager.getOrders().containsKey(obj.getSender()))
                        {
                            for(int index : manager.getOrders().get(obj.getSender()).keySet())
                            {
                                if(index>obj.getId())
                                {
                                    manager.getOrders().get(obj.getSender()).put(index-1,manager.getOrders().get(obj.getSender()).get(index));
                                }
                            }
                            manager.getOrders().get(obj.getSender()).remove(manager.getOrders().get(obj.getSender()).size());
                        }
                    }
                    // manager.addOrder(obj);
                    // new Thread(()->{
                    //     HashMap<Food,Integer> foodCount = obj.getOrders();
                    //     for(Map.Entry<Food,Integer> mp : foodCount.entrySet())
                    //     {
                    //         manager.increaseOrderCount(mp.getKey(), mp.getValue());
                    //     }
                    // }).start();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (main.getPage().equals("Show Order")) {
                                try {
                                    main.showOrderPage();
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
}
