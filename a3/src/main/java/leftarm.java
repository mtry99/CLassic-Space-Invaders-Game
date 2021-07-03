import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class leftarm {
    private ImageView upperarm;
    private ImageView lowerarm;
    private ImageView hand;

    double armRotate = 0;
    double startRotate = 0;

    private Group lowerarm_hand;
    private Group arm;

    leftarm(){
        upperarm = new ImageView (new Image("images/leftupperarm.png"));
        lowerarm = new ImageView (new Image("images/leftlowerarm.png"));
        hand = new ImageView (new Image("images/lefthand.png"));

        upperarm.setFitWidth(77);
        upperarm.setFitHeight(120);

        lowerarm.setFitWidth(72);
        lowerarm.setFitHeight(120);

        hand.setFitWidth(53);
        hand.setFitHeight(80);


        lowerarm_hand = new Group(lowerarm, hand);
        arm = new Group(upperarm, lowerarm_hand);

        position_center();
    };

    public void position_center(){
        upperarm.setLayoutX(426);
        upperarm.setLayoutY(240);

        lowerarm.setLayoutX(426);
        lowerarm.setLayoutY(343);

        hand.setLayoutX(427);
        hand.setLayoutY(410);

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
