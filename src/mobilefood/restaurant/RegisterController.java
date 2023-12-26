package mobilefood.restaurant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import networking.SocketWrapper;

public class RegisterController {
    private RestaurantMain main;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label alertText;

    @FXML
    void goToLogin()
    {
        try{
            main.showLoginPage();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void registerAction(ActionEvent event)
    {
        try{
            String username = userText.getText();
            String password = passwordText.getText();

            if(!username.isEmpty() && !password.isEmpty())
            {
                SocketWrapper socketWrapper = new SocketWrapper("127.0.0.1", 12345);
                socketWrapper.write("restaurantRegister");
                socketWrapper.write(username);
                socketWrapper.write(password);
                if((boolean)socketWrapper.read())
                {
                    main.showRegisterInfoPage(username);
                }
                else
                {
                    main.showAlert(1);
                }
                socketWrapper.closeConnection();
            }
            else
            {
                new Thread(this::blinking,"T").start();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void resetAction(ActionEvent event) {
        userText.setText("");
        passwordText.setText("");
    }

    private void blinking()
    {
        alertText.setOpacity(1);
        try{
            Thread.sleep(1500);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        alertText.setOpacity(0);
    }

    public void setMain(RestaurantMain main)
    {
        this.main = main;
    }
}
