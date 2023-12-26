package mobilefood.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import networking.ReadThreadCustomer;
import networking.SocketWrapper;

public class RegisterController {
    private CustomerMain main;
    private String customerName;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label alertText;

    private void dataLoad()
    {
        try{
            SocketWrapper socketWrapper = new SocketWrapper("127.0.0.1", 12345);
            CustomerManager manager = new CustomerManager(customerName,socketWrapper);
            main.setCustomerManager(manager);
            socketWrapper.write("customerData");
            socketWrapper.write(customerName);
            new ReadThreadCustomer(manager,main);
        } 
        catch (Exception e) {
                System.out.println(e);
        }
    }

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
                socketWrapper.write("customerRegister");
                socketWrapper.write(username);
                socketWrapper.write(password);
                boolean check = (boolean)(socketWrapper.read());
                socketWrapper.closeConnection();
                if(check)
                {
                    try {
                        customerName = username;
                        new Thread(this::dataLoad,"T").start();
                        main.showHomePage();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                else
                {
                    main.showAlert();
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

    void setMain(CustomerMain main)
    {
        this.main = main;
    }
}
