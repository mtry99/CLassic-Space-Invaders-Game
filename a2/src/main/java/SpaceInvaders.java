
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class SpaceInvaders extends Application{
    float SCREEN_WIDTH = 800;
    float SCREEN_HEIGHT = 600;

    @Override
    public void start(Stage stage) {
        // Set stage properties
        stage.setTitle("Space Invaders");
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setResizable(false);

        //get titleScreen
        Scene titleScreen = new TitleScreen().getScene();


        //get gameScreen
        GameScreen game = new GameScreen();
        Scene gameScreen = game.getScene();

        //Handle titleScreen Events
        titleScreen.setOnKeyPressed(keyEvent -> {
           if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.DIGIT1) {               //Level 1
               game.setLevel(1);
               game.populateGameScreen();
               stage.setScene(gameScreen);
           }else if (keyEvent.getCode() == KeyCode.DIGIT2) {                                                //Level 2
               game.setLevel(2);
               game.populateGameScreen();
               stage.setScene(gameScreen);
           }else if (keyEvent.getCode() == KeyCode.DIGIT3){                                                  //Level 3
               game.setLevel(3);
               game.populateGameScreen();
               stage.setScene(gameScreen);
           }else if (keyEvent.getCode() == KeyCode.Q){                                                      //Quit Game
               System.exit(0);
           }
        });

        stage.setScene(titleScreen);
        stage.show();

    }
}
