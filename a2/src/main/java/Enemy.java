import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Enemy {
    ImageView enemyView;
    Image enemy;
    boolean alive = true;
    int enemyID;
    int groupID;
    double posX;
    double posY;


    Enemy(int id){
       String imageURL = "images/enemy" + String.valueOf(id) + ".png";
       enemy = new Image(imageURL, 38, 55, true, true);
       enemyView = new ImageView(enemy);
       enemyID = id;
    }

    public void positionEnemy(double X, double Y){
        posX = X;
        posY = Y;
        enemyView.setX(X);
        enemyView.setY(Y);
    }

    public ImageView getEnemy(){
        return this.enemyView;
    }

    public int getEnemyID(){
        return this.enemyID;
    }


    public void incrementEnemyPosBy(double X, double Y){
        posX += X;
        posY += Y;
        enemyView.setX(posX);
        enemyView.setY(posY);
    }

    public double getX(){
        return this.posX;
    }
    public double getY(){
        return this.posY;
    }

}
