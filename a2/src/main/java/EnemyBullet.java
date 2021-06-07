import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EnemyBullet {
    ImageView enemyBulletView;
    double position_X;
    double position_Y;

    EnemyBullet(int id){
        String imageURL = "images/bullet" + String.valueOf(id) + ".png";
        Image enemyBullet = new Image(imageURL);
        enemyBulletView = new ImageView(enemyBullet);
        enemyBulletView.setFitHeight(25);
        enemyBulletView.setFitWidth(10);
    };

    public ImageView getEnemyBulletView(){
        return this.enemyBulletView;
    }

    public void setPosition(double X, double Y){
        position_X = X;
        position_Y = Y;
        enemyBulletView.setLayoutX(position_X);
        enemyBulletView.setLayoutY(position_Y);
    }

    public void moveDown(double Y){
        position_Y += Y;
        enemyBulletView.setLayoutX(position_X);
        enemyBulletView.setLayoutY(position_Y);
    }
}
