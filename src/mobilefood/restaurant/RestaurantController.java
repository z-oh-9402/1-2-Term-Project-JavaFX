package mobilefood.restaurant;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RestaurantController {

    private RestaurantManager manager;
    private RestaurantMain main;

    @FXML
    private Label name;

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

    @FXML
    private Button seeOrder;

    @FXML
    private Button addFood;

    public void init(RestaurantManager manage,RestaurantMain main) {
        this.manager = manage;
        this.main = main;
        Restaurant restaurant = manager.getRestaurant();
        name.setText(restaurant.getName());
        score.setText(Double.toString(restaurant.getScore()));
        price.setText(restaurant.getPrice());
        zipcode.setText(restaurant.getZipcode());
        String[] cat = restaurant.getCategory();
        category1.setText(cat[0]);
        category2.setText(cat[1]);
        category3.setText(cat[2]);
    }

    @FXML
    void foodAdding()
    {
        try{
            main.showAddFoodPage();
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
            main.showMenuPage();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void showingOrders()
    {
        try{
            main.showOrderPage();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void showChat()
    {
        try{
            main.showChatListPage("");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void logoutAction(ActionEvent event) {
        try {
            manager.getSocketWrapper().closeConnection();
            main.showLoginPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
