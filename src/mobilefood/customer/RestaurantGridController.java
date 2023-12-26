package mobilefood.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import mobilefood.restaurant.Restaurant;

public class RestaurantGridController {
    private CustomerMain main;
    @FXML
    private Label name;

    @FXML
    private Label score;

    @FXML
    private Label price;

    @FXML
    private Label zipcode;

    @FXML
    private Label categories;

    @FXML
    AnchorPane anchor;

    AnchorPane getAnchorPane()
    {
        return anchor;
    }

    synchronized void setData(Restaurant r,boolean flag)
    {
        name.setText(r.getName());
        name.setOnMouseEntered(e -> {
            name.setUnderline(true);
        });

        name.setOnMouseExited(e -> {
            name.setUnderline(false);
        });
        name.setOnMouseClicked(e -> {
            try{
                if(flag)
                {
                    main.showRestaurantProfile(r);
                }
                else
                {
                    main.showFoodSearchingInARestaurantPage(r,main.getPage());
                }
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        });

        score.setText(Double.toString(r.getScore()));
        price.setText(r.getPrice());
        zipcode.setText(r.getZipcode());
        String[] c = r.getCategory();
        String category = c[0];
        for(int j=1;j<3;j++)
        {
            if(!c[j].isEmpty())
            {
                category += ","+c[j];
            }
        }
        categories.setText(category);
    }

    void setMain(CustomerMain main)
    {
        this.main = main;
    }
}