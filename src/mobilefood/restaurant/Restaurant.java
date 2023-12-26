package mobilefood.restaurant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant implements Serializable{
    private int id;
    private String name;
    private double score;
    private String price;
    private String zipcode;
    private String[] category;
    private Map<String,List<Food>> menu;
    private double maxPriceOfFood;

    public Restaurant(int id,String name,double score,String price,String zipcode,String[] category)
    {
        this.id = id;
        this.name = name;
        this.score = score;
        this.price = price;
        this.zipcode = zipcode;
        this.category = category;
        menu = new HashMap<>();
        maxPriceOfFood = 0.0;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public double getScore()
    {
        return score;
    }

    public String getPrice()
    {
        return price;
    }

    public String getZipcode()
    {
        return zipcode;
    }

    public String[] getCategory()
    {
        return category;
    }

    public Map<String,List<Food>> getMenu()
    {
        return menu;
    }

    public double getMaxPrice()
    {
        return maxPriceOfFood;
    }

    public boolean isFoodAdded(String category, String name)
    {
        for(String cat : menu.keySet())
        {
            if(cat.toLowerCase().contains(category.toLowerCase()))
            {
                for(Food f : menu.get(cat))
                {
                    if(f.getName().equalsIgnoreCase(name))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int foodCount()
    {
        return menu.size();
    }

    public void addFood(Food food)
    {
        if(menu.containsKey(food.getCategory()))
        {
            menu.get(food.getCategory()).add(food);
        }
        else
        {
            List<Food> foodList = new ArrayList<>();
            foodList.add(food);
            menu.put(food.getCategory(),foodList);
        }
        if(maxPriceOfFood<food.getPrice())
        {
            maxPriceOfFood = food.getPrice();
        }
    }

    public List<String> getMenuStringInfo()
    {
        List<String> menuInfo = new ArrayList<>();
        for(String category : menu.keySet())
        {
            for(Food f : menu.get(category))
            {
                menuInfo.add(f.getStringInfo());
            }
        }
        
        return menuInfo;
    }

    public String getStringInfo()
    {
        return new String(id+","+name+","+score+","+price+","+zipcode+","+category[0]+","+category[1]+","+category[2]);
    }

    public String toString()
    {
        String info = "ID: "+id+"\nName: "+name+"\nScore: "+score+"\nPrice: "+price+"\nZip Code: "+zipcode+"\nCategory: "+category[0];
        for(int i=1;i<3;i++)
        {
            if(!category[i].equals(""))
            {
                info += ","+category[i];
            }
        }
        return info;
    }
}