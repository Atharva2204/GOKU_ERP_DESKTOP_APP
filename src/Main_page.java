import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class Main_page extends Application {
    // Button login;
    // Button exit;

    Label response;
    int authentiction_result=-1;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //giving the title to the stage
        stage.setTitle("GOKU ERP");

        //this is the first element in the stage
        GridPane rootnode = new GridPane();

        rootnode.setAlignment(Pos.CENTER);
        rootnode.setHgap(10);
        rootnode.setVgap(10);
        rootnode.setPadding(new Insets(25, 25, 25, 25));


        Text title = new Text("Welcome!");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        rootnode.add(title, 0, 0, 1, 1);

        Label username = new Label("Username");
        rootnode.add(username, 0, 1);

        TextField userTextField = new TextField();
        rootnode.add(userTextField, 1, 1);

        Label password = new Label("Password");
        rootnode.add(password, 0, 2);

        PasswordField userPassword = new PasswordField();
        rootnode.add(userPassword, 1, 2);
        //a scene must be created as there are scenes in a stage
        Scene mainpage = new Scene(rootnode, 500, 500);

        //setting the stage to this previously created scene
        stage.setScene(mainpage);

        response = new Label("Push a button");

        Button login = new Button("LOGIN");
        Button exit = new Button("EXIT");
        Button signin = new Button("SignIn");

        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                response.setText("Login pressed");
            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                response.setText("Exit pressed");
            }
        });

        signin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                Main_page obj = new Main_page();
                try {
                    if(userTextField.getText().isBlank() && userPassword.getText().isBlank())
                        response.setText("Username/Password not entered!");
                    else
                    authentiction_result = obj.CreateConnection(userTextField.getText(), userPassword.getText());


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (authentiction_result == 1)
                {
                    response.setTextFill(Color.GREEN);
                    response.setText("Successfully signed in!");
                }
                if(authentiction_result==0){
                    response.setTextFill(Color.RED);
                    //response.wrapTextProperty();
                    response.setText("Username/Password entered is incorrect!");
                }


            }
        });

        rootnode.add(signin, 1, 3);
        rootnode.add(response,1,4,1,2);

        //showing the stage
        stage.show();


    }

    //this function is used for establishing connection with our database
    int CreateConnection(String username, String password) throws ClassNotFoundException, SQLException
    {
        int result = 0;
        String db_username = "", db_password = "";
        //loading the jdbc driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        //configuring the driver manager
        //url format is as follows -> jdbc:mysql://"address of server":"port number"/"name of the database,"username of mysql server acount","password for the same server account" <-
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/erp", "root", "Adwaitg7122");

        //created an object of statement type so that we can use it to execute the query of our choice
        Statement stmt = con.createStatement();

        if (username.contentEquals("Administrator"))
        {
            //executequery function of the Statement class is used to pass the query written in MySQL and the result of the query is obtained in the object of ResultSet class object
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM admin_login");


            while (resultSet.next())  //next() function of the ResultSet Class iterates through the rows present in a particular table in our database
            {
                db_username = resultSet.getString("Username"); //getString functionn is used to access the data present in the database whre we have provided the colomn heading as an argument to it.
                db_password = resultSet.getString("Password");
            }


        }
            if (db_password.contentEquals(password) && db_username.contentEquals(username))
            {
                return 1;

            } else {
                return 0;
            }


    }
}



