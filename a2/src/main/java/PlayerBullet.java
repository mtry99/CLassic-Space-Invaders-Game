import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerBullet {
    ImageView playerBulletView;
    double Y = 508;
    double X = 18;

    PlayerBullet(){
        Image playerBullet = new Image("images/player_bullet.png");
        playerBulletView = new ImageView(playerBullet);
        playerBulletView.setFitHeight(20);
        playerBulletView.setFitWidth(5);
    };


    public void setPosition(double position_X){
        X += position_X;
        playerBulletView.setLayoutX(X);
        playerBulletView.setLayoutY(Y);
    }

    public void moveUp(double position_Y){
        Y -= position_Y;
        playerBulletView.setLayoutX(X);
        playerBulletView.setLayoutY(Y);
    }

    public ImageView getPlayerBullet(){
        return this.playerBulletView;
    }

}
