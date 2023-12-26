package mobilefood.restaurant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import networking.ReadThreadRestaurant;
import networking.SocketWrapper;

public class LoginController {
    private RestaurantMain main;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label alertText;

    @FXML
    void registerAction()
    {
        try{
            main.showRegisterPage();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    void loginAction(ActionEvent event) {
        String username = userText.getText();
        String password = passwordText.getText();

        try
        {
            if(!username.isEmpty() && !password.isEmpty())
            {
                SocketWrapper socketWrapper = new SocketWrapper("127.0.0.1",12345);
                socketWrapper.write("restaurantLogin");
                socketWrapper.write(username);
                socketWrapper.write(password);
                int o = (Integer) socketWrapper.read();
                if(o == 0)
                {
                    main.showAlert(false);
                }
                else if(o == -1)
                {
                    main.showAlert(true);
                }
                else
                {
                    try {
                        Restaurant r = (Restaurant)socketWrapper.read();
                        RestaurantManager manager = new RestaurantManager(r, socketWrapper);
                        main.setRestaurantManager(manager);
                        new ReadThreadRestaurant(manager,main);
                        main.showHomePage();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
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
