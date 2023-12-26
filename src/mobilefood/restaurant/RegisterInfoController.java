package mobilefood.restaurant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import networking.ReadThreadRestaurant;
import networking.SocketWrapper;

public class RegisterInfoController {
    private RestaurantMain main;
    private String restaurantName;

    @FXML
    private TextField scoreText;

    @FXML
    private TextField priceText;

    @FXML
    private TextField zipcodeText;

    @FXML
    private TextField category1Text;

    @FXML
    private TextField category2Text;

    @FXML
    private TextField category3Text;

    @FXML
    private Label alertText;

    @FXML
    void registerAction(ActionEvent event)
    {
        if(!scoreText.getText().isEmpty() && !priceText.getText().isEmpty() && !zipcodeText.getText().isEmpty() && !category1Text.getText().isEmpty())
        {
            if(scoreText.getText().matches("^[0-9]*\\.?[0-9]+$") && priceText.getText().matches("^\\$+$"))
            {
                try{
                    SocketWrapper socketWrapper = new SocketWrapper("127.0.0.1", 12345);
                    socketWrapper.write("restaurantRegisterInfo");
                    int id = (Integer)socketWrapper.read();
                    String category[] = {category1Text.getText(),category2Text.getText(),category3Text.getText()};
                    Restaurant r = new Restaurant(id, restaurantName, Double.parseDouble(scoreText.getText()), priceText.getText(), zipcodeText.getText(), category);
                    socketWrapper.write(r);
                    RestaurantManager manager = new RestaurantManager(r, socketWrapper);
                    main.setRestaurantManager(manager);
                    new ReadThreadRestaurant(manager,main);
                    main.showHomePage();
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            else
            {
                main.showAlert(2);
            }
        }
        else
        {
            new Thread(this::blinking,"T").start();
        }
    }

    @FXML
    void resetAction(ActionEvent event) {
        scoreText.setText("");
        priceText.setText("");
        zipcodeText.setText("");
        category1Text.setText("");
        category2Text.setText("");
        category3Text.setText("");
    }

    private void blinking()
    {
        alertText.setOpacity(1);
        try{
            Thread.sleep(1500);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        alertText.setOpacity(0);
    }

    public void setMain(RestaurantMain main)
    {
        this.main = main;
    }

    public void setRestaurantName(String restaurantName)
    {
        this.restaurantName = restaurantName;
    }
}
