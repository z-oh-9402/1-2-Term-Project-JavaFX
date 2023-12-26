package mobilefood.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CustomerController {

    private CustomerMain main;
    private CustomerManager manager;

    @FXML
    private Label message;

    @FXML
    private Button button;

    void init(CustomerManager manage,CustomerMain main) {
        this.manager = manage;
        message.setText(manager.getCustomerName());
        this.main = main;
    }

    @FXML
    void restaurantSearching()
    {
        try{
            main.showRestaurantSearchingPage("Restaurant search");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void foodSearching()
    {
        try{
            main.showFoodSearchingPage();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void placingOrder()
    {
        try{
            main.showRestaurantSearchingPage("Place Order");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void seeOrderStatus()
    {
        try{
            main.showOrderPage(1);
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
            main.showChatPage("");
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
