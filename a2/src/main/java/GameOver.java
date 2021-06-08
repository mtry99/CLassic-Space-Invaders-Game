import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameOver {
    Group gameOver;

    GameOver(int score, int level, boolean winner){


        /* Style Back Ground */
        Rectangle frame = new Rectangle(390, 240, Color.LIGHTGREY);
        frame.setArcHeight(20);
        frame.setArcWidth(20);

        /* Create Labels */
        Label displayResult = winner ? new Label("YOU WON!") : new Label("GAME OVER!");
        Label displayScore = new Label("Score: " + score);
        Label displayNext;
        if (winner && level == 3){
            displayNext = new Label("ENTER - Play again");
        }else if (winner){
            displayNext = new Label("ENTER - Advance to Level " + (level + 1));
        }else{
            displayNext = new Label ("ENTER - To Retry Level " + level);
        }
        Label displayQuit = new Label("Q - Quit Game");



        /* Style Labels */
        displayResult.setFont(new Font("Verdana", 30));
        displayScore.setFont(new Font("Verdana", 16));
        displayNext.setFont(new Font("Verdana", 16));
        displayQuit.setFont(new Font("Verdana", 16));


        displayResult.setTextFill(Color.BLACK);
        displayScore.setTextFill(Color.BLACK);
        displayNext.setTextFill(Color.BLACK);
        displayQuit.setTextFill(Color.BLACK);


        displayResult.setStyle("-fx-font-weight: bold");


        /* Position Labels */
        frame.setX(220);
        frame.setY(180);

        if (winner){
            displayResult.setLayoutX(330);
            displayResult.setLayoutY(200);
        }else{
            displayResult.setLayoutX(305);
            displayResult.setLayoutY(200);
        }


        displayScore.setLayoutX(380);
        displayScore.setLayoutY(245);

        displayNext.setLayoutX(325);
        displayNext.setLayoutY(290);

        displayQuit.setLayoutX(365);
        displayQuit.setLayoutY(320);

        gameOver = new Group(frame, displayResult, displayScore, displayNext, displayQuit);
    };

    public Group getGameOver(){
        return this.gameOver;
    }
}