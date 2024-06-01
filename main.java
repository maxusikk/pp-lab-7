import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main extends Application {

    private TextField directoryPathField;
    private TextField searchField;
    private TextArea resultArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Browser and Search");

        directoryPathField = new TextField();
        directoryPathField.setPromptText("Enter directory path");

        searchField = new TextField();
        searchField.setPromptText("Enter search phrase");

        resultArea = new TextArea();
        resultArea.setPrefHeight(400);

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> browseDirectory());

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchFiles());

        HBox hBox = new HBox(10, directoryPathField, browseButton);
        VBox vBox = new VBox(10, hBox, searchField, searchButton, resultArea);

        Scene scene = new Scene(vBox, 600, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void browseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");

        Stage stage = new Stage();
        stage.initOwner(directoryPathField.getScene().getWindow());

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void searchFiles() {
        String directoryPath = directoryPathField.getText().trim();
        if (directoryPath.isEmpty()) {
            resultArea.setText("Please provide a directory path.");
            return;
        }

        String searchPhrase = searchField.getText().trim();
        if (searchPhrase.isEmpty()) {
            resultArea.setText("Please enter a search phrase.");
            return;
        }

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            resultArea.setText("The provided path is not a directory.");
            return;
        }

        StringBuilder results = new StringBuilder();
        try {
            searchInDirectory(directory, searchPhrase, results);
        } catch (IOException e) {
            e.printStackTrace();
            resultArea.setText("Error occurred while searching files.");
            return;
        }

        resultArea.setText(results.toString());
    }

    private boolean containsPhrase(File file, String searchPhrase) throws IOException {
        try {
            return Files.lines(file.toPath()).anyMatch(line -> line.contains(searchPhrase));
        } catch (IOException e) {
            throw new IOException("Error reading file: " + file.getAbsolutePath(), e);
        }
    }

    private void searchInDirectory(File directory, String searchPhrase, StringBuilder results) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchInDirectory(file, searchPhrase, results);
                } else {
                    if (containsPhrase(file, searchPhrase)) {
                        results.append(file.getAbsolutePath()).append("\n");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
