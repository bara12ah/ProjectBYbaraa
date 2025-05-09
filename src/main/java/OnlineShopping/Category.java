package OnlineShopping;

import javafx.stage.Stage;

import java.util.List;

public class Category {
    private String name;
    private String imageName;

    public Category(String name, String imageName) {
        this.name = name;
        this.imageName = imageName;
    }

    public Category(Stage primaryStage, String testUser) {
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;

}

    public void show() {
    }

//    public void setCategory(List<OnlineShopping.Category> sampleCategories) {
//    }
}
