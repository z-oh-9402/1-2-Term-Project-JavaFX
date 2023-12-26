package mobilefood.restaurant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;
import mobilefood.passableobjects.Message;

public class ChatListController {
    private RestaurantMain main;
    private RestaurantManager manager;
    private HashMap<String,List<Pair<String,String>>> chatList;
    private ScrollPane scrollPane;
    private TextField text;
    private ImageView imageView;
    private VBox vBox;
    private VBox vBox1;
    private Label emptyMessage;

    @FXML
    private AnchorPane container;

    @FXML
    private ScrollPane scrollPane1;

    void init(RestaurantManager manage,RestaurantMain main,String name)
    {
        this.main = main;
        manager = manage;
        chatList = new HashMap<>();
        vBox1 = new VBox(8);
        scrollPane1.setContent(vBox1);

        if(!manager.getchatList().isEmpty())
        {
            vBox1.setPrefWidth(144);
            chatList = manager.getchatList();

            for(Map.Entry<String,List<Pair<String,String>>> Name : chatList.entrySet())
            {
                Label label = new Label(Name.getKey());
                Font font = new Font(18);
                label.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
                vBox1.getChildren().add(label);
                label.setOnMouseEntered(e -> {
                    label.setUnderline(true);
                });
        
                label.setOnMouseExited(e -> {
                    label.setUnderline(false);
                });
                label.setOnMouseClicked(e -> {
                    try{
                        main.setCustomerName(Name.getKey());
                        showChatSection(Name.getKey(),Name.getValue());
                    }
                    catch(Exception ex)
                    {
                        System.out.println(ex);
                    }
                });
            }

            if(!name.isEmpty())
            {
                showChatSection(name,chatList.get(name));
            }
        }

        else
        {
            emptyMessage = new Label("No Chat History");
            Font font = new Font(16);
            emptyMessage.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            vBox1.getChildren().add(emptyMessage);
        }
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

    private void showChatSection(String customerName,List<Pair<String,String>> chat)
    {
        container.getChildren().removeAll(scrollPane,text,imageView,emptyMessage);

        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(220);
        scrollPane.setLayoutY(25);
        scrollPane.setPrefHeight(397);
        scrollPane.setPrefWidth(360);
        scrollPane.setStyle("-fx-background-radius: 10px;");
        scrollPane.setStyle("-fx-border-radius: 10px;");

        vBox = new VBox(12);
        vBox.setPrefWidth(344);
        vBox.setStyle("-fx-background-radius: 10px;");
        vBox.setStyle("-fx-border-radius: 10px;");
        scrollPane.setContent(vBox);

        text = new TextField();
        text.setStyle("-fx-background-radius: 70px;");
        text.setPromptText("write something...");
        text.setLayoutX(220);
        text.setLayoutY(438);
        text.setPrefHeight(39);
        text.setPrefWidth(318);

        imageView = new ImageView("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\images-removebg-preview.png");
        imageView.setLayoutX(546);
        imageView.setLayoutY(441);
        imageView.setPickOnBounds(true);
        imageView.setFitWidth(34);
        imageView.setFitHeight(34);
        imageView.setOnMouseClicked(e ->{
            sendText(customerName);
        });

        for(Pair<String,String> pair : chat)
        {
            Label chatbox = new Label(pair.getValue());
            chatbox.setPadding(new Insets(10, 10, 10, 10));
            chatbox.setTextFill(Color.BLACK);
            Font font = new Font(12);
            chatbox.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            HBox hBox = new HBox();

            if(pair.getKey().equals(manager.getRestaurant().getName()))
            {
                hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                chatbox.setStyle("-fx-background-color: #9CCF91;-fx-background-radius: 10px;");
            }
            else
            {
                hBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                chatbox.setStyle("-fx-background-color: #DEDEDE;-fx-background-radius: 10px;");
            }

            hBox.getChildren().add(chatbox);
            vBox.getChildren().add(hBox);
        }

        container.getChildren().addAll(scrollPane,text,imageView);
    }

    private void sendText(String Name)
    {
        if(!text.getText().isEmpty())
        {
            String message = text.getText();
            text.setText("");
            text.setPromptText("write something....");

            chatList.get(Name).add(new Pair<>(manager.getRestaurant().getName(),message));
            try{
                manager.getSocketWrapper().write(new Message(Name, manager.getRestaurant().getName(), manager.getRestaurant().getName(), message));
            }
            catch(Exception e)
            {
                System.out.println(e);
            }

            Label chatbox = new Label(message);
            chatbox.setAlignment(Pos.BASELINE_RIGHT);
            chatbox.setStyle("-fx-background-color: #9CCF91;-fx-background-radius: 10px;");
            chatbox.setPadding(new Insets(10, 10, 10, 10));
            chatbox.setTextFill(Color.BLACK);
            Font font = new Font(12);
            chatbox.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            HBox hBox = new HBox();
            hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            hBox.getChildren().add(chatbox);
            vBox.getChildren().add(hBox);
        }
    }
}
