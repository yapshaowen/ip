import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tyrone.Tyrone;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Tyrone tyrone;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image tyroneImage = new Image(this.getClass().getResourceAsStream("/images/DaTyrone.png")); // reuse name or rename file later

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setTyrone(Tyrone t) {
        tyrone = t;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = tyrone.getResponse(input);

        if ("bye".equals(response)) {
            userInput.getScene().getWindow().hide();
            return;
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getTyroneDialog(response, tyroneImage)
        );

        userInput.clear();
    }
}
