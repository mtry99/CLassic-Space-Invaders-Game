import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    ImageView playerView;
    double X = 370;
    double X_MAX = 745;
    double X_MIN = 0;

    Player(){
        Image player = new Image("images/player.png");
        playerView = new ImageView(player);
        playerView.setFitHeight(20);
        playerView.setFitWidth(40);
        playerView.setLayoutX(370);
        playerView.setLayoutY(530);
    }

    public ImageView getPlayer(){
        return this.playerView;
    }

    public void moveRight(){
        if (X + 4 < X_MAX){
            X += 4;
            playerView.setLayoutX(X);
        }
    }

    public void moveLeft(){
        if (X - 4 > X_MIN){
            X-= 4;
            playerView.setLayoutX(X);
        }
    }
    public double getPosition(){
        return X;
    }
}
