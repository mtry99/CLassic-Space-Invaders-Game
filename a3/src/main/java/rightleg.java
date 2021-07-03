import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class rightleg {
    private ImageView upperleg;
    private ImageView lowerleg;
    private ImageView foot;
    private Group leg;
    private Group lowerleg_foot;
    double upperLegRotate = 0;
    double lowerLegRotate = 0;

    rightleg(){
        upperleg = new ImageView (new Image("images/rightupperleg.png"));
        lowerleg = new ImageView (new Image("images/rightlowerleg.png"));
        foot = new ImageView (new Image("images/rightfeet.png"));

        upperleg.setFitWidth(75);
        upperleg.setFitHeight(100);

        lowerleg.setFitWidth(97);
        lowerleg.setFitHeight(120);

        foot.setFitWidth(85);
        foot.setFitHeight(60);

        position_center();
        lowerleg_foot = new Group(lowerleg,foot);
        leg = new Group(upperleg, lowerleg_foot);
    };

    public void position_center(){
        upperleg.setLayoutX(546);
        upperleg.setLayoutY(388);

        lowerleg.setLayoutX(563);
        lowerleg.setLayoutY(480);

        foot.setLayoutX(595);
        foot.setLayoutY(550);
    }

    public ImageView getUpperleg(){
        return this.upperleg;
    }

    public ImageView getLowerleg(){
        return this.lowerleg;
    }

    public ImageView getFoot(){
        return this.foot;
    }

    public Group getLeg(){
        return this.leg;
    }

    public Group getLowerleg_foot(){ return this.lowerleg_foot;}

}
