package OnlineShopping;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class LoginScreen {
    private Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Text title = new Text("Welcome to Online Shopping");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Text subtitle = new Text("Enter your username and password to continue");
        subtitle.setFont(Font.font("Arial", 14));


        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        form.add(usernameLabel, 0, 0);
        form.add(usernameField, 1, 0);
        form.add(passwordLabel, 0, 1);
        form.add(passwordField, 1, 1);
        form.add(loginButton, 1, 2);
        GridPane.setMargin(loginButton, new Insets(10, 0, 0, 0));


        Text footer = new Text("* This application is a course project for SWER142.");
        footer.setFont(Font.font("Arial", 12));
        footer.setFill(Color.GRAY);


        root.getChildren().addAll(title, subtitle, form, footer);


        Scene scene = new Scene(root, 400, 350);


        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();


            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Login Error", "Missing Credentials", "Please enter both username and password.");
                return;
            }


            List<User> users = FileHandler.readUsers("C:\\Users\\User\\IdeaProjects\\Lab10\\src\\main\\resources\\USERS.txt");

            for (User user : users) {
                System.out.println(user);
                if (user.getUsername().equals(username) && user.authenticate(password)) {

                    CategoryScreen categoryScreen = new CategoryScreen(stage, username);
                    categoryScreen.show();
                    return;
                }
            }

            showAlert("Login Error", "Invalid Credentials", "The username or password you entered is incorrect.");
        });


        stage.setTitle("Online Shopping - Login");
        stage.setScene(scene);
        stage.show();
    }
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

}}