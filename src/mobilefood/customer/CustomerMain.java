package mobilefood.customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mobilefood.restaurant.Restaurant;

public class CustomerMain extends Application {
    
    private Stage stage;
    private Parent root;
    private CustomerManager manager;
    private String page;
    private String restaurantName;

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showLoginPage();
    }


    public void showLoginPage() throws Exception {
        page = "Login";
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        root = loader.load();

        // Loading the controller
        LoginController controller = loader.getController();
        controller.setMain(this);

        // Set the primary stage
        stage.setTitle("Login");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showRegisterPage() throws Exception {
        page = "Register";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("register.fxml"));
        root = loader.load();

        // Loading the controller
        RegisterController controller = loader.getController();
        controller.setMain(this);

        // Set the primary stage
        stage.setTitle("Register");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showHomePage() throws Exception {
        page = "Home";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("customer.fxml"));
        root = loader.load();

        // Loading the controller
        CustomerController controller = loader.getController();
        controller.init(manager,this);

        // Set the primary stage
        stage.setTitle("Home");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showRestaurantSearchingPage(String pageInfo) throws Exception {
        page = pageInfo;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchrestaurant.fxml"));
        root = loader.load();

        // Loading the controller
        SearchRestaurantController controller = loader.getController();
        controller.init(manager,this);

        stage.setTitle("Search Restaurant");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showFoodSearchingPage() throws Exception {
        page = "Food search";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchfood.fxml"));
        root = loader.load();

        // Loading the controller
        SearchFoodController controller = loader.getController();
        controller.init(manager,this);

        stage.setTitle("Search Food");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showFoodSearchingInARestaurantPage(Restaurant restaurant,String str) throws Exception {
        page = "Food search in a restaurant";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchfoodinarestaurant.fxml"));
        root = loader.load();

        // Loading the controller
        SearchFoodInARestaurantController controller = loader.getController();
        controller.init(manager,this,restaurant,str);

        stage.setTitle("Search Food in a Restaurant");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showRestaurantProfile(Restaurant restaurant) throws Exception {
        page = "Restaurant profile";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("restaurantprofile.fxml"));
        root = loader.load();

        // Loading the controller
        RestaurantProfileController controller = loader.getController();
        controller.init(restaurant,this);

        stage.setTitle("Restaurant profile");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showCartPage(Restaurant restaurant) throws Exception {
        page = "Cart";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("confirmorder.fxml"));
        root = loader.load();

        // Loading the controller
        ConfirmOrderController controller = loader.getController();
        controller.init(restaurant,this,manager);

        stage.setTitle("Cart");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showMenuPage(Restaurant restaurant) throws Exception {
        page = "Menu";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("menupage.fxml"));
        root = loader.load();

        // Loading the controller
        MenuPageController controller = loader.getController();
        controller.init(restaurant,this);

        stage.setTitle("Menu");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showOrderPage(int i) throws Exception {
        page = "Order"+i;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("orderpage.fxml"));
        root = loader.load();

        // Loading the controller
        OrderPageController controller = loader.getController();
        controller.init(manager,this);

        stage.setTitle("Orders");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showTextingPage(Restaurant restaurant) throws Exception{
        page = "Texting";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("textpage.fxml"));
        root = loader.load();

        // Loading the controller
        TextPageController controller = loader.getController();
        controller.init(manager,this,restaurant);

        stage.setTitle("Text");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showChatPage(String name) throws Exception{
        page = "Chat List";
        restaurantName = name;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("chatpage.fxml"));
        root = loader.load();

        // Loading the controller
        ChatPageController controller = loader.getController();
        controller.init(manager,this,restaurantName);

        stage.setTitle("Chat List");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showAlert(boolean exist) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        if(exist)
        {
            alert.setContentText("The username and password you provided does not match.");
        }
        else
        {
            alert.setContentText("The username does not exist.");
        }
        alert.showAndWait();
    }

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username already exists.");
        alert.showAndWait();
    }

    public void setCustomerManager(CustomerManager manager)
    {
        this.manager = manager;
    }

    public String getPage()
    {
        return page;
    }

    void setPage(String pageWithNum)
    {
        page = pageWithNum;
    }

    void setRestaurantName(String restaurantName)
    {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName()
    {
        return restaurantName;
    }

    Parent getRoot()
    {
        return root;
    } 

    public static void main(String[] args) {
        // This will launch the JavaFX application
        launch(args);
    }
}
