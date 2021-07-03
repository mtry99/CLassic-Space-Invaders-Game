import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class leftleg {
    private ImageView upperleg;
    private ImageView lowerleg;
    private ImageView foot;
    private Group leg;
    private Group lowerleg_foot;
    double upperLegRotate = 0;
    double lowerLegRotate = 0;

    leftleg(){
        upperleg = new ImageView (new Image("images/leftupperleg.png"));
        lowerleg = new ImageView (new Image("images/leftlowerleg.png"));
        foot = new ImageView (new Image("images/leftfeet.png"));

        upperleg.setFitWidth(60);
        upperleg.setFitHeight(100);

        lowerleg.setFitWidth(125);
        lowerleg.setFitHeight(120);

        foot.setFitWidth(78);
        foot.setFitHeight(60);

        position_center();
        lowerleg_foot = new Group(lowerleg, foot);

        leg = new Group(upperleg, lowerleg_foot);
    };

    public void position_center(){
        upperleg.setLayoutX(487);
        upperleg.setLayoutY(395);

        lowerleg.setLayoutX(450);
        lowerleg.setLayoutY(485);

        foot.setLayoutX(445);
        foot.setLayoutY(563);
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

    public Group getLowerleg_foot(){
        return this.lowerleg_foot;
    }




}
