package mobilefood.customer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mobilefood.restaurant.Food;
import mobilefood.restaurant.Restaurant;

public class MenuPageController {
    private CustomerMain main;
    private Restaurant restaurant;
    private Map<String,List<Food>> menu;
    private ScrollPane scrollPane;
    private Label message;

    @FXML
    private AnchorPane container;

    void init(Restaurant r,CustomerMain main)
    {
        restaurant = r;
        menu = restaurant.getMenu();
        this.main = main;
        createStructure();
    }

    @FXML
    void goToLogin()
    {
        try{
            main.showRestaurantProfile(restaurant);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private void createStructure()
    {
        container.getChildren().remove(message);
        container.getChildren().remove(scrollPane);
        if(!menu.isEmpty())
        {
            scrollPane = new ScrollPane();
            scrollPane.setLayoutX(239);
            scrollPane.setLayoutY(36);
            scrollPane.setPrefHeight(427);
            scrollPane.setPrefWidth(340);
            scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

            VBox vBox = new VBox(30);
            vBox.setPrefWidth(324);
            vBox.setStyle("-fx-background-color: #000000;");
            for(Map.Entry<String,List<Food>> category : menu.entrySet())
            {
                VBox vBox1 = new VBox(15);
                vBox.setPrefWidth(324);

                Label label = new Label(category.getKey());
                Font font = new Font(26);
                label.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
                vBox.getChildren().add(label);

                List<Food> list = category.getValue();
                Collections.sort(list,(food1,food2)->Integer.compare(food2.getAllTimeOrderCount(), food1.getAllTimeOrderCount()));
                VBox vBox2 = new VBox(9);
                vBox2.setPrefWidth(324);

                for(Food F : list)
                {
                    AnchorPane gridPane = createGridPane(F);
                    vBox2.getChildren().add(gridPane);
                }

                vBox1.getChildren().addAll(label,vBox2);
                vBox.getChildren().add(vBox1);
            }

            scrollPane.setContent(vBox);
            container.getChildren().add(scrollPane);
        }
        else
        {
            message = new Label("There is nothing in the list");
            message.setTextFill(Color.WHITE);
            message.setLayoutX(180);
            message.setLayoutY(210);
            message.setPrefHeight(40);
            message.setPrefWidth(340);
            Font font5 = new Font(18);
            message.setFont(Font.font(font5.getFamily(),FontWeight.BOLD,font5.getSize()));
            container.getChildren().add(message);
        }
    }

    private AnchorPane createGridPane(Food F)
    {
        FoodGridController controller = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("foodgrid.fxml"));
        try{
            Parent root = main.getRoot();
            root = loader.load();

            controller = loader.getController();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        controller.setData(F,1);
        return controller.getAnchorPane();
    }
}
