package mobilefood.restaurant;

import java.text.DecimalFormat;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class NotCartOrderController {
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

    AnchorPane getAnchorPane()
    {
        return anchor;
    }

    void setData(Map.Entry<Food,Integer> f)
    {
        name.setText(f.getKey().getName());
        price.setText(Double.toString(f.getKey().getPrice()));
        category.setText(f.getKey().getCategory());
        orderCount.setText(Integer.toString(f.getValue()));
        DecimalFormat dec = new DecimalFormat("0.00");
        totalPrice.setText(dec.format(f.getValue()*f.getKey().getPrice()));
    }
}
