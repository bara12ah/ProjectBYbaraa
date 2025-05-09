package OnlineShopping;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class CategoryScreen {
    private Stage stage;
    private String username;
    private static Cart cart = new Cart();

    public CategoryScreen(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
    }

    public void show() {
        // Create root layout
        BorderPane root = new BorderPane();

        // Create top bar
        VBox topContainer = new VBox();

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #3498db;");

        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        welcomeLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button cartButton = new Button("Cart (" + cart.getItemCount() + ")");
        cartButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        topBar.getChildren().addAll(welcomeLabel, spacer, cartButton, logoutButton);
        topContainer.getChildren().add(topBar);

        // Set top region
        root.setTop(topContainer);

        // Create a scroll pane for the category grid
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Create a grid for categories
        GridPane categoryGrid = new GridPane();
        categoryGrid.setHgap(20);
        categoryGrid.setVgap(20);
        categoryGrid.setAlignment(Pos.CENTER);
        categoryGrid.setPadding(new Insets(20));

        // Load categories
        List<Category> categories = FileHandler.readCategories("C:\\Users\\User\\IdeaProjects\\Lab10\\src\\main\\resources\\Categories.txt");

        int row = 0;
        int col = 0;
        for (Category category : categories) {
            VBox categoryBox = createCategoryBox(category);
            categoryGrid.add(categoryBox, col, row);

            col++;
            if (col > 2) { // 3 columns max
                col = 0;
                row++;
            }
        }

        scrollPane.setContent(categoryGrid);
        root.setCenter(scrollPane);

        // Create bottom bar
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(10));

        Button devInfoButton = new Button("Developer Info");
        bottomBar.getChildren().add(devInfoButton);

        root.setBottom(bottomBar);

        // Create scene
        Scene scene = new Scene(root, 800, 600);
        cartButton.setOnAction(e -> {
            CartScreen cartScreen = new CartScreen(stage, username, cart);
            cartScreen.show();
        });

        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });

        devInfoButton.setOnAction(e -> {
            showDeveloperInfo();
        });

        // Set stage
        stage.setTitle("Online Shopping - Categories");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createCategoryBox(Category category) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ddd; -fx-background-color: white; -fx-border-radius: 5;");
        box.setPrefWidth(180);
        box.setPrefHeight(200);
        box.setAlignment(Pos.CENTER);


        try {
            File file = new File("C:\\Users\\User\\IdeaProjects\\Lab10\\src\\main\\resources\\" + category.getImageName());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(160);
                imageView.setFitHeight(140);
                imageView.setPreserveRatio(true);
                box.getChildren().add(imageView);
            } else {
                Label noImage = new Label("No Image");
                noImage.setPrefHeight(140);
                noImage.setPrefWidth(160);
                noImage.setAlignment(Pos.CENTER);
                noImage.setStyle("-fx-background-color: #f8f8f8;");
                box.getChildren().add(noImage);
            }
        } catch (Exception e) {
            Label noImage = new Label("No Image");
            noImage.setPrefHeight(140);
            noImage.setPrefWidth(160);
            noImage.setAlignment(Pos.CENTER);
            noImage.setStyle("-fx-background-color: #f8f8f8;");
            box.getChildren().add(noImage);
        }


        Label nameLabel = new Label(category.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        box.getChildren().add(nameLabel);


        box.setOnMouseClicked(e -> {
            ProductScreen productScreen = new ProductScreen(stage, username, category, cart);
            productScreen.show();
        });
        box.setOnMouseEntered(e -> {
            box.setStyle("-fx-border-color: #3498db; -fx-background-color: #f8f8f8; -fx-border-radius: 5;");
            box.setCursor(javafx.scene.Cursor.HAND);
        });

        box.setOnMouseExited(e -> {
            box.setStyle("-fx-border-color: #ddd; -fx-background-color: white; -fx-border-radius: 5;");
        });

        return box;
    }

    private void showDeveloperInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Developer Information");
        alert.setHeaderText("Online Shopping Application");
        alert.setContentText("Developed by:  Bara'a Sbaih & Mahmmoud Farhan\n Email: Bara'a@email.com & Mahmmoud@email.com\n\nSWER142 Course Project - Bethlehem University");
        alert.showAndWait();
    }
}
