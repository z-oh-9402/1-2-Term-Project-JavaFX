package mobilefood;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.util.Pair;
import mobilefood.passableobjects.Message;
import mobilefood.passableobjects.Order;
import mobilefood.restaurant.Food;
import mobilefood.restaurant.Restaurant;
import networking.SocketWrapper;
import networking.ReadThreadServer;

public class Server {
    private Vector<Restaurant> restaurants;
    private ServerSocket serverSocket;
    private SocketWrapper socketWrapper;
    private HashMap<String,String> customerPass;
    private HashMap<String,String> restaurantPass;
    private HashMap<String,Integer> restaurantNameToId;
    private HashMap<String,List<SocketWrapper>> customerMap;
    private HashMap<String,List<SocketWrapper>> restaurantMap;
    private HashMap<String,HashMap<String,List<Pair<String,String>>>> chatListCustomer;
    private HashMap<String,HashMap<String,List<Pair<String,String>>>> chatListRestaurant;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> addedToCart;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> pendingOrderCustomer;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> pendingOrderRestaurant;
    private HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> confirmedOrder;

    Server() {
        restaurants = new Vector<>();
        customerMap = new HashMap<>();
        restaurantMap = new HashMap<>();
        customerPass = new HashMap<>();
        restaurantPass = new HashMap<>();
        restaurantNameToId = new HashMap<>();
        chatListCustomer = new HashMap<>();
        chatListRestaurant = new HashMap<>();
        addedToCart = new HashMap<>();
        pendingOrderCustomer = new HashMap<>();
        pendingOrderRestaurant = new HashMap<>();
        confirmedOrder = new HashMap<>();

        new Thread(this::fileReading3,"T3").start();
        fileReading1();
        new Thread(this::fileReading2,"T2").start();

        try {
            serverSocket = new ServerSocket(12345);
            while (true) {
                socketWrapper = new SocketWrapper(serverSocket.accept());
                serve();
            }
        } 
        catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public HashMap<String,List<SocketWrapper>> getCustomerMap()
    {
        return customerMap;
    }

    public HashMap<String,List<SocketWrapper>> getRestaurantMap()
    {
        return restaurantMap;
    }

    public HashMap<String,HashMap<String,List<Pair<String,String>>>> getChatListCustomer()
    {
        return chatListCustomer;
    }

    public HashMap<String,HashMap<String,List<Pair<String,String>>>> getChatListRestaurant()
    {
        return chatListRestaurant;
    }
    
    public HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> getAddedToCart()
    {
        return addedToCart;
    }

    public HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> getPendingOrderCustomer()
    {
        return pendingOrderCustomer;
    }

    public HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> getPendingOrderRestaurant()
    {
        return pendingOrderRestaurant;
    }

    public HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> getConfirmedOrder()
    {
        return confirmedOrder;
    }

    private void fileReading1()
    {
        try{
            StringCorrector strc = new StringCorrector();
            BufferedReader br = new BufferedReader(new FileReader("restaurant.txt"));
            while (true) 
            {
                String line = br.readLine();
                if (line == null) break;
                String [] array = line.split(",", -1);
                String [] restaurantInfo = strc.inputDivider(array, 9);
                String[] category = new String[3];
                for(int i=0;i<3;i++)
                {
                    category[i] = restaurantInfo[i+5];
                }
                restaurantPass.put(restaurantInfo[1],restaurantInfo[8]);
                restaurants.add(new Restaurant(Integer.parseInt(restaurantInfo[0]), restaurantInfo[1], Double.parseDouble(restaurantInfo[2]), restaurantInfo[3], restaurantInfo[4],category));
                restaurantNameToId.put(restaurantInfo[1],Integer.parseInt(restaurantInfo[0]));
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private void fileReading2()
    {
        try{
            StringCorrector strc = new StringCorrector();
            FileReader file = new FileReader("menu.txt");
            BufferedReader br = new BufferedReader(file);

            while (true) 
            {
                String line = br.readLine();
                if (line == null) break;
                String [] array = line.split(",", -1);
                String[] food = strc.inputDivider(array, 4);
                restaurants.get(Integer.parseInt(food[0])-1).addFood(new Food(Integer.parseInt(food[0]),food[1],food[2],Double.parseDouble(food[3])));
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println("File not found.");
            System.out.println(e);
        }
    }

    private void fileReading3()
    {
        try{
            StringCorrector strc = new StringCorrector();
            BufferedReader br = new BufferedReader(new FileReader("customer.txt"));
            while (true) 
            {
                String line = br.readLine();
                if (line == null) break;
                String [] array = line.split(",", -1);
                String[] info = strc.inputDivider(array, 2);
                customerPass.put(info[0], info[1]);
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private void customerRestaurantDataUpdate()
    {
        for(String name : customerMap.keySet())
        {
            List<SocketWrapper> sockets = customerMap.get(name);
            try{
                for(SocketWrapper socket : sockets)
                {
                    socket.write(restaurants.get(restaurants.size()-1));
                }
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }

    private void orderDataPass(HashMap<String,HashMap<String,HashMap<Integer,HashMap<Food,Integer>>>> orderData, String name, String orderType, SocketWrapper socket)
    {
        if(orderData.containsKey(name))
        {
            for(Map.Entry<String, HashMap<Integer,HashMap<Food,Integer>>> m : orderData.get(name).entrySet())
            {
                if(!m.getValue().isEmpty())
                {
                    for(Map.Entry<Integer,HashMap<Food,Integer>> n : m.getValue().entrySet())
                    {
                        Order order = new Order(n.getKey(),orderType,name,m.getKey(),n.getValue());
                        try{
                            socket.write(order);
                        }
                        catch(Exception e)
                        {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }

    private void serve() throws IOException, ClassNotFoundException {
        String workType = (String) socketWrapper.read();

        if(workType.equals("customerLogin"))
        {
            String username = (String) socketWrapper.read();
            String password = (String) socketWrapper.read();
            if(customerPass.containsKey(username))
            {
                if(customerPass.get(username).equals(password))
                {
                    socketWrapper.write(1);
                }
                else
                {
                    socketWrapper.write(-1);
                }
            }
            else
            {
                socketWrapper.write(0);
            }
        }

        if(workType.equals("restaurantLogin"))
        {
            String username = (String) socketWrapper.read();
            String password = (String) socketWrapper.read();
            if(restaurantPass.containsKey(username))
            {
                if(restaurantPass.get(username).equals(password))
                {
                    socketWrapper.write(1);
                    Restaurant r = restaurants.get(restaurantNameToId.get(username)-1);
                    socketWrapper.write(r);
                    if(!restaurantMap.containsKey(username))
                    {
                        restaurantMap.put(username,new ArrayList<>());
                    }
                    restaurantMap.get(username).add(socketWrapper);
                    new ReadThreadServer(this, socketWrapper);
                    new Thread(()->{
                        try{
                            if(chatListRestaurant.containsKey(r.getName()))
                            {
                                HashMap<String,List<Pair<String,String>>> chat = chatListRestaurant.get(r.getName());
                                if(!chat.isEmpty())
                                {
                                    for(String customerName : chat.keySet())
                                    {
                                        for(Pair<String,String> sender : chat.get(customerName))
                                        {
                                            socketWrapper.write(new Message(customerName,r.getName(),sender.getKey(),sender.getValue()));
                                        }
                                    }
                                }
                            }

                            if(pendingOrderRestaurant.containsKey(r.getName()))
                            {
                                for(Map.Entry<String, HashMap<Integer,HashMap<Food,Integer>>> m : pendingOrderRestaurant.get(r.getName()).entrySet())
                                {
                                    if(!m.getValue().isEmpty())
                                    {
                                        for(Map.Entry<Integer,HashMap<Food,Integer>> n : m.getValue().entrySet())
                                        {
                                            Order order = new Order(n.getKey(),"orderToBeConfirmed",m.getKey(),r.getName(),n.getValue());
                                            socketWrapper.write(order);
                                        }
                                    }
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            System.out.println(e);
                        }
                    }).start();
                }
                else
                {
                    socketWrapper.write(-1);
                }
            }
            else
            {
                socketWrapper.write(0);
            }
        }

        if(workType.equals("customerRegister"))
        {
            String username = (String) socketWrapper.read();
            String password = (String) socketWrapper.read();
            if(customerPass.containsKey(username))
            {
                socketWrapper.write(false);
            }
            else
            {
                customerPass.put(username,password);
                BufferedWriter bw = new BufferedWriter(new FileWriter("customer.txt",true));
                bw.write(username+","+password);
                bw.write(System.lineSeparator());
                bw.close();
                socketWrapper.write(true);
            }
        }

        if(workType.equals("restaurantRegister"))
        {
            String username = (String) socketWrapper.read();
            String password = (String) socketWrapper.read();
            if(restaurantPass.containsKey(username))
            {
                socketWrapper.write(false);
            }
            else
            {
                restaurantPass.put(username,password);
                socketWrapper.write(true);
            }
        }

        if(workType.equals("restaurantRegisterInfo"))
        {
            socketWrapper.write(restaurants.size()+1);
            Restaurant r = (Restaurant)socketWrapper.read();
            restaurants.add(r);
            BufferedWriter bw = new BufferedWriter(new FileWriter("restaurant.txt",true));
            restaurantNameToId.put(r.getName(),r.getId());
            if(!restaurantMap.containsKey(r.getName()))
            {
                restaurantMap.put(r.getName(),new ArrayList<>());
            }
            restaurantMap.get(r.getName()).add(socketWrapper);
            bw.write(r.getStringInfo()+","+restaurantPass.get(r.getName()));
            bw.write(System.lineSeparator());
            bw.close();
            new Thread(this::customerRestaurantDataUpdate,"T0").start();
            new ReadThreadServer(this, socketWrapper);
        }

        if(workType.equals("customerData"))
        {
            String clientName = (String) socketWrapper.read();
            if(!customerMap.containsKey(clientName))
            {
                customerMap.put(clientName,new ArrayList<>());
            }
            customerMap.get(clientName).add(socketWrapper);
            new ReadThreadServer(this, socketWrapper);
            new Thread(()->{
                try{
                    for(Restaurant r : restaurants)
                    {
                        socketWrapper.write(r);
                    }
                    if(chatListCustomer.containsKey(clientName))
                    {
                        HashMap<String,List<Pair<String,String>>> chat = chatListCustomer.get(clientName);
                        if(!chat.isEmpty())
                        {
                            for(String restaurantName : chat.keySet())
                            {
                                for(Pair<String,String> sender : chat.get(restaurantName))
                                {
                                    socketWrapper.write(new Message(clientName,restaurantName,sender.getKey(),sender.getValue()));
                                }
                            }
                        }
                    }

                    orderDataPass(addedToCart, clientName, "addToCart", socketWrapper);
                    orderDataPass(pendingOrderCustomer, clientName, "addToPendingCustomer", socketWrapper);
                    orderDataPass(confirmedOrder, clientName, "addToConfirmed", socketWrapper);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }).start();
        }

        if(workType.equals("addFood"))
        {
            Food obj = (Food) socketWrapper.read();
            restaurants.get(obj.getRestaurantId()-1).addFood(obj);
            new Thread(() -> {
                for(String name : customerMap.keySet())
                {
                    List<SocketWrapper> sockets = customerMap.get(name);
                    try{
                        for(SocketWrapper socket : sockets)
                        {
                            socket.write(obj);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
            }).start();
            BufferedWriter bw = new BufferedWriter(new FileWriter("menu.txt",true));
            bw.write(obj.getStringInfo());
            bw.write(System.lineSeparator());
            bw.close();
        }
    }

    public static void main(String args[]) {
        new Server();
    }
}