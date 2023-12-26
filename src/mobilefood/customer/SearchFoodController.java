package mobilefood.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mobilefood.restaurant.Food;

public class SearchFoodController {
    private CustomerMain main;
    private CustomerManager manager;
    private int searchCategorySelected;
    private Map<Integer,List<Food>> menu;
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane container;

    @FXML
    private TextField search1;

    @FXML
    private TextField search2;

    void init(CustomerManager manager,CustomerMain main)
    {
        this.manager = manager;
        this.main = main;
        menu = new HashMap<>();
        searchCategorySelected = 0;
        search1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchAction();
            }
        });
        search2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchAction();
            }
        });
    }

    @FXML
    void goToLogin()
    {
        try{
            main.showHomePage();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void searchByName()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(319);
        searchCategorySelected = 1;
        search1.setText("");
        search2.setVisible(false);
        search1.setPromptText("Name");
        search1.setEditable(true);
    }

    @FXML
    void searchByCategory()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(319);
        searchCategorySelected = 2;
        search1.setText("");
        search2.setVisible(false);
        search1.setPromptText("Category");
        search1.setEditable(true);
    }

    @FXML
    void searchByPrice()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(143);
        searchCategorySelected = 3;
        search1.setText("");
        search1.setPromptText("Lower score");
        search1.setEditable(true);
        search2.setVisible(true);
        search2.setText("");
        search2.setPromptText("Upper score");
    }

    @FXML
    void showTotalFood()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(false);

        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(237);
        scrollPane.setLayoutY(37);
        scrollPane.setPrefWidth(340);
        scrollPane.setPrefHeight(420);

        VBox vBox = new VBox(10);
        scrollPane.setPrefWidth(324);
        scrollPane.setContent(vBox);
        container.getChildren().add(scrollPane);

        List<String> totalFoodtString = manager.totalFoodInAllRestaurant();
        for(String str : totalFoodtString)
        {
            Label foodCount = new Label(str);
            Font font = new Font(24);
            foodCount.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            vBox.getChildren().add(foodCount);
        }
    }

    private void searchAction(){
        if(searchCategorySelected>0 && searchCategorySelected<4)
        {
            menu = new HashMap<>();
            if(!(search1.getText().isEmpty() || search1.getText() == null))
            {
                if(searchCategorySelected == 1)
                {
                    menu = manager.searchFoodByName(search1.getText());
                }
                else if(searchCategorySelected == 2)
                {
                    menu = manager.searchFoodByCategory(search1.getText());
                }
                else
                {
                    if(!search2.getText().isEmpty())
                    {
                        menu = manager.searchFoodByPriceRange(Double.parseDouble(search1.getText()),Double.parseDouble(search2.getText()));
                    }
                }
            }
            
            if(!menu.isEmpty())
            {
                createStructure(menu);
            }
            else
            {
                container.getChildren().remove(scrollPane);
            }
        }
    }

    private void createStructure(Map<Integer,List<Food>> Food)
    {
        container.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(237);
        scrollPane.setLayoutY(86);
        scrollPane.setPrefWidth(340);
        scrollPane.setPrefHeight(360);
        scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

        VBox vBox = new VBox(20);
        vBox.setPrefWidth(324);
        vBox.setStyle("-fx-background-color: #000000;");

        scrollPane.setContent(vBox);
        container.getChildren().add(scrollPane);

        for(Map.Entry<Integer,List<Food>> id : Food.entrySet())
        {
            VBox vBox1 = new VBox(12);
            Label restaurantName = new Label(manager.getRestaurantNameById(id.getKey()));
            Font font = new Font(24);
            restaurantName.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            vBox1.getChildren().add(restaurantName);

            VBox vBox2 = new VBox(8);
            vBox1.getChildren().add(vBox2);
            vBox.getChildren().add(vBox1);
            List<Food> F = id.getValue();
            for(Food f : F)
            {
                AnchorPane gridPane = createGridPane(f);
                vBox2.getChildren().add(gridPane);
            }
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
        
        controller.setData(F,0);
        return controller.getAnchorPane();
    }
}
