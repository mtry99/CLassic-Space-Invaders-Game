import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class rightarm {
    private ImageView upperarm;
    private ImageView lowerarm;
    private ImageView hand;
    double startRotate = 0;
    double armRotate = 0;
    private Group lowerarm_hand;
    private Group arm;

    rightarm(){
        upperarm = new ImageView (new Image("images/rightupperarm.png"));
        lowerarm = new ImageView (new Image("images/rightlowerarm.png"));
        hand = new ImageView (new Image("images/righthand.png"));

        upperarm.setFitWidth(75);
        upperarm.setFitHeight(115);

        lowerarm.setFitWidth(70);
        lowerarm.setFitHeight(110);

        hand.setFitWidth(52);
        hand.setFitHeight(60);

        position_center();
        lowerarm_hand = new Group(lowerarm, hand);

        arm = new Group(upperarm, lowerarm_hand);
    };

    public void position_center(){
        upperarm.setLayoutX(597);
        upperarm.setLayoutY(243);

        lowerarm.setLayoutX(597);
        lowerarm.setLayoutY(320);

        hand.setLayoutX(603);
        hand.setLayoutY(395);
    }

    public ImageView getUpperarm(){
        return this.upperarm;
    }

    public ImageView getLowerarm(){
        return this.lowerarm;
    }

    public ImageView getHand(){
        return this.hand;
    }

    public Group getArm(){
        return this.arm;
    }

    public Group getLowerarm_hand(){ return this.lowerarm_hand; }

}
