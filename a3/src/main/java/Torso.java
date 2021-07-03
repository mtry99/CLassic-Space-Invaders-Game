import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Torso {
    private ImageView torso;


    Torso(){
        torso = new ImageView (new Image("images/torso.png"));
        torso.setFitHeight(180);
        torso.setFitWidth(120);
        position_center();
    };

    public ImageView getTorso(){
        return this.torso;
    }

    public void position_center(){

        torso.setLayoutX(480);
        torso.setLayoutY(250);
    }




}
