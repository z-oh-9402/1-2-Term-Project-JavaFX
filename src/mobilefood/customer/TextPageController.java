package mobilefood.customer;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;
import mobilefood.passableobjects.Message;
import mobilefood.restaurant.Restaurant;

public class TextPageController {
    private CustomerMain main;
    private CustomerManager manager;
    private Restaurant restaurant;
    private List<Pair<String,String>> chatList;
    private ScrollPane scrollPane;
    private VBox vBox;

    @FXML
    private AnchorPane container;

    @FXML
    private TextField yourText;

    void init(CustomerManager manage, CustomerMain main,Restaurant restaurant)
    {
        this.main = main;
        manager = manage;
        this.restaurant = restaurant;
        chatList = new ArrayList<>();
        scrollPane = new ScrollPane();
        vBox = new VBox(12);
        scrollPane.setContent(vBox);

        scrollPane.setPrefWidth(325);
        scrollPane.setPrefHeight(363);
        scrollPane.setLayoutX(52);
        scrollPane.setLayoutY(43);
        vBox.setPrefWidth(309);

        container.getChildren().add(scrollPane);
        scrollPane.setVisible(false);

        if(manager.getChatList().containsKey(restaurant.getName()))
        {
            chatList = manager.getChatList().get(restaurant.getName());
            showChatThread();
        }
    }

    @FXML
    void sendMessage()
    {
        if(!yourText.getText().isEmpty())
        {
            String message = yourText.getText();
            yourText.setText("");
            yourText.setPromptText("write something....");
            if(!scrollPane.isVisible())
            {
                scrollPane.setVisible(true);
            }

            chatList.add(new Pair<>(manager.getCustomerName(),message));
            manager.getChatList().put(restaurant.getName(),chatList);
            try{
                manager.getSocketWrapper().write(new Message(manager.getCustomerName(), restaurant.getName(),manager.getCustomerName(), message));
            }
            catch(Exception e)
            {
                System.out.println(e);
            }

            Label chatbox = new Label(message);
            chatbox.setAlignment(Pos.BASELINE_RIGHT);
            chatbox.setStyle("-fx-background-color: #DBBC55;-fx-background-radius: 10px;");
            chatbox.setPadding(new Insets(10, 10, 10, 10));
            Font font = new Font(12);
            chatbox.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            HBox hBox = new HBox();
            hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            hBox.getChildren().add(chatbox);
            vBox.getChildren().add(hBox);
        }
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

    private void showChatThread()
    {
        scrollPane.setVisible(true);
        vBox.setVisible(true);

        for(Pair<String,String> pair : chatList)
        {
            Label chatbox = new Label(pair.getValue());
            chatbox.setPadding(new Insets(10, 10, 10, 10));
            Font font = new Font(12);
            chatbox.setFont(Font.font(font.getFamily(),FontWeight.BOLD,font.getSize()));
            HBox hBox = new HBox();

            if(pair.getKey().equals(restaurant.getName()))
            {
                hBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                chatbox.setStyle("-fx-background-color: #DEDEDE;-fx-background-radius: 10px;");
            }
            else
            {
                hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                chatbox.setStyle("-fx-background-color: #DBBC55;-fx-background-radius: 10px;");
            }

            hBox.getChildren().add(chatbox);
            vBox.getChildren().add(hBox);
        }
    }
}
