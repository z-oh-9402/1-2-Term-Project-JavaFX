package mobilefood.customer;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mobilefood.passableobjects.Order;
import mobilefood.restaurant.Food;

public class CartOrderController {
    @FXML
    private Label name;

    @FXML
    private Label price;

    @FXML
    private Label category;

    @FXML
    private Label totalOrder;

    @FXML
    private Label orderCount;

    @FXML
    private Label totalPrice;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane anchor;

    public AnchorPane getAnchorPane()
    {
        return anchor;
    }

    void setData(Map.Entry<Food,Integer> F,HashMap<Integer,HashMap<Food,Integer>> foodCountMap,int index,String restaurantName,CustomerManager manager,OrderPageController controller)
    {
        name.setText(F.getKey().getName());
        price.setText(Double.toString(F.getKey().getPrice()));
        category.setText(F.getKey().getCategory());
        totalOrder.setText(Integer.toString(F.getKey().getAllTimeOrderCount()));
        orderCount.setText(Integer.toString(F.getValue()));
        DecimalFormat dec = new DecimalFormat("0.00");
        totalPrice.setText(dec.format(F.getValue()*F.getKey().getPrice()));

        imageView.setOnMouseClicked(e ->{
            HashMap<Food,Integer> map = new HashMap<>();
            map.put(F.getKey(),F.getValue());
            Order o = new Order(index,"orderToBeDeleted",manager.getCustomerName(),restaurantName,map);
            try{
                manager.getSocketWrapper().write(o);
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        });
    }

    public void setData(Map.Entry<Food,Integer> F,HashMap<Integer,HashMap<Food,Integer>> foodCountMap,int index,String restaurantName,CustomerManager manager)
    {

        name.setText(F.getKey().getName());
        price.setText(Double.toString(F.getKey().getPrice()));
        category.setText(F.getKey().getCategory());
        totalOrder.setText(Integer.toString(F.getKey().getAllTimeOrderCount()));
        orderCount.setText(Integer.toString(F.getValue()));
        anchor.setStyle("-fx-background-color: #3DB879;-fx-background-radius: 10px;");
        DecimalFormat dec = new DecimalFormat("0.00");
        totalPrice.setText(dec.format(F.getValue()*F.getKey().getPrice()));

        imageView.setOnMouseClicked(e ->{
            HashMap<Food,Integer> map = new HashMap<>();
            map.put(F.getKey(),F.getValue());
            Order o = new Order(index,"orderToBeDeleted",manager.getCustomerName(),restaurantName,map);
            try{
                manager.getSocketWrapper().write(o);
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        });
    }
}
