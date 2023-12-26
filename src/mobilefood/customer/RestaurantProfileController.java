package mobilefood.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mobilefood.restaurant.Restaurant;

public class RestaurantProfileController {
    private Restaurant restaurant;
    private CustomerMain main;

    @FXML
    private Label restaurantName; 

    @FXML
    private Label score;

    @FXML
    private Label price;

    @FXML
    private Label zipcode;

    @FXML
    private Label category1;

    @FXML
    private Label category2;

    @FXML
    private Label category3;

    void init(Restaurant restaurant,CustomerMain main)
    {
        this.restaurant = restaurant;
        this.main = main;
        restaurantName.setText(restaurant.getName());
        score.setText(Double.toString(restaurant.getScore()));
        price.setText(restaurant.getPrice());
        zipcode.setText(restaurant.getZipcode());
        String[] cat = restaurant.getCategory();
        category1.setText(cat[0]);
        category2.setText(cat[1]);
        category3.setText(cat[2]);
    }

    @FXML
    void foodSearching()
    {
        try{
            main.showFoodSearchingInARestaurantPage(restaurant,main.getPage());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void showCartPage()
    {
        try{
            main.showCartPage(restaurant);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void showMenu()
    {
        try{
            main.showMenuPage(restaurant);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void sendingMessage()
    {
        try{
            main.showTextingPage(restaurant);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void goToLogin()
    {
        try{
            main.showRestaurantSearchingPage("Restaurant search");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
