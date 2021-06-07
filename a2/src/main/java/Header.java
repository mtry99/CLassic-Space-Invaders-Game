import javafx.scene.text.Font;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Header {
    Group header;
    private Label displayLevel;
    private Label displayScore;
    private Label displayLives;

    Header(){
        /* Create Header labels */

        displayLevel = new Label("Level: ");
        displayScore = new Label("Score: ");
        displayLives = new Label("Lives: ");

        /* Style Header Labels */
        displayLevel.setFont(new Font("Verdana", 16));
        displayScore.setFont(new Font("Verdana", 16));
        displayLives.setFont(new Font("Verdana", 16));

        displayLevel.setTextFill(Color.WHITE); /* make text white */
        displayScore.setTextFill(Color.WHITE);
        displayLives.setTextFill(Color.WHITE);
        displayLevel.setStyle("-fx-font-weight: bold");
        displayScore.setStyle("-fx-font-weight: bold");
        displayLives.setStyle("-fx-font-weight: bold");

        /* Position Header Labels */
        displayScore.setLayoutX(20);
        displayScore.setLayoutY(10);

        displayLives.setLayoutX(500);
        displayLives.setLayoutY(10);

        displayLevel.setLayoutX(650);
        displayLevel.setLayoutY(10);

        header= new Group (displayLevel, displayScore, displayLives);
    }

    public void updateLevel(int level){
        displayLevel.setText("Level: "+ level);
    }

    public void updateLives(int lives){
        displayLives.setText("Lives: "+ lives);
    }

    public void updateScore(int score){
        displayScore.setText("Score: "+ score);
    }

    public Label getDisplayLevel(){
        return this.displayLevel;
    }
    public Label getDisplayScore(){
        return this.displayScore;
    }
    public Label getDisplayLives(){
        return this.displayLives;
    }
}
