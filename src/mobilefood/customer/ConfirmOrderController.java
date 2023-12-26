package mobilefood.customer;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mobilefood.passableobjects.Order;
import mobilefood.restaurant.Food;
import mobilefood.restaurant.Restaurant;

public class ConfirmOrderController {
    private Restaurant restaurant;
    private CustomerMain main;
    private CustomerManager manager;
    private HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> orders;
    private ScrollPane scrollPane;
    private Label message;

    @FXML
    private AnchorPane container;

    void init(Restaurant restaurant,CustomerMain main,CustomerManager manager)
    {
        this.manager = manager;
        this.main = main;
        this.restaurant = restaurant;
        orders = manager.getAddedToCart();
        createOrderPane();
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

    void createOrderPane()
    {
        container.getChildren().remove(message);
        container.getChildren().remove(scrollPane);
        if(!orders.isEmpty())
        {
            if(orders.containsKey(restaurant.getName()))
            {
                scrollPane = new ScrollPane();
                scrollPane.setLayoutX(49);
                scrollPane.setLayoutY(168);
                scrollPane.setPrefHeight(300);
                scrollPane.setPrefWidth(506);
                scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

                VBox vBox = new VBox(30);
                vBox.setPrefWidth(490);
                vBox.setStyle("-fx-background-color: #000000;");
                scrollPane.setContent(vBox);
                HashMap<Integer,HashMap<Food,Integer>> list = orders.get(restaurant.getName());
                for(Map.Entry<Integer,HashMap<Food,Integer>> i : list.entrySet())
                {
                    HashMap<Food,Integer> foodCount = i.getValue();
                    VBox vBox1 = new VBox(12);
                    vBox1.setPrefWidth(490);
                    vBox1.setStyle("-fx-background-color: #000000;");

                    Label label1 = new Label("Order: "+Integer.toString(i.getKey()));
                    Font font1 = new Font(22);
                    label1.setFont(Font.font(font1.getFamily(),FontWeight.BOLD,font1.getSize()));

                    VBox vBox2 = new VBox(8);
                    vBox2.setPrefWidth(490);
                    vBox2.setStyle("-fx-background-color: #000000;");

                    for(Map.Entry<Food,Integer> F : foodCount.entrySet())
                    {
                        AnchorPane gridPane = new AnchorPane();
                        gridPane = createGridPane2(F,list,i.getKey(),restaurant.getName());
                        vBox2.getChildren().add(gridPane);
                    }

                    ButtonBar bar = new ButtonBar();
                    Button cancelOrder = new Button("Cancel Order");
                    cancelOrder.setPrefHeight(35);
                    cancelOrder.setPrefWidth(130);
                    cancelOrder.setStyle("-fx-background-color: linear-gradient(to right, #82DEBC, #01C07A);-fx-background-radius: 50px;-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic;");
                    cancelOrder.setOnMouseClicked(e ->{
                        Order o = new Order(i.getKey(),"orderToBeCancelled",manager.getCustomerName(), restaurant.getName(),foodCount);
                        try{
                            manager.getSocketWrapper().write(o);
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex);
                        }
                    });
                    Button confirmOrder = new Button("Confirm Order");
                    confirmOrder.setPrefHeight(35);
                    confirmOrder.setStyle("-fx-background-color: linear-gradient(to right, #82DEBC, #01C07A);-fx-background-radius: 50px;-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic;");
                    confirmOrder.setOnMouseClicked(e ->{
                        Order o = new Order(i.getKey(),"orderToBeConfirmed",manager.getCustomerName(), restaurant.getName(),foodCount);
                        try{
                            manager.getSocketWrapper().write(o);
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex);
                        }
                    });
                    bar.getButtons().addAll(confirmOrder,cancelOrder);
                    vBox.getChildren().addAll(label1,vBox2,bar);

                    vBox.getChildren().add(vBox1);
                }

                scrollPane.setContent(vBox);
                container.getChildren().add(scrollPane);
            }
        }
        else
        {
            message = new Label("There is nothing in the list");
            message.setTextFill(Color.WHITE);
            message.setLayoutX(130);
            message.setLayoutY(210);
            message.setPrefHeight(40);
            message.setPrefWidth(340);
            Font font5 = new Font(18);
            message.setFont(Font.font(font5.getFamily(),FontWeight.BOLD,font5.getSize()));
            container.getChildren().add(message);
        }
    }

    private AnchorPane createGridPane2(Map.Entry<Food,Integer> F,HashMap<Integer,HashMap<Food,Integer>> foodCountMap,int index,String restaurantName)
    {
        CartOrderController controller = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("cartorder.fxml"));
        try{
            Parent root = main.getRoot();
            root = loader.load();

            controller = loader.getController();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        controller.setData(F,foodCountMap,index,restaurantName,manager);
        return controller.getAnchorPane();
    }
}
