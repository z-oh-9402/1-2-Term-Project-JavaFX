package mobilefood.restaurant;

import java.io.Serializable;

public class Food  implements Serializable{
    private int restaurantId;
    private String category;
    private String name;
    private double price;
    private int allTimeOrderCount;

    public Food(int restaurantId, String category, String name, double price)
    {
        this.restaurantId = restaurantId;
        this.category = category;
        this.name = name;
        this.price = price;
        allTimeOrderCount = 0;
    }

    public int getRestaurantId()
    {
        return restaurantId;
    }

    public String getCategory()
    {
        return category;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public String getStringInfo()
    {
        return new String(restaurantId+","+category+","+name+","+price);
    }

    public int getAllTimeOrderCount()
    {
        return allTimeOrderCount;
    }

    public void setAllTimeOrderCount(int count)
    {
        allTimeOrderCount = count;
    }

    public boolean equals(Food obj)
    {
        return (obj.restaurantId == restaurantId && obj.getName().equals(name) && obj.getPrice()==price && obj.getCategory().equals(category));
    }

    public String toString()
    {
        String info = "Name: "+name+"\nCategory: "+category+"\nPrice: "+price;
        return info;
    }
}