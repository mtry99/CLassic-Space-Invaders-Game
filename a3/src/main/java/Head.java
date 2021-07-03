import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

public class Head {
    private ImageView head;

    double startRotate;
    double startX;



    Head(){
        head = new ImageView (new Image("images/head.png"));
        head.setFitWidth(130);
        head.setFitHeight(100);
        head.setPreserveRatio(true);
        position_center();


    };

    public ImageView getHead(){
        return this.head;
    }

    public void position_center(){
        head.setLayoutY(180);
        head.setLayoutX(493);
    }
}
