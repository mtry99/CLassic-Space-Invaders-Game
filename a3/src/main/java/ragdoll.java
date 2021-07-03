import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

import java.awt.*;

public class ragdoll extends Application {
    float SCREEN_WIDTH = 1024;
    float SCREEN_HEIGHT = 768;
    final KeyCombination ctrl_q = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrl_r = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);


    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    double startX, startY, pivotX, pivotY;
    double scalePivotYBefore = 0;

    double startRotate = 0;
    Point2D pivot;


    menu menuC;
    VBox menu;
    Torso torsoC;
    ImageView torso;
    Head headC;
    ImageView head;
    leftarm leftarmC;
    Group leftarm;
    rightarm rightarmC;
    Group rightarm;
    leftleg leftlegC;
    rightleg rightlegC;
    Group leftleg;
    Group rightleg;
    Group window;
    Group top;
    Scene screen;


    public void reset(){

        top.getChildren().remove(1);
        for (int i =0; i < 6; i++){
            window.getChildren().remove(0);
        }
        window = new Group();
        window.getChildren().addAll(torso, head,leftleg, rightleg,leftarm,rightarm);

      //Translate to center
        torsoC.position_center();
        headC.position_center();
        leftarmC.position_center();
        rightarmC.position_center();
        leftlegC.position_center();
        rightlegC.position_center();

        //Undo Rotations
        head.setRotate(0);

        pivotX = leftarmC.getUpperarm().getBoundsInParent().getCenterX();
        pivotY = leftarmC.getUpperarm().getBoundsInParent().getCenterY();
        leftarmC.getArm().getTransforms().add(new Rotate(leftarmC.armRotate  * -1 , pivotX, pivotY));
        leftarmC.armRotate = 0;


        pivotX = leftarmC.getLowerarm().getBoundsInParent().getCenterX();
        pivotY = leftarmC.getLowerarm().getBoundsInParent().getCenterY() - 50;
        leftarmC.getLowerarm_hand().getTransforms().add(new Rotate(leftarmC.startRotate * -1, pivotX, pivotY));
        leftarmC.startRotate = 0;
        leftarmC.getHand().setRotate(0);

        pivotX = rightarmC.getUpperarm().getBoundsInParent().getCenterX();
        pivotY = rightarmC.getUpperarm().getBoundsInParent().getCenterY();
        rightarmC.getArm().getTransforms().add(new Rotate(rightarmC.armRotate * -1, pivotX, pivotY));
        rightarmC.armRotate = 0;

        pivotX = rightarmC.getLowerarm().getBoundsInParent().getCenterX() - 6;
        pivotY = rightarmC.getLowerarm().getBoundsInParent().getCenterY() - 25;
        rightarmC.getLowerarm_hand().getTransforms().add(new Rotate(rightarmC.startRotate * -1, pivotX, pivotY));
        rightarmC.startRotate = 0;
        rightarmC.getHand().setRotate(0);
        rightarmC.getHand().setRotate(0);


        pivotX = leftlegC.getUpperleg().getBoundsInParent().getCenterX() + 15;
        pivotY = leftlegC.getUpperleg().getBoundsInParent().getCenterY() - 25;
        leftleg.getTransforms().add(new Rotate(leftlegC.upperLegRotate * -1 , pivotX, pivotY));
        leftlegC.upperLegRotate = 0;

        pivotX = leftlegC.getLowerleg().getBoundsInParent().getCenterX() + 20;
        pivotY = leftlegC.getLowerleg().getBoundsInParent().getCenterY() - 55;
        leftlegC.getLowerleg_foot().getTransforms().add(new Rotate(leftlegC.lowerLegRotate * -1, pivotX, pivotY));
        leftlegC.lowerLegRotate = 0;

        leftlegC.getFoot().setRotate(0);

        pivotX = rightlegC.getUpperleg().getBoundsInParent().getCenterX() - 20;
        pivotY = rightlegC.getUpperleg().getBoundsInParent().getCenterY() - 38;
        rightleg.getTransforms().add(new Rotate(rightlegC.upperLegRotate * -1 , pivotX, pivotY));
        rightlegC.upperLegRotate = 0;

        pivotX = rightlegC.getLowerleg().getBoundsInParent().getCenterX() - 20;
        pivotY = rightlegC.getLowerleg().getBoundsInParent().getCenterY() - 60;
        rightlegC.getLowerleg_foot().getTransforms().add(new Rotate(rightlegC.lowerLegRotate * -1, pivotX, pivotY));
        rightlegC.lowerLegRotate = 0;

        rightlegC.getFoot().setRotate(0);

        //Fix Scaling
        leftlegC.getUpperleg().setScaleX(1);
        leftlegC.getUpperleg().setScaleY(1);
        leftlegC.getLowerleg().setScaleX(1);
        leftlegC.getLowerleg().setScaleY(1);
        rightlegC.getUpperleg().setScaleX(1);
        rightlegC.getUpperleg().setScaleY(1);
        rightlegC.getLowerleg().setScaleX(1);
        rightlegC.getLowerleg().setScaleY(1);




        top.getChildren().add(window);
    }

    @Override
    public void start(Stage stage) {

        // Set stage properties
        stage.setTitle("Ragdoll");
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setResizable(false);

        // Get Menu
        menuC = new menu();
        menu = menuC.getScene();
        torsoC = new Torso();
        torso = torsoC.getTorso();
        headC = new Head();
        head = headC.getHead();
        leftarmC = new leftarm();
        leftarm = leftarmC.getArm();
        rightarmC = new rightarm();
        rightarm = rightarmC.getArm();
        leftlegC = new leftleg();
        leftleg = leftlegC.getLeg();
        rightlegC = new rightleg();
        rightleg = rightlegC.getLeg();

        window = new Group();
        top = new Group();
        window.getChildren().addAll(torso, head, leftleg, rightleg, leftarm, rightarm);

        top.getChildren().addAll(menu, window);
        screen = new Scene(top);

        // Add key event handlers on menu screen
        screen.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                 if (ctrl_q.match(event)){
                     System.exit(0);
                 }
                 if (ctrl_r.match(event)){
                     reset();
                 }
            }
        });

        menuC.getReset().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reset();
            }
        });
        // TRANSLATION
        torso.setOnMousePressed( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();

                orgTranslateX = window.getTranslateX();
                orgTranslateY = window.getTranslateY();

            }
        });

        torso.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                double offsetX = t.getSceneX() - orgSceneX;
                double offsetY = t.getSceneY() - orgSceneY;


                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                window.setTranslateX(newTranslateX);
                window.setTranslateY(newTranslateY);

            }

        });

        //ROTATION
        head.setOnMousePressed(event -> {
            Point2D pt = head.localToParent(event.getX(), event.getY());
            headC.startX = pt.getX();
            headC.startRotate = head.getRotate();
        });

        head.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = head.localToParent(event.getX(), event.getY());
                double x = pt.getX();
                double newRotate = (x - headC.startX) + headC.startRotate;
                if (newRotate <= 50 && newRotate >= -50){
                    head.setRotate(newRotate);
                }

            }
        });

        leftarmC.getUpperarm().setOnMousePressed(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = leftarmC.getUpperarm().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();
                pivotX = leftarmC.getUpperarm().getBoundsInParent().getCenterX();
                pivotY = leftarmC.getUpperarm().getBoundsInParent().getCenterY();


            }
        });

        leftarmC.getUpperarm().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = leftarmC.getUpperarm().localToParent(event.getX(), event.getY());
                double x = pt.getX();
                double y = pt.getY();
                double newRotate = x - startX;

                leftarm.getTransforms().add(new Rotate(newRotate, pivotX, pivotY));
                leftarmC.armRotate += newRotate;

            }
        });

        rightarmC.getUpperarm().setOnMousePressed(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = rightarmC.getUpperarm().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();
                pivotX = rightarmC.getUpperarm().getBoundsInParent().getCenterX();
                pivotY = rightarmC.getUpperarm().getBoundsInParent().getCenterY();


            }
        });

        rightarmC.getUpperarm().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = rightarmC.getUpperarm().localToParent(event.getX(), event.getY());
                double x = pt.getX();
                double y = pt.getY();
                double newRotate = x - startX;

                rightarm.getTransforms().add(new Rotate(newRotate, pivotX, pivotY,0));
                rightarmC.armRotate += newRotate;
            }
        });

        rightarmC.getLowerarm().setOnMousePressed(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = rightarmC.getLowerarm().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();
                pivotX = rightarmC.getLowerarm().getBoundsInParent().getCenterX();
                pivotY = rightarmC.getLowerarm().getBoundsInParent().getCenterY();
                pivotX -= 6;
                pivotY -= 25;

            }

        });

        rightarmC.getLowerarm().setOnMouseDragged(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = rightarmC.getLowerarm().localToParent(t.getX(), t.getY());
                double newRotate = startX - pt.getX();
                if ((rightarmC.startRotate + newRotate) >= -135 && (rightarmC.startRotate + newRotate <= 135)){
                    rightarmC.getLowerarm_hand().getTransforms().add(new Rotate(newRotate, pivotX, pivotY));
                    rightarmC.startRotate += newRotate;
                }

            }

        });

        leftarmC.getLowerarm().setOnMousePressed(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = leftarmC.getLowerarm().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();

                pivotX = leftarmC.getLowerarm().getBoundsInParent().getCenterX();
                pivotY = leftarmC.getLowerarm().getBoundsInParent().getCenterY();
                pivotY -= 50;

            }

        });

        leftarmC.getLowerarm().setOnMouseDragged(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = leftarmC.getLowerarm().localToParent(t.getX(), t.getY());
                double newRotate = startX - pt.getX();
                if ((leftarmC.startRotate + newRotate) >= -135 && (leftarmC.startRotate + newRotate <= 135)){
                    leftarmC.getLowerarm_hand().getTransforms().add(new Rotate(newRotate, pivotX, pivotY));
                    leftarmC.startRotate += newRotate;
                }
            }

        });

        leftarmC.getHand().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                Point2D pt = leftarmC.getHand().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startRotate = leftarmC.getHand().getRotate();
            }

        });

        leftarmC.getHand().setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = leftarmC.getHand().localToParent(event.getX(), event.getY());
                double x = pt.getX();
                double newRotate = (x - startX) + startRotate;
                if (newRotate <= 35 && newRotate >= -35){
                    leftarmC.getHand().setRotate(newRotate);
                }

            }
        });

        rightarmC.getHand().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t){
                Point2D pt = rightarmC.getHand().localToParent(t.getX(), t.getY());
                pivotX = rightarmC.getHand().getBoundsInParent().getCenterX();
                pivotY = rightarmC.getHand().getBoundsInParent().getCenterY();
                startX = pt.getX();
                startRotate = rightarmC.getHand().getRotate();
            }

        });

        rightarmC.getHand().setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = rightarmC.getHand().localToParent(event.getX(), event.getY());
                double x = pt.getX();
                double newRotate = (x - startX) + startRotate;

                if (newRotate <= 35 && newRotate >= -35){
                    rightarmC.getHand().setRotate(newRotate);
                }

            }
        });

        leftlegC.getUpperleg().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = leftlegC.getUpperleg().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();
                pivotX = leftlegC.getUpperleg().getBoundsInParent().getCenterX() + 15;
                pivotY = leftlegC.getUpperleg().getBoundsInParent().getCenterY() - 25;
               // scalePivotYBefore = pivotX -

            }
        });

        leftlegC.getUpperleg().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = leftlegC.getUpperleg().localToParent(event.getX(), event.getY());

                double newRotate = startX - pt.getX();

                if ((newRotate + leftlegC.upperLegRotate) >= -90 && (newRotate + leftlegC.upperLegRotate) <= 90){
                    leftleg.getTransforms().add(new Rotate( newRotate, pivotX, pivotY));
                    leftlegC.upperLegRotate += newRotate;
                }


                if ((pt.getY() - startY) > 5){
                    if (leftlegC.getUpperleg().getScaleX() < 6 && leftlegC.getUpperleg().getScaleY() < 6){
                        leftlegC.getUpperleg().setScaleX(leftlegC.getUpperleg().getScaleX() + 0.01);
                        leftlegC.getUpperleg().setScaleY(leftlegC.getUpperleg().getScaleY() + 0.01);
                    }

                    if (leftlegC.getLowerleg().getScaleX() < 5 && leftlegC.getLowerleg().getScaleY() < 5){
                        leftlegC.getLowerleg().setScaleX(leftlegC.getLowerleg().getScaleX() + 0.005);
                        leftlegC.getLowerleg().setScaleY(leftlegC.getLowerleg().getScaleY() + 0.005);
                    }

                }


                else if ((pt.getY() - startY) < 0){
                    if (leftlegC.getUpperleg().getScaleX() > 1 && leftlegC.getUpperleg().getScaleY() > 1){
                        leftlegC.getUpperleg().setScaleX(leftlegC.getUpperleg().getScaleX() - 0.05);
                        leftlegC.getUpperleg().setScaleY(leftlegC.getUpperleg().getScaleY() - 0.05);
                    }

                    if (leftlegC.getLowerleg().getScaleX() > 1 && leftlegC.getLowerleg().getScaleY() > 1){
                        leftlegC.getLowerleg().setScaleX(leftlegC.getLowerleg().getScaleX() - 0.025);
                        leftlegC.getLowerleg().setScaleY(leftlegC.getLowerleg().getScaleY() - 0.025);
                    }

                }

            }
        });

        leftlegC.getLowerleg().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = leftlegC.getLowerleg().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();

                pivotX = leftlegC.getLowerleg().getBoundsInParent().getCenterX() + 20;
                pivotY = leftlegC.getLowerleg().getBoundsInParent().getCenterY() - 55;
            }
        });

        leftlegC.getLowerleg().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = leftlegC.getLowerleg().localToParent(event.getX(), event.getY());

                double newRotate = startX - pt.getX();

                if ((newRotate + leftlegC.lowerLegRotate) >= -90 && (newRotate + leftlegC.lowerLegRotate) <= 90){
                    leftlegC.getLowerleg_foot().getTransforms().add(new Rotate(newRotate, pivotX, pivotY));
                    leftlegC.lowerLegRotate += newRotate;
                }

                if ((pt.getY() - startY) > 5){
                    if (leftlegC.getLowerleg().getScaleX() < 5 && leftlegC.getLowerleg().getScaleY() < 5){
                        leftlegC.getLowerleg().setScaleX(leftlegC.getLowerleg().getScaleX() + 0.005);
                        leftlegC.getLowerleg().setScaleY(leftlegC.getLowerleg().getScaleY() + 0.005);
                    }
                }


                else if ((pt.getY() - startY) < 0){
                    if (leftlegC.getLowerleg().getScaleX() > 1 && leftlegC.getLowerleg().getScaleY() > 1){
                        leftlegC.getLowerleg().setScaleX(leftlegC.getLowerleg().getScaleX() - 0.025);
                        leftlegC.getLowerleg().setScaleY(leftlegC.getLowerleg().getScaleY() - 0.025);
                    }
                }

            }
        });

        leftlegC.getFoot().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = leftlegC.getFoot().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();


            }
        });

        leftlegC.getFoot().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = leftlegC.getFoot().localToParent(event.getX(), event.getY());

                double newRotate = (pt.getX() - startX);
                if (newRotate  >= -35 && newRotate <= 35){
                    leftlegC.getFoot().setRotate(newRotate);

                }

            }
        });


        rightlegC.getUpperleg().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = rightlegC.getUpperleg().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();

                pivotX = rightlegC.getUpperleg().getBoundsInParent().getCenterX() - 20;
                pivotY = rightlegC.getUpperleg().getBoundsInParent().getCenterY() - 38;

            }
        });

        rightlegC.getUpperleg().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = rightlegC.getUpperleg().localToParent(event.getX(), event.getY());

                double newRotate = startX - pt.getX();

                if ((newRotate + rightlegC.upperLegRotate) >= -90 && (newRotate + rightlegC.upperLegRotate) <= 90){
                    rightleg.getTransforms().add(new Rotate(newRotate, pivotX, pivotY));
                    rightlegC.upperLegRotate += newRotate;
                }

                if ((pt.getY() - startY) > 5){
                    if (rightlegC.getUpperleg().getScaleX() < 6 && rightlegC.getUpperleg().getScaleY() < 6){
                        rightlegC.getUpperleg().setScaleX(rightlegC.getUpperleg().getScaleX() + 0.01);
                        rightlegC.getUpperleg().setScaleY(rightlegC.getUpperleg().getScaleY() + 0.01);
                    }

                    if (rightlegC.getLowerleg().getScaleX() < 5 && rightlegC.getLowerleg().getScaleY() < 5){
                        rightlegC.getLowerleg().setScaleX(rightlegC.getLowerleg().getScaleX() + 0.005);
                        rightlegC.getLowerleg().setScaleY(rightlegC.getLowerleg().getScaleY() + 0.005);
                    }

                }


                else if ((pt.getY() - startY) < 0){
                    if (rightlegC.getUpperleg().getScaleX() > 1 && rightlegC.getUpperleg().getScaleY() > 1){
                        rightlegC.getUpperleg().setScaleX(rightlegC.getUpperleg().getScaleX() - 0.05);
                        rightlegC.getUpperleg().setScaleY(rightlegC.getUpperleg().getScaleY() - 0.05);
                    }

                    if (rightlegC.getLowerleg().getScaleX() > 1 && rightlegC.getLowerleg().getScaleY() > 1){
                        rightlegC.getLowerleg().setScaleX(rightlegC.getLowerleg().getScaleX() - 0.025);
                        rightlegC.getLowerleg().setScaleY(rightlegC.getLowerleg().getScaleY() - 0.025);
                    }

                }

            }
        });

        rightlegC.getLowerleg().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = rightlegC.getLowerleg().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();

                pivotX = rightlegC.getLowerleg().getBoundsInParent().getCenterX() - 20;
                pivotY = rightlegC.getLowerleg().getBoundsInParent().getCenterY() - 60;
            }
        });

        rightlegC.getLowerleg().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = rightlegC.getLowerleg().localToParent(event.getX(), event.getY());
                double newRotate = startX - pt.getX();

                if ((newRotate + rightlegC.lowerLegRotate) >= -90 && (newRotate + rightlegC.lowerLegRotate) <= 90){
                    rightlegC.getLowerleg_foot().getTransforms().add(new Rotate(newRotate, pivotX, pivotY));
                    rightlegC.lowerLegRotate += newRotate;
                }

                if ((pt.getY() - startY) > 5){
                    if (rightlegC.getLowerleg().getScaleX() < 5 && rightlegC.getLowerleg().getScaleY() < 5){
                        rightlegC.getLowerleg().setScaleX(rightlegC.getLowerleg().getScaleX() + 0.005);
                        rightlegC.getLowerleg().setScaleY(rightlegC.getLowerleg().getScaleY() + 0.005);
                    }

                } else if ((pt.getY() - startY) < 0){
                    if (rightlegC.getLowerleg().getScaleX() > 1 && rightlegC.getLowerleg().getScaleY() > 1){
                        rightlegC.getLowerleg().setScaleX(rightlegC.getLowerleg().getScaleX() - 0.025);
                        rightlegC.getLowerleg().setScaleY(rightlegC.getLowerleg().getScaleY() - 0.025);
                    }
                }

            }
        });

        rightlegC.getFoot().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Point2D pt = rightlegC.getFoot().localToParent(t.getX(), t.getY());
                startX = pt.getX();
                startY = pt.getY();


            }
        });

        rightlegC.getFoot().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D pt = rightlegC.getFoot().localToParent(event.getX(), event.getY());

                double newRotate = (pt.getX() - startX);
                if (newRotate  >= -35 && newRotate <= 35){
                    rightlegC.getFoot().setRotate(newRotate);

                }

            }
        });

        stage.setScene(screen);
        stage.show();
    }
}
