package mobilefood.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.util.Pair;
import mobilefood.restaurant.Food;
import mobilefood.restaurant.Restaurant;
import networking.SocketWrapper;

public class CustomerManager {
    private String customerName;
    private SocketWrapper socketWrapper;
    private Vector<Restaurant> restaurants;
    private Map<String,List<Restaurant>> categoryToRestaurant;
    private HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> addedToCart;
    private HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> pendingOrder;
    private HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> confirmedOrder;
    private HashMap<String,List<Pair<String,String>>> chatList;

    public CustomerManager(String customerName,SocketWrapper socketWrapper)
    {
        this.socketWrapper = socketWrapper;
        this.customerName = customerName;
        restaurants = new Vector<>();
        addedToCart = new HashMap<>();
        pendingOrder = new HashMap<>();
        confirmedOrder = new HashMap<>();
        categoryToRestaurant = new HashMap<>();
        chatList = new HashMap<>();
    }

    public void restaurantLoading(Restaurant r)
    {
        restaurants.add(r);
        String[] categories = r.getCategory();
        for(int i=0;i<3;i++)
        {
            if(!categories[i].isEmpty())
            {
                List<Restaurant> buffer = new ArrayList<>();
                if(categoryToRestaurant.containsKey(categories[i]))
                {
                    buffer = categoryToRestaurant.get(categories[i]);
                }
                buffer.add(r);
                categoryToRestaurant.put(categories[i],buffer);
            }
        }
    }

    public boolean isFoodAdded(int restaurantId,String category,String name)
    {
        return restaurants.get(restaurantId-1).isFoodAdded(category,name);
    }

    public void addFood(Food f)
    {
        restaurants.get(f.getRestaurantId()-1).addFood(f);
    }

    public void addChat(String restaurantName,String sender,String message)
    {
        if(chatList.containsKey(restaurantName))
        {
            chatList.get(restaurantName).add(new Pair<>(sender, message));
        }
        else
        {
            List<Pair<String,String>> list = new ArrayList<>();
            list.add(new Pair<>(sender,message));
            chatList.put(restaurantName,list);
        }
    }

    public HashMap<String,List<Pair<String,String>>> getChatList()
    {
        return chatList;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public SocketWrapper getSocketWrapper()
    {
        return socketWrapper;
    }

    public int getRestaurantIdByName(String name)
    {
        int id = -1;
        for(Restaurant r : restaurants)
        {
            if(r.getName().equalsIgnoreCase(name))
            {
                id = r.getId();
            }
        }
        return id;
    }

    public Restaurant getRestaurantById(int id)
    {
        return restaurants.get(id-1);
    }

    public String getRestaurantNameById(int id)
    {
        return restaurants.get(id-1).getName();
    }

    public HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> getAddedToCart()
    {
        return addedToCart;
    }

    public HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> getPendingOrder()
    {
        return pendingOrder;
    }

    public HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> getConfirmedOrder()
    {
        return confirmedOrder;
    }

    public List<Restaurant> searchRestaurantByName(String name)
    {
        List<Restaurant> R = new ArrayList<>();
        for(Restaurant r : restaurants)
        {
            if(r.getName().toLowerCase().contains(name.toLowerCase()))
            {
                R.add(r);
            }
        }
        return R;
    }

    public List<Restaurant> searchRestaurantByScore(double lowerScore,double upperScore)
    {
        List<Restaurant> R = new ArrayList<>();
        for(Restaurant r : restaurants)
        {
            if(r.getScore()>=lowerScore && r.getScore()<=upperScore)
            {
                R.add(r);
            }
        }
        return R;
    }

    public List<Restaurant> searchRestaurantByCategory(String category)
    {
        List<Restaurant> R = new ArrayList<>();
        for(Map.Entry<String,List<Restaurant>> c : categoryToRestaurant.entrySet())
        {
            if(c.getKey().toLowerCase().contains(category.toLowerCase()))
            {
                List<Restaurant> buffer = c.getValue();
                R.addAll(buffer);
            }
        }
        return R;
    }

    public List<Restaurant> searchRestaurantByPrice(String price)
    {
        List<Restaurant> R = new ArrayList<>();
        for(Restaurant r : restaurants)
        {
            if(r.getPrice().equals(price))
            {
                R.add(r);
            }
        }
        return R;
    }

    public List<Restaurant> searchRestaurantByZipcode(String zipcode)
    {
        List<Restaurant> R = new ArrayList<>();
        for(Restaurant r : restaurants)
        {
            if(r.getZipcode().equals(zipcode))
            {
                R.add(r);
            }
        }
        return R;
    }

    public Map<String,List<Restaurant>> categoryWiseRestaurantList()
    {
        return categoryToRestaurant;
    }

    public Map<Integer,List<Food>> searchFoodByName(String name)
    {
        Map<Integer,List<Food>> F = new HashMap<>();
        for(int i=0;i<restaurants.size();i++)
        {
            Map<String,List<Food>> food = restaurants.get(i).getMenu();
            List<Food> menu = new ArrayList<>();
            for(Map.Entry<String,List<Food>> category : food.entrySet())
            {
                for(Food f : category.getValue())
                {
                    if(f.getName().toLowerCase().contains(name.toLowerCase()))
                    {
                        menu.add(f);
                    }
                }
            }
            
            if(menu.size()!=0)
            {
                Collections.sort(menu,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(),food1.getAllTimeOrderCount()));
                F.put(i+1,menu);
            }
        }
        return F;
    }

    public List<Food> searchFoodByNameInGivenRestaurant(String name,Restaurant restaurant)
    {
        Map<String,List<Food>> F = restaurant.getMenu();
        List<Food> menu = new ArrayList<>();
        for(Map.Entry<String,List<Food>> category : F.entrySet())
        {
            for(Food f : category.getValue())
            {
                if(f.getName().toLowerCase().contains(name.toLowerCase()))
                {
                    menu.add(f);
                }
            }
        }
        if(menu.size()!=0)
        {
            Collections.sort(menu,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(),food1.getAllTimeOrderCount()));
        }
        return menu;
    }

    public Map<Integer,List<Food>> searchFoodByCategory(String category)
    {
        Map<Integer,List<Food>> F = new HashMap<>();
        for(int i=0;i<restaurants.size();i++)
        {
            Map<String,List<Food>> food = restaurants.get(i).getMenu();
            List<Food> menu = new ArrayList<>();
            for(Map.Entry<String,List<Food>> cat : food.entrySet())
            {
                if(cat.getKey().toLowerCase().contains(category.toLowerCase()))
                {
                    menu.addAll(cat.getValue());
                }
            }
            if(menu.size()!=0)
            {
                Collections.sort(menu,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(),food1.getAllTimeOrderCount()));
                F.put(i+1,menu);
            }
        }
        return F;
    }

    public List<Food> searchFoodByCategoryInGivenRestaurant(String category,Restaurant restaurant)
    {
        Map<String,List<Food>> F = restaurant.getMenu();
        List<Food> menu = new ArrayList<>();
        for(Map.Entry<String,List<Food>> cat : F.entrySet())
        {
            if(cat.getKey().toLowerCase().contains(category.toLowerCase()))
            {
                for(Food f : cat.getValue())
                {
                    if(f.getCategory().toLowerCase().contains(category.toLowerCase()))
                    {
                        menu.add(f);
                    }
                }
            }
        }
        if(menu.size()!=0)
        {
            Collections.sort(menu,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(),food1.getAllTimeOrderCount()));
        }
        return menu;
    }

    public Map<Integer,List<Food>> searchFoodByPriceRange(double lowerPrice,double upperPrice)
    {
        Map<Integer,List<Food>> F = new HashMap<>();
        for(int i=0;i<restaurants.size();i++)
        {
            Map<String,List<Food>> food = restaurants.get(i).getMenu();
            List<Food> menu = new ArrayList<>();
            for(Map.Entry<String,List<Food>> category : food.entrySet())
            {   
                for(Food f : category.getValue())
                {
                    if(f.getPrice()>=lowerPrice && f.getPrice()<=upperPrice)
                    {
                        menu.add(f);
                    }
                }
            }
            
            if(menu.size()!=0)
            {
                Collections.sort(menu,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(),food1.getAllTimeOrderCount()));
                F.put(i+1,menu);
            }
        }
        return F;
    }

    public List<Food> searchFoodByPriceRangeInGivenRestaurant(double lowerPrice,double upperPrice,Restaurant restaurant)
    {
        Map<String,List<Food>> F = restaurant.getMenu();
        List<Food> menu = new ArrayList<>();
        for(Map.Entry<String,List<Food>> category : F.entrySet())
        {
            for(Food f : category.getValue())
            {
                if(f.getPrice()>=lowerPrice && f.getPrice()<=upperPrice)
                {
                    menu.add(f);
                }
            }
        }
        if(menu.size()!=0)
        {
            Collections.sort(menu,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(),food1.getAllTimeOrderCount()));
        }
        return menu;
    }

    public List<Food> costliestFoodInGivenRestaurant(String restaurantName)
    {
        Restaurant r = restaurants.get(getRestaurantIdByName(restaurantName)-1);
        Map<String,List<Food>> F = r.getMenu();
        double maxPrice = r.getMaxPrice();
        List<Food> food = new ArrayList<>();
        for(Map.Entry<String,List<Food>> category : F.entrySet())
        {
            for(Food f : category.getValue())
            {
                if(maxPrice == f.getPrice())
                {
                    food.add(f);
                }
            }
        }
        if(food.size()!=0)
        {
            Collections.sort(food,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(),food1.getAllTimeOrderCount()));
        }
        return food;
    }

    public List<String> totalFoodInAllRestaurant()
    {
        List<String> print = new ArrayList<>(); 
        for(Restaurant r : restaurants)
        {
            print.add(r.getName()+": "+r.foodCount());
        }
        return print;
    }
}
