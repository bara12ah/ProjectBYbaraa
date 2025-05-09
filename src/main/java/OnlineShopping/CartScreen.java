package OnlineShopping;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class CartScreen {
    private Stage stage;
    private String username;
    private Cart cart;

    public CartScreen(Stage stage, String username, Cart cart) {
        this.stage = stage;
        this.username = username;
        this.cart = cart;
    }
    public void show() {

        BorderPane root = new BorderPane();


        VBox topContainer = new VBox();

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #3498db;");

        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        welcomeLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        topBar.getChildren().addAll(welcomeLabel, spacer, logoutButton);
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(10));
        navBar.setStyle("-fx-background-color: #f8f8f8;");

        Button backButton = new Button("← Continue Shopping");
        Label cartLabel = new Label("Shopping Cart");
        cartLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        navBar.getChildren().addAll(backButton, cartLabel);

        topContainer.getChildren().addAll(topBar, navBar);
        root.setTop(topContainer);


        VBox cartContent = new VBox(20);
        cartContent.setPadding(new Insets(20));


        TableView<Product> cartTable = new TableView<>();
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Product");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(150);

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(100);

        TableColumn<Product, String> madeInColumn = new TableColumn<>("Made In");
        madeInColumn.setCellValueFactory(new PropertyValueFactory<>("madeIn"));
        madeInColumn.setPrefWidth(100);


        TableColumn<Product, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setPrefWidth(100);

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button removeButton = new Button("Remove");

                    {
                        removeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                        removeButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            cart.removeProduct(product);
                            updateTable();
                        });
                    }

                    private void updateTable() {
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(removeButton);
                        }
                    }
                };
            }
        };
        actionColumn.setCellFactory(cellFactory);


        cartTable.getColumns().addAll(nameColumn, categoryColumn, priceColumn, madeInColumn, actionColumn);


        updateTable(cartTable);

        VBox.setVgrow(cartTable, Priority.ALWAYS);


        HBox totalSection = new HBox(20);
        totalSection.setAlignment(Pos.CENTER_RIGHT);

        Label totalLabel = new Label(String.format("Total: $%.2f", cart.getTotalPrice()));
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        totalSection.getChildren().addAll(totalLabel, checkoutButton);


        cartContent.getChildren().addAll(cartTable, totalSection);
        root.setCenter(cartContent);


        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(10));

        Button devInfoButton = new Button("Developer Info");
        bottomBar.getChildren().add(devInfoButton);

        root.setBottom(bottomBar);


        Scene scene = new Scene(root, 800, 600);


        backButton.setOnAction(e -> {
            CategoryScreen categoryScreen = new CategoryScreen(stage, username);
            categoryScreen.show();
        });

        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });
        checkoutButton.setOnAction(e -> {
            if (cart.getItemCount() == 0) {
                showAlert("Empty Cart", "Your cart is empty. Add some products before checkout.");
                return;
            }

            String billFile = cart.generateBill(username);

            if (billFile != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Checkout Successful");
                alert.setHeaderText("Thank you for your purchase!");
                alert.setContentText("Your bill has been saved to " + billFile);
                alert.showAndWait();


                CategoryScreen categoryScreen = new CategoryScreen(stage, username);
                categoryScreen.show();
            } else {
                showAlert("Checkout Error", "Failed to generate bill. Please try again.");
            }
        });

        devInfoButton.setOnAction(e -> {
            showDeveloperInfo();
        });


        stage.setTitle("Online Shopping - Shopping Cart");
        stage.setScene(scene);
        stage.show();
    }

    private void updateTable(TableView<Product> cartTable) {
        List<Product> items = cart.getItems();
        cartTable.setItems(FXCollections.observableArrayList(items));
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