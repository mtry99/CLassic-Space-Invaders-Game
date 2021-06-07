import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class TitleScreen{
    private Scene titleScreen;

    TitleScreen(){

        /* Upload Space Invader logo */
        Image logo = new Image("images/logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(170);
        logoView.setFitWidth(400);
        logoView.setLayoutX(200);
        logoView.setLayoutY(10);

        /* Create Instruction Labels */
        Label instructions = new Label("Instructions");
        Label startGame = new Label("ENTER - Start Game");
        Label movement = new Label("A or <, D or > - Move ship left or right");
        Label fire = new Label("SPACE - Fire!");
        Label quitGame = new Label ("Q - Quit Game");
        Label levels = new Label("1 or 2 or 3 - Start Game at Specific Level");
        Label copyRight = new Label("Implemented by Maitry Mistry for CS 349, University of Waterloo, S21");

        /* Style Instruction Labels */
        instructions.setFont(new Font("Verdana", 26));
        instructions.setStyle("-fx-font-weight: bold");
        startGame.setFont(new Font("Verdana", 14));
        movement.setFont(new Font("Verdana", 14));
        fire.setFont(new Font("Verdana", 14));
        quitGame.setFont(new Font("Verdana", 14));
        levels.setFont(new Font("Verdana", 14));
        copyRight.setFont(new Font("Verdana", 11));

        /* Position Instruction Labels */
        instructions.setLayoutX(300);
        instructions.setLayoutY(240);

        startGame.setLayoutX(320);
        startGame.setLayoutY(300);

        movement.setLayoutX(250);
        movement.setLayoutY(325);

        fire.setLayoutX(340);
        fire.setLayoutY(350);

        quitGame.setLayoutX(340);
        quitGame.setLayoutY(375);

        levels.setLayoutX(240);
        levels.setLayoutY(400);

        copyRight.setLayoutX(200);
        copyRight.setLayoutY(520);

        /* Set scene */
        Group root = new Group(logoView, instructions, startGame, movement, fire, quitGame, levels, copyRight);
        titleScreen = new Scene(root);
    };


    public Scene getScene(){
        return this.titleScreen;
    }
}
