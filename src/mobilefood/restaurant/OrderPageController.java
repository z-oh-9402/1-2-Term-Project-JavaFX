package mobilefood.restaurant;

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

public class OrderPageController {
    private RestaurantMain main;
    private RestaurantManager manager;
    private HashMap<String,HashMap<Integer,HashMap<Food,Integer>>> orders;
    private ScrollPane scrollPane;
    private Label message;

    @FXML
    private AnchorPane container;

    public void init(RestaurantManager manage,RestaurantMain main)
    {
        manager = manage;
        this.main = main;
        orders = manager.getOrders();
        createStructure();
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

    private void createStructure()
    {
        container.getChildren().remove(message);
        container.getChildren().remove(scrollPane);
        if(!orders.isEmpty())
        {
            scrollPane = new ScrollPane();
            scrollPane.setLayoutX(49);
            scrollPane.setLayoutY(133);
            scrollPane.setPrefHeight(300);
            scrollPane.setPrefWidth(506);
            scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000");

            VBox vBox = new VBox(30);
            vBox.setPrefWidth(490);
            vBox.setStyle("-fx-background-color: #000000;");
            scrollPane.setContent(vBox);
            for(Map.Entry<String,HashMap<Integer,HashMap<Food,Integer>>> clientName : orders.entrySet())
            {
                VBox vBox1 = new VBox(20);
                vBox1.setPrefWidth(490);
                vBox1.setStyle("-fx-background-color: #000000;");

                Label label = new Label(clientName.getKey());
                Font font = new Font(28);
                label.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
                vBox.getChildren().add(label);

                HashMap<Integer,HashMap<Food,Integer>> list = clientName.getValue();
                for(Map.Entry<Integer,HashMap<Food,Integer>> i : list.entrySet())
                {
                    HashMap<Food,Integer> foodCount = i.getValue();
                    VBox vBox2 = new VBox(12);
                    vBox2.setPrefWidth(400);

                    Label label1 = new Label("Order: "+Integer.toString(i.getKey()));
                    Font font1 = new Font(24);
                    label1.setFont(Font.font(font1.getFamily(),FontWeight.BOLD,font1.getSize()));

                    VBox vBox3 = new VBox(7);
                    vBox3.setPrefWidth(400);

                    for(Map.Entry<Food,Integer> F : foodCount.entrySet())
                    {
                        AnchorPane gridPane = createGridPane(F);
                        vBox3.getChildren().add(gridPane);
                    }

                    ButtonBar bar = new ButtonBar();
                    Button confirmOrder = new Button("Confirm Order");
                    confirmOrder.setPrefHeight(25);
                    confirmOrder.setPrefWidth(130);
                    confirmOrder.setStyle("-fx-background-color: linear-gradient(to right, #82DEBC, #01C07A);-fx-background-radius: 50px;-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic;");
                    confirmOrder.setOnMouseClicked(e ->{
                        Order o = new Order(i.getKey(),"confirmedOrder",clientName.getKey(),manager.getRestaurant().getName(),i.getValue());
                        try{
                            manager.getSocketWrapper().write(o);
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex);
                        }
                    });
                    bar.getButtons().add(confirmOrder);
                    vBox2.getChildren().addAll(label1,vBox3,bar);
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
}
