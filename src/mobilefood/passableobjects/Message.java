package mobilefood.passableobjects;

import java.io.Serializable;

public class Message implements Serializable {
    private String customerName;
    private String restaurantName;
    private String sender;
    private String message;

    public Message(String customerName,String restaurantName,String sender,String message)
    {
        this.customerName = customerName;
        this.restaurantName = restaurantName;
        this.sender = sender;
        this.message = message;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public String getRestaurantName()
    {
        return restaurantName;
    }

    public String getSender()
    {
        return sender;
    }

    public String getMessage()
    {
        return message;
    }
}
