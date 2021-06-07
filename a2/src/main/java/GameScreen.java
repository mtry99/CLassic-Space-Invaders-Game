import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;


public class GameScreen {
    private Scene gameScreen;
    private Group root;
    private Header header;
    private Player player;
    private Group enemiesGroup;
    private Enemy [][] enemies;

    int level = 0;
    int score = 0;
    int lives = 3;

    /* Player */
    boolean STOPPED = true;
    boolean MOVE_RIGHT = false;
    boolean MOVE_LEFT = false;

    /* Player shoot */
    ArrayList <PlayerBullet> playerBullets;
    long player_last_shoot_timing = System.currentTimeMillis();
    double player_shoot_frequency = 500;    //shoot at most 2x in 1s
    double player_bullet_speed = 5;

    /* Enemies */
    double ENEMY_LEVEL1_SPEED = 1;
    double ENEMY_LEVEL2_SPEED = 2;
    double ENEMY_LEVEL3_SPEED = 3;

    double X_MAX = 850;
    double X_MIN = -50;

    double ENEMIES_X_LEFT = 130;
    double ENEMIES_X_RIGHT = 680;
    double ENEMIES_Y = 80;
    boolean ENEMIES_MOVE_RIGHT = true;
    boolean ENEMIES_MOVE_DOWN = false;
    double ENEMY_SPEED = 0;
    double ENEMY_DOWN_SPEED = 10;

    /* Enemy shoot */
    ArrayList <EnemyBullet> enemyBullets;
    long enemies_last_shoot_timing = System.currentTimeMillis();
    long enemy_shoot_frequency = 1000;
    double enemy_bullet_speed = 5;

    /* GAME SCREEN */

    GameScreen(){
        root = new Group();
        header = new Header();
        player = new Player();
        enemiesGroup = new Group();
        playerBullets = new ArrayList<PlayerBullet>();
        enemyBullets = new ArrayList<EnemyBullet>();
        createEnemiesGroup();
        root.getChildren().add(header.getDisplayScore());
        root.getChildren().add(header.getDisplayLevel());
        root.getChildren().add(header.getDisplayLives());
        root.getChildren().add(player.getPlayer());
        root.getChildren().add(enemiesGroup);

        gameScreen = new Scene(root, Color.BLACK);
    };

    public void setLevel(int lev){
        level = lev;
    }

    public void populateGameScreen(){

        /* Populate Header values */
        header.updateLevel(level);
        header.updateScore(score);
        header.updateLives(lives);

        /* Set Enemies Speed */
        if (level == 1){
            setEnemiesSpeed(ENEMY_LEVEL1_SPEED);
        }else if (level == 2){
            setEnemiesSpeed(ENEMY_LEVEL2_SPEED);
        }else if (level == 3){
            setEnemiesSpeed(ENEMY_LEVEL3_SPEED);
        }


        /* startGameTimer */
        startGameTimer();

        /* Key Handling events to move Player */

        //Move Player LEFT or RIGHT
        gameScreen.setOnKeyPressed(keyEvent -> {
            if (STOPPED && (keyEvent.getCode() == KeyCode.A ||  keyEvent.getCode() == KeyCode.LEFT)) {   // GO LEFT
                STOPPED = false;
                MOVE_LEFT = true;
            }
            if (STOPPED && (keyEvent.getCode() == KeyCode.D ||  keyEvent.getCode() == KeyCode.RIGHT)) {  // GO RIGHT
                STOPPED = false;
                MOVE_RIGHT = true;
            }
            if (keyEvent.getCode() == KeyCode.SPACE){
                playerShoot();

            }
        });

        //Stop Moving Player
        gameScreen.setOnKeyReleased(keyEvent -> {
           if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT){
               STOPPED = true;
               MOVE_LEFT = false;
               MOVE_RIGHT = false;
           }
        });

    }

    public void startGameTimer(){
        AnimationTimer timer = new AnimationTimer(){

            @Override
            public void handle(long now) {
                movePlayer();
               // moveEnemies();
                movePlayerShots();

                checkIfPlayerHitEnemy();


                enemiesShoot();
                moveEnemiesShots();
            }
        };
        timer.start();
    }

    public Scene getScene(){
        return this.gameScreen;
    }


    /* PLAYERS */
    private void movePlayer(){
        if (!STOPPED && MOVE_LEFT){
            player.moveLeft();
        }else if (!STOPPED && MOVE_RIGHT){
            player.moveRight();
        }
    }

    private void playerShoot(){
        long current_time = System.currentTimeMillis();
        //Can only shoot once every 500 ms
        if (current_time - player_shoot_frequency > player_last_shoot_timing){
            player_last_shoot_timing = current_time;
            PlayerBullet playerBullet = new PlayerBullet();
            playerBullet.setPosition(player.getPosition());
            playerBullets.add(playerBullet);
            root.getChildren().add(playerBullet.getPlayerBullet());
        }

    }

    private void movePlayerShots(){
        for (int i=0; i < playerBullets.size(); i++) {
            PlayerBullet bullet = playerBullets.get(i);
            if (bullet.Y == 0){
                deleteBullet(bullet);
                playerBullets.remove(i);
            }else{
                bullet.moveUp(player_bullet_speed);
            }
        }
    }

    private void deleteBullet(PlayerBullet bullet){
        root.getChildren().remove(bullet.getPlayerBullet());
    }

    private void checkIfPlayerHitEnemy(){
        for (int i=4; i >= 0; i--){
            for (int j=0; j < 10; j++){
                if (enemies[i][j].alive){
                    for (PlayerBullet bullet : playerBullets){

                       if (objectsCollide(bullet, enemies[i][j])){
                           System.out.println("i: " +  i + " j: " + j);
                           deleteEnemy(enemies[i][j].getEnemy());
                            //deleteBullet(bullet);
                            playerBullets.remove(bullet);
                            enemies[i][j].alive = false;

                            break;
                        }
                    }
                }
            }
        }
    }

    boolean objectsCollide(PlayerBullet bullet, Enemy e){
        Node a = bullet.getPlayerBullet();
        Node b = enemiesGroup.getChildren().get(e.groupID);
        if (a.getBoundsInParent().intersects(b.getBoundsInParent())){
            
            System.out.println("Bullet X: " +  bullet.X + " Bullet Y: " +  bullet.Y);
            System.out.println("Alien X: " + e.X_relative + "Alien Y: " + e.Y_relative);
            return true;
        }
        return false;
    }

    /* ENEMIES */
    private void createEnemiesGroup(){

        int kind = 0;
        double x = 0;
        double y = 0;
        int ID = 0;

        enemies = new Enemy[5][10];
        for (int i=0; i < 5; i++){
            y += 35;
            kind ++;
            for (int j=0; j < 10; j++){
                x += 45;
                enemies[i][j] = new Enemy(kind);
                enemies[i][j].positionEnemy(x,y);
                enemies[i][j].X_relative =x;
                enemies[i][j].Y_relative = y;
                enemies[i][j].setEnemy(x + 142,y + 110);
                enemiesGroup.getChildren().add((enemies[i][j]).getEnemy());
                enemies[i][j].groupID = ID;
                ID++;
            }
            x = 0;
            if (kind >= 3){
                kind = 0;
            }
        }

        //Position enemies
        enemiesGroup.setLayoutX(130);
        enemiesGroup.setLayoutY(80);

    }

    private void moveEnemies(){
        //Check Enemies Boundaries
        if (ENEMIES_MOVE_RIGHT && ((ENEMIES_X_RIGHT + ENEMY_SPEED) >= X_MAX)){  //CHECK RIGHT BOUNDARY
            ENEMIES_MOVE_RIGHT = false;
            ENEMIES_MOVE_DOWN = true;
        }else if (!ENEMIES_MOVE_RIGHT && (ENEMIES_X_LEFT - ENEMY_SPEED <= X_MIN)) {      //CHECK LEFT BOUNDARY
            ENEMIES_MOVE_RIGHT = true;
            ENEMIES_MOVE_DOWN = true;
        }

        //Move Enemies RIGHT , LEFT , or DOWN

        //Move Enemies down 1 Row
        if (ENEMIES_MOVE_DOWN){
                ENEMIES_Y += ENEMY_DOWN_SPEED;
        for (int i=0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                enemies[i][j].incrementEnemyPosBy(0, ENEMY_DOWN_SPEED);
            }
        }
        ENEMIES_MOVE_DOWN = false;
        } else if (ENEMIES_MOVE_RIGHT){        //MOVE RIGHT
            ENEMIES_X_LEFT += ENEMY_SPEED;
            ENEMIES_X_RIGHT += ENEMY_SPEED;

            //Update enemy positions
            for (int i=0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    enemies[i][j].incrementEnemyPosBy(ENEMY_SPEED, 0);
                }
            }

        }else {
            ENEMIES_X_LEFT -= ENEMY_SPEED;       //MOVE LEFT
            ENEMIES_X_RIGHT -= ENEMY_SPEED;
            //Update enemy positions
            for (int i=0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    enemies[i][j].incrementEnemyPosBy(-ENEMY_SPEED, 0);
                }
            }
        }




        enemiesGroup.setLayoutX(ENEMIES_X_LEFT);
        enemiesGroup.setLayoutY(ENEMIES_Y);
    }

    private void setEnemiesSpeed(double speed){
        ENEMY_SPEED = speed;
    }

    private void enemiesShoot(){
        long current_time = System.currentTimeMillis();

        if (current_time - enemy_shoot_frequency > enemies_last_shoot_timing){
            enemies_last_shoot_timing = current_time;

            //choose random ALIVE enemy to shoot
            boolean found_alive_enemy = false;
            while (!found_alive_enemy){
                Random rand = new Random();
                int enemy_at_x = rand.nextInt(5);
                int enemy_at_y = rand.nextInt(10);

                if (enemies[enemy_at_x][enemy_at_y].alive){
                    //found alive Enemy to shoot
                    EnemyBullet enemyBullet = new EnemyBullet((enemies[enemy_at_x][enemy_at_y]).getEnemyID());
                    enemyBullets.add(enemyBullet);

                    double posX = (enemies[enemy_at_x][enemy_at_y]).getX();
                    double posY = (enemies[enemy_at_x][enemy_at_y]).getY();
                    enemyBullet.setPosition(posX, posY);
                    root.getChildren().add(enemyBullet.getEnemyBulletView());
                    found_alive_enemy = true;
                }
            }
        }
    }

    private void moveEnemiesShots(){
        for (int i=0; i < enemyBullets.size(); i++){
            EnemyBullet bullet = enemyBullets.get(i);
            if (bullet.position_Y == 600){
                deleteEnemyBullet(bullet);
                enemyBullets.remove(i);
            }else{
                bullet.moveDown(enemy_bullet_speed);
            }

        }
    }

    private void deleteEnemyBullet(EnemyBullet bullet){
        root.getChildren().remove(bullet);
    }

    private void deleteEnemy(Node enemy){
        enemy.setVisible(false);
    }
}
