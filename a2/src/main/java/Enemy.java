import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Enemy {
    ImageView enemyView;
    Image enemy;
    boolean alive = true;
    int enemyID;
    int groupID;
    double X_relative;
    double Y_relative;
    double posX;
    double posY;


    Enemy(int id){
       String imageURL = "images/enemy" + String.valueOf(id) + ".png";
       enemy = new Image(imageURL);
       enemyView = new ImageView(enemy);
       enemyView.setFitHeight(35);
       enemyView.setFitWidth(40);
       enemyID = id;
    }

    public void positionEnemy(double X, double Y){
        enemyView.setX(X);
        enemyView.setY(Y);
    }

    public ImageView getEnemy(){
        return this.enemyView;
    }

    public int getEnemyID(){
        return this.enemyID;
    }

    public void setEnemy(double X, double Y){
        posX = X;
        posY = Y;
    }

    public void incrementEnemyPosBy(double X, double Y){
        posX += X;
        posY += Y;
    }

    public double getX(){
        return this.posX;
    }
    public double getY(){
        return this.posY;
    }

}
