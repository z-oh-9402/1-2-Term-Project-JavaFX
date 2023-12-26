package mobilefood.customer;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mobilefood.restaurant.Food;

public class FoodOrderGridController {
    @FXML
    private Label name;

    @FXML
    private Label price;

    @FXML
    private Label category;

    @FXML
    private Label totalOrder;

    @FXML
    private ImageView imageView;

    @FXML
    private Label count;

    @FXML
    private AnchorPane anchor;

    public AnchorPane getAnchorPane()
    {
        return anchor;
    }

    public void setData(Food F,HashMap<Food,Integer> foodCount)
    {
        name.setText(F.getName());
        price.setText(Double.toString(F.getPrice()));
        category.setText(F.getCategory());
        totalOrder.setText(Integer.toString(F.getAllTimeOrderCount()));

        if(!foodCount.containsKey(F)){
            count.setVisible(false);
        }
        else{
            count.setVisible(true);
            count.setText(Integer.toString(foodCount.get(F)));
        }

        imageView.setOnMouseClicked(e ->{
            if(foodCount.containsKey(F))
            {
                foodCount.put(F,foodCount.get(F)+1);
            }
            else
            {
                foodCount.put(F,1);
            }
            count.setVisible(true);
            count.setText(Integer.toString(foodCount.get(F)));
        });
    }
}
