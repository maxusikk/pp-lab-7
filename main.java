import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public it class Main extends Application {

    private TextField directoryPathField;
    private TextField searchField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Browser and Search");

        directoryPathField = new TextField();
        directoryPathField.setPromptText("Enter directory path");

        searchField = new TextField();
        searchField.setPromptText("Enter search phrase");

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> browseDirectory());

        Button searchButton = new Button("Search");

        HBox hBox = new HBox(10, directoryPathField, browseButton);
        VBox vBox = new VBox(10, hBox, searchField, searchButton);

        Scene scene = new Scene(vBox, 600, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void browseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");

        Stage stage = new Stage();
        stage.initOwner(directoryPathField.getScene().getWindow());

        java.io.File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
