package mobilefood.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RestaurantMain extends Application {
    
    private Stage stage;
    private Parent root;
    private RestaurantManager manager;
    private String page;
    private String customerName;

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

    public void showRegisterInfoPage(String restaurantName) throws Exception{
        page = "RegisterInfo";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("registerinfo.fxml"));
        root = loader.load();

        // Loading the controller
        RegisterInfoController controller = loader.getController();
        controller.setMain(this);
        controller.setRestaurantName(restaurantName);

        // Set the primary stage
        stage.setTitle("Register");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showHomePage() throws Exception {
        page = "Home";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("restaurant.fxml"));
        root = loader.load();

        // Loading the controller
        RestaurantController controller = loader.getController();
        controller.init(manager,this);

        // Set the primary stage
        stage.setTitle("Home");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showAddFoodPage() throws Exception{
        page = "Add Food";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addfood.fxml"));
        root = loader.load();

        // Loading the controller
        AddFoodController controller = loader.getController();
        controller.init(manager,this);

        // Set the primary stage
        stage.setTitle("Add Food");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showMenuPage() throws Exception{
        page = "Show Menu";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("showmenu.fxml"));
        root = loader.load();

        // Loading the controller
        ShowMenuController controller = loader.getController();
        controller.init(this,manager);

        // Set the primary stage
        stage.setTitle("Menu");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showOrderPage() throws Exception{
        page = "Show Order";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("orderpage.fxml"));
        root = loader.load();

        // Loading the controller
        OrderPageController controller = loader.getController();
        controller.init(manager,this);

        // Set the primary stage
        stage.setTitle("Orders");
        stage.getIcons().add(new Image("C:\\Users\\HP\\Desktop\\BUET\\1-2\\OOP\\Java_Term_Project_Part_2\\MobileFood\\Cute_chef_boy_cartoon_character-removebg-preview.png"));
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public void showChatListPage(String name) throws Exception{
        page = "Chat List";
        customerName = name;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("chatlist.fxml"));
        root = loader.load();

        // Loading the controller
        ChatListController controller = loader.getController();
        controller.init(manager,this,customerName);

        // Set the primary stage
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

    public void showAlert(int worktype){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        if(worktype == 0)
        {
            alert.setContentText("This food already exists in the same category.");
            alert.showAndWait();
        }
        else if(worktype == 1)
        {
            alert.setContentText("The username already exists.");
            alert.showAndWait();
        }
        else
        {
            alert.setContentText("Invalid input.");
            alert.showAndWait();
        }
    }

    public void setRestaurantManager(RestaurantManager manager)
    {
        this.manager = manager;
    }

    public String getPage()
    {
        return page;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
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
