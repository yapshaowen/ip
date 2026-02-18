import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import tyrone.Tyrone;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Main extends Application {

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image tyroneImage = new Image(this.getClass().getResourceAsStream("/images/DaTyrone.png"));
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private final Tyrone tyrone = new Tyrone(Paths.get("data", "tyrone.txt"));

    /**
     * Initializes and displays the main GUI window
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        initComponents();
        AnchorPane mainLayout = buildLayout();
        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        configureStage(stage, mainLayout);
        configureLayout(mainLayout);
        configureHandlers();
        autoScroll();
    }

    /**
     * Creates and initializes the core UI components used by the GUI
     */
    private void initComponents() {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");
    }

    /**
     * Constructs the root layout node for the GUI
     */
    private AnchorPane buildLayout() {
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);
        return mainLayout;
    }

    /**
     * Configures the main window properties
     *
     * @param stage
     * @param mainLayout
     */
    private void configureStage(Stage stage, AnchorPane mainLayout) {
        stage.setTitle("Tyrone");
        stage.setResizable(true);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);
    }

    /**
     * Configures the sizing, scrolling behaviour of the GUI components
     *
     * @param mainLayout
     */
    private void configureLayout(AnchorPane mainLayout) {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        AnchorPane.setTopAnchor(scrollPane, 10.0);
        AnchorPane.setLeftAnchor(scrollPane, 10.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0);
        AnchorPane.setBottomAnchor(scrollPane, 55.0);

        AnchorPane.setBottomAnchor(sendButton, 10.0);
        AnchorPane.setRightAnchor(sendButton, 10.0);

        AnchorPane.setLeftAnchor(userInput, 10.0);
        AnchorPane.setBottomAnchor(userInput, 10.0);
        AnchorPane.setRightAnchor(userInput, 80.0);
    }

    /**
     * Attaches event handlers for user input submission
     */
    private void configureHandlers() {
        sendButton.setOnMouseClicked(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());
    }

    /**
     * Enables auto-scrolling to the bottom of the dialog container
     */
    private void autoScroll() {
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Creates a dialog box containing user input, and appends it to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        String userText = userInput.getText();
        String tyroneText = tyrone.getResponse(userInput.getText());
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getTyroneDialog(tyroneText, tyroneImage)
        );
        userInput.clear();
    }
}
