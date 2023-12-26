package mobilefood.restaurant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import networking.SocketWrapper;

public class AddFoodController {
    private RestaurantMain main;
    private RestaurantManager manager;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label alertText;

    @FXML
    private TextField foodNameText;

    @FXML
    private TextField categoryText;

    @FXML
    private TextField priceText;

    public void init(RestaurantManager manager,RestaurantMain main)
    {
        this.main = main;
        this.manager = manager;
    }

    @FXML
    void goToLogin()
    {
        try{
            main.showHomePage();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void addingFood()
    {
        if(!foodNameText.getText().isEmpty() && !categoryText.getText().isEmpty() && (!priceText.isVisible() || !priceText.getText().isEmpty()))
        {
            String foodName = foodNameText.getText();
            String category = categoryText.getText();

            if(!priceText.isVisible())
            {
                if(manager.getRestaurant().isFoodAdded(category, foodName))
                {
                    main.showAlert(0);
                }
                else
                {
                    categoryLabel.setLayoutX(261);
                    categoryLabel.setLayoutY(204);
                    categoryText.setLayoutX(367);
                    categoryText.setLayoutY(200);
                    priceText.setVisible(true);
                    priceLabel.setVisible(true);
                }
            }
            else
            {
                double price = Double.parseDouble(priceText.getText());
                Restaurant r = manager.getRestaurant();
                Food food = new Food(r.getId(), category, foodName, price);
                r.addFood(food);
                try{
                    SocketWrapper socketWrapper = new SocketWrapper("127.0.0.1", 12345);
                    socketWrapper.write("addFood");
                    socketWrapper.write(food);
                    main.showHomePage();
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }
        else
        {
            new Thread(this::blinking,"T").start();
        }
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

    @FXML
    void resetAction()
    {
        foodNameText.setText("");
        categoryText.setText("");
        if(priceText.isVisible())
        {
            priceText.setText("");
        }
    }
}
