module com.example.lab10 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens OnlineShopping.lecture to javafx.fxml;
    exports OnlineShopping.lecture;
    exports OnlineShopping;
    opens OnlineShopping to javafx.fxml;
}