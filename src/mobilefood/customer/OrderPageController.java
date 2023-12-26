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

public class OrderPageController {
    private CustomerMain main;
    private CustomerManager manager;
    private HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> orders;
    private ScrollPane scrollPane;
    private Label message;

    @FXML
    private AnchorPane container;

    void init(CustomerManager manager,CustomerMain main)
    {
        this.manager = manager;
        this.main = main;
        if(main.getPage().equals("Order1"))
        {
            orders = manager.getAddedToCart();
        }
        if(main.getPage().equals("Order2"))
        {
            orders = manager.getPendingOrder();
        }
        if(main.getPage().equals("Order3"))
        {
            orders = manager.getConfirmedOrder();
        }
        createOrderPane();
    }

    @FXML
    void showCartOrder()
    {
        orders = manager.getAddedToCart();
        main.setPage("Order"+1);
        createOrderPane();
    }

    @FXML
    void showPendingOrder()
    {
        orders = manager.getPendingOrder();
        main.setPage("Order"+2);
        createOrderPane();
    }

    @FXML
    void showConfirmedOrder()
    {
        orders = manager.getConfirmedOrder();
        main.setPage("Order"+3);
        createOrderPane();
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

    void createOrderPane()
    {
        container.getChildren().remove(message);
        container.getChildren().remove(scrollPane);
        if(!orders.isEmpty())
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
            for(Map.Entry<String,HashMap<Integer,HashMap<Food,Integer>>> restaurantName : orders.entrySet())
            {
                VBox vBox1 = new VBox(20);
                vBox1.setPrefWidth(490);
                vBox1.setStyle("-fx-background-color: #000000;");

                Label label = new Label(restaurantName.getKey());
                Font font = new Font(30);
                label.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
                vBox1.getChildren().add(label);

                HashMap<Integer,HashMap<Food,Integer>> list = restaurantName.getValue();
                for(Map.Entry<Integer,HashMap<Food,Integer>> i : list.entrySet())
                {
                    HashMap<Food,Integer> foodCount = i.getValue();
                    VBox vBox2 = new VBox(12);
                    vBox2.setPrefWidth(490);
                    vBox2.setStyle("-fx-background-color: #000000;");

                    Label label1 = new Label("Order: "+Integer.toString(i.getKey()));
                    Font font1 = new Font(22);
                    label1.setFont(Font.font(font1.getFamily(),FontWeight.BOLD,font1.getSize()));

                    VBox vBox3 = new VBox(8);
                    vBox3.setPrefWidth(490);
                    vBox3.setStyle("-fx-background-color: #000000;");

                    for(Map.Entry<Food,Integer> F : foodCount.entrySet())
                    {
                        AnchorPane gridPane = new AnchorPane();
                        if(main.getPage().equals("Order1"))
                        {
                            gridPane = createGridPane2(F,list,i.getKey(),restaurantName.getKey());
                        }
                        else
                        {
                            gridPane = createGridPane(F);
                        }
                        vBox3.getChildren().add(gridPane);
                    }

                    if(main.getPage().equals("Order1"))
                    {
                        ButtonBar bar = new ButtonBar();
                        bar.setPrefHeight(35);
                        bar.setPrefWidth(220);
                        Button cancelOrder = new Button("Cancel Order");
                        cancelOrder.setStyle("-fx-background-color: linear-gradient(to right, #FFD7E3, #E6A5BA);-fx-background-radius: 50px;-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic;");
                        cancelOrder.setOnMouseClicked(e ->{
                            Order o = new Order(i.getKey(),"orderToBeCancelled",manager.getCustomerName(), restaurantName.getKey(),foodCount);
                            try{
                                manager.getSocketWrapper().write(o);
                            }
                            catch(Exception ex)
                            {
                                System.out.println(ex);
                            }
                        });
                        Button confirmOrder = new Button("Confirm Order");
                        confirmOrder.setStyle("-fx-background-color: linear-gradient(to right, #FFD7E3, #E6A5BA);-fx-background-radius: 50px;-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic;");
                        confirmOrder.setOnMouseClicked(e ->{
                            Order o = new Order(i.getKey(),"orderToBeConfirmed",manager.getCustomerName(), restaurantName.getKey(),foodCount);
                            try{
                                manager.getSocketWrapper().write(o);
                            }
                            catch(Exception ex)
                            {
                                System.out.println(ex);
                            }
                        });
                        bar.getButtons().addAll(confirmOrder,cancelOrder);
                        vBox2.getChildren().addAll(label1,vBox3,bar);
                    }
                    else
                    {
                        vBox2.getChildren().addAll(label1,vBox3);
                    }
                    vBox1.getChildren().add(vBox2);
                }

                vBox.getChildren().add(vBox1);
            }

            scrollPane.setContent(vBox);
            container.getChildren().add(scrollPane);
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

    private AnchorPane createGridPane(Map.Entry<Food,Integer> food)
    {
        NotCartOrderController controller = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("notcartorder.fxml"));
        try{
            Parent root = main.getRoot();
            root = loader.load();

            controller = loader.getController();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        controller.setData(food);
        return controller.getAnchorPane();
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
        controller.setData(F, foodCountMap, index, restaurantName,manager,this);
        return controller.getAnchorPane();
    }
}
