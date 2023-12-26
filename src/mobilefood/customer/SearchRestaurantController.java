package mobilefood.customer;

import java.util.ArrayList;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mobilefood.restaurant.Restaurant;

public class SearchRestaurantController {
    private CustomerMain main;
    private CustomerManager manager;
    private int searchCategorySelected;
    private Map<String,List<Restaurant>> categoryToRestaurants;
    private List<Restaurant> restaurants;
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane container;

    @FXML
    private TextField search1;

    @FXML
    private TextField search2;

    @FXML
    private Label category;

    void init(CustomerManager manager,CustomerMain main)
    {
        restaurants = new ArrayList<>();
        this.manager = manager;
        this.main = main;
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
    void goBack()
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
        search1.setText("");
        search2.setVisible(false);
        searchCategorySelected = 1;
        search1.setPromptText("Name");
        search1.setEditable(true);
    }

    @FXML
    void searchByScore()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(143);
        search1.setText("");
        searchCategorySelected = 2;
        search1.setPromptText("Lower score");
        search1.setEditable(true);
        search2.setVisible(true);
        search2.setText("");
        search2.setPromptText("Upper score");
    }

    @FXML
    void searchByCategory()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(319);
        search1.setText("");
        search2.setVisible(false);
        searchCategorySelected = 3;
        search1.setPromptText("Category");
        search1.setEditable(true);
    }

    @FXML
    void searchByPrice()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(319);
        search1.setText("");
        search2.setVisible(false);
        searchCategorySelected = 4;
        search1.setPromptText("Price");
        search1.setEditable(true);
    }

    @FXML
    void searchByZipcode()
    {
        container.getChildren().remove(scrollPane);
        search1.setVisible(true);
        search1.setPrefWidth(319);
        search1.setText("");
        search2.setVisible(false);
        searchCategorySelected = 5;
        search1.setPromptText("Zip Code");
        search1.setEditable(true);
    }

    @FXML
    void showCategorizedList()
    {
        container.getChildren().remove(scrollPane);
        search2.setVisible(false);
        search1.setVisible(false);
        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(237);
        scrollPane.setLayoutY(37);
        scrollPane.setPrefWidth(340);
        scrollPane.setPrefHeight(420);
        scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

        VBox vBox = new VBox(20);
        vBox.setPrefWidth(324);
        vBox.setStyle("-fx-background-color: #000000;");
        categoryToRestaurants = manager.categoryWiseRestaurantList();
        for(String c : categoryToRestaurants.keySet())
        {
            VBox vBox1 = new VBox(12);
            Label category = new Label(c);
            Font font = new Font(24);
            category.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            category.setTextFill(Color.WHITE);
            vBox1.getChildren().add(category);

            VBox vBox2 = new VBox(8);
            List<Restaurant> cRestaurants = categoryToRestaurants.get(c);
            for(Restaurant r : cRestaurants)
            {
                AnchorPane gridPane = createGridPane(r);
                vBox2.getChildren().add(gridPane);
            }
            vBox1.getChildren().add(vBox2);
            vBox.getChildren().add(vBox1);
        }
        scrollPane.setContent(vBox);
        container.getChildren().add(scrollPane);
    }

    private void searchAction()
    {
        if(searchCategorySelected>0 && searchCategorySelected<6)
        {
            restaurants = new ArrayList<>();
            if(!(search1.getText().isEmpty() || search1.getText() == null))
            {
                if(searchCategorySelected == 1)
                {
                    restaurants = manager.searchRestaurantByName(search1.getText());
                }
                else if(searchCategorySelected == 2)
                {
                    if(!search2.getText().isEmpty())
                    {
                        restaurants = manager.searchRestaurantByScore(Double.parseDouble(search1.getText()),Double.parseDouble(search2.getText()));
                    }
                }
                else if(searchCategorySelected == 3)
                {
                    restaurants = manager.searchRestaurantByCategory(search1.getText());
                }
                else if(searchCategorySelected == 4)
                {
                    restaurants = manager.searchRestaurantByPrice(search1.getText());
                }
                else
                {
                    restaurants = manager.searchRestaurantByZipcode(search1.getText());
                }
            }

            if(restaurants.size()!=0)
            {
                setUpGridPane();
            }
            else
            {
                container.getChildren().remove(scrollPane);
            }
        }
    }

    private void setUpGridPane()
    {
        container.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(237);
        scrollPane.setLayoutY(86);
        scrollPane.setPrefWidth(340);
        scrollPane.setPrefHeight(360);
        scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

        VBox vBox = new VBox(10);
        vBox.setPrefWidth(324);
        vBox.setStyle("-fx-background-color: #000000;");

        for(Restaurant r : restaurants)
        {
            AnchorPane gridPane = createGridPane(r);
            vBox.getChildren().add(gridPane);
        }
        scrollPane.setContent(vBox);
        container.getChildren().add(scrollPane);
    }

    private AnchorPane createGridPane(Restaurant r)
    {
        RestaurantGridController controller = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("restaurantgrid.fxml"));
        try{
            Parent root = main.getRoot();
            root = loader.load();

            controller = loader.getController();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        controller.setMain(main);
        if(main.getPage().equals("Place Order"))
        {
            controller.setData(r,false);
        }
        else
        {
            controller.setData(r,true);
        }
        return controller.getAnchorPane();
    }
}
