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
import java.util.stream.Collectors;

public class ProductScreen {
    private Stage stage;
    private String username;
    private Category category;
    private Cart cart;

    public ProductScreen(Stage stage, String username, Category category, Cart cart) {
        this.stage = stage;
        this.username = username;
        this.category = category;
        this.cart = cart;
    }

//    public ProductScreen(Stage stage, String username, OnlineShopping.Category category, OnlineShopping.Cart cart) {
//    }

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

        // Create navigation bar
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(10));
        navBar.setStyle("-fx-background-color: #f8f8f8;");

        Button backButton = new Button("← Back to Categories");
        Label categoryLabel = new Label(category.getName());
        categoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        navBar.getChildren().addAll(backButton, categoryLabel);

        topContainer.getChildren().addAll(topBar, navBar);
        root.setTop(topContainer);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        GridPane productsGrid = new GridPane();
        productsGrid.setHgap(20);
        productsGrid.setVgap(20);
        productsGrid.setAlignment(Pos.CENTER);
        productsGrid.setPadding(new Insets(20));

        // Load and filter products
        List<Product> allProducts = FileHandler.readProducts("C:\\Users\\User\\IdeaProjects\\Lab10\\src\\main\\resources\\Products.txt");
        List<Product> categoryProducts = allProducts.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category.getName()))
                .collect(Collectors.toList());

        int row = 0;
        int col = 0;
        for (Product product : categoryProducts) {
            VBox productBox = createProductBox(product);
            productsGrid.add(productBox, col, row);

            col++;
            if (col > 2) { // 3 columns max
                col = 0;
                row++;
            }
        }

        scrollPane.setContent(productsGrid);
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
        backButton.setOnAction(e -> {
            CategoryScreen categoryScreen = new CategoryScreen(stage, username);
            categoryScreen.show();
        });

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
        stage.setTitle("Online Shopping - " + category.getName() + " Products");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createProductBox(Product product) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ddd; -fx-background-color: white; -fx-border-radius: 5;");
        box.setPrefWidth(180);
        box.setPrefHeight(230);
        box.setAlignment(Pos.CENTER);

        try {
            File file = new File("C:\\Users\\User\\IdeaProjects\\Lab10\\src\\main\\resources\\" + product.getImageName());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(160);
                imageView.setFitHeight(120);
                imageView.setPreserveRatio(true);
                box.getChildren().add(imageView);
            } else {
                Label noImage = new Label("No Image");
                noImage.setPrefHeight(120);
                noImage.setPrefWidth(160);
                noImage.setAlignment(Pos.CENTER);
                noImage.setStyle("-fx-background-color: #f8f8f8;");
                box.getChildren().add(noImage);
            }
        } catch (Exception e) {
            Label noImage = new Label("No Image");
            noImage.setPrefHeight(120);
            noImage.setPrefWidth(160);
            noImage.setAlignment(Pos.CENTER);
            noImage.setStyle("-fx-background-color: #f8f8f8;");
            box.getChildren().add(noImage);
        }
        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));


        Label priceLabel = new Label("$" + product.getPrice());
        priceLabel.setTextFill(Color.RED);
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));


        Label originLabel = new Label("Made in " + product.getMadeIn());
        originLabel.setFont(Font.font("Arial", 12));
        originLabel.setTextFill(Color.GRAY);


        Button addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        addButton.setPrefWidth(140);

        addButton.setOnAction(e -> {
            cart.addProduct(product);
            showAlert("Added to Cart", product.getName() + " has been added to your cart.");


            ProductScreen productScreen = new ProductScreen(stage, username, category, cart);
            productScreen.show();
        });

        box.getChildren().addAll(nameLabel, priceLabel, originLabel, addButton);

        return box;
    }

    private void showDeveloperInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Developer Information");
        alert.setHeaderText("Online Shopping Application");
        alert.setContentText("Developed by: Your Name\nEmail: your.email@example.com\n\nSWER142 Course Project - Bethlehem University");
        alert.showAndWait();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
}
}
