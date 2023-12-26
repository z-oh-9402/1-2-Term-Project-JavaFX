package mobilefood.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import mobilefood.restaurant.Food;

public class FoodGridController {
    @FXML
    private Label name;

    @FXML
    private Label price;

    @FXML
    private Label category;

    @FXML
    private Label totalOrder;

    @FXML
    private AnchorPane anchor;

    public AnchorPane getAnchorPane()
    {
        return anchor;
    }

    public void setData(Food f,int flag)
    {
        name.setText(f.getName());
        price.setText(Double.toString(f.getPrice()));
        category.setText(f.getCategory());
        totalOrder.setText(Integer.toString(f.getAllTimeOrderCount()));
        if(flag == 1)
        {
            anchor.setStyle("-fx-background-color: #bba37b;-fx-background-radius: 10px;");
        }

        if(flag == 2)
        {
            anchor.setStyle("-fx-background-color: #FFC1C7;-fx-background-radius: 10px;");
        }
    }
}
