package mobilefood.restaurant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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

    public void setData(Food f)
    {
        name.setText(f.getName());
        price.setText(Double.toString(f.getPrice()));
        category.setText(f.getCategory());
    }
}
