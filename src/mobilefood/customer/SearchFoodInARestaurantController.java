package mobilefood.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import mobilefood.passableobjects.Order;
import mobilefood.restaurant.Food;
import mobilefood.restaurant.Restaurant;

public class SearchFoodInARestaurantController {
    private CustomerMain main;
    private CustomerManager manager;
    private Restaurant restaurant;
    private List<Food> menu;
    private ScrollPane scrollPane;
    private int searchCategorySelected;
    private HashMap<Food,Integer> foodCount;
    private String prevPage;

    @FXML
    private AnchorPane container;

    @FXML
    private TextField search1;

    @FXML
    private TextField search2;

    @FXML
    private Label restaurantName;

    void init(CustomerManager manager,CustomerMain main,Restaurant r,String prevPage)
    {
        restaurant = r;
        restaurantName.setText(r.getName());
        this.manager = manager;
        this.main = main;
        foodCount = new HashMap<>();
        this.prevPage = prevPage;
        
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
            if(!foodCount.isEmpty())
            {
                Order order = new Order(1,"addToCart",manager.getCustomerName(),restaurant.getName(),foodCount);
                manager.getSocketWrapper().write(order);
            }
            if(prevPage.equals("Restaurant profile"))
            {
                main.showRestaurantProfile(restaurant);
            }
            else
            {
                main.showRestaurantSearchingPage("Place Order");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void searchByName()
    {
        searchCategorySelected = 1;
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(319);
        search1.setText("");
        search2.setVisible(false);
        search1.setPromptText("Name");
        search1.setEditable(true);
    }

    @FXML
    void searchByCategory()
    {
        searchCategorySelected = 2;
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(319);
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
    void showCostliestFood()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(false);
        search2.setVisible(false);

        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(237);
        scrollPane.setLayoutY(37);
        scrollPane.setPrefWidth(340);
        scrollPane.setPrefHeight(420);
        scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

        List<Food> costliestFoodtString = manager.costliestFoodInGivenRestaurant(restaurant.getName());
        VBox vBox = new VBox(6);
        vBox.setPrefWidth(324);
        vBox.setStyle("-fx-background-color: #000000;");
        for(Food f : costliestFoodtString)
        {
            AnchorPane gridPane = createGridPane(f);
            vBox.getChildren().add(gridPane);
        }
        scrollPane.setContent(vBox);
        container.getChildren().add(scrollPane);
    }

    private void searchAction()
    {
        if(searchCategorySelected>0 && searchCategorySelected<4)
        {
            menu = new ArrayList<>();
            if(!(search1.getText().isEmpty() || search1.getText() == null))
            {
                if(searchCategorySelected == 1)
                {
                    menu = manager.searchFoodByNameInGivenRestaurant(search1.getText(),restaurant);
                }
                else if(searchCategorySelected == 2)
                {
                    menu = manager.searchFoodByCategoryInGivenRestaurant(search1.getText(),restaurant);
                }
                else
                {
                    if(!search2.getText().isEmpty())
                    {
                        menu = manager.searchFoodByPriceRangeInGivenRestaurant(Double.parseDouble(search1.getText()),Double.parseDouble(search2.getText()),restaurant);
                    }
                }
            }
            
            if(menu.size()!=0)
            {
                container.getChildren().remove(scrollPane);
                scrollPane = new ScrollPane();
                scrollPane.setLayoutX(240);
                scrollPane.setLayoutY(99);
                scrollPane.setPrefWidth(340);
                scrollPane.setPrefHeight(360);
                scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

                VBox vBox = new VBox(8);
                vBox.setPrefWidth(324);
                vBox.setStyle("-fx-background-color: #000000;");
                for(Food f : menu)
                {
                    AnchorPane gridPane = createGridPane(f);
                    vBox.getChildren().add(gridPane);
                }
                scrollPane.setContent(vBox);
                container.getChildren().add(scrollPane);
            }
            else
            {
                container.getChildren().remove(scrollPane);
            }
        }
    }

    private AnchorPane createGridPane(Food F)
    {
        FoodOrderGridController controller = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("foodordergrid.fxml"));
        try{
            Parent root = main.getRoot();
            root = loader.load();

            controller = loader.getController();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        controller.setData(F,foodCount);
        return controller.getAnchorPane();
    }
}
