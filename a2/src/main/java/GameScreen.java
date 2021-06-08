import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.Group;
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
    private GameOver gameover;
    private AnimationTimer timer;
    private SoundEffect explosion;
    private SoundEffect shoot;
    private SoundEffect enemyKilled;

    boolean GAME_OVER;
    boolean ENEMIES_REACHED_BOTTOM;
    boolean WINNER;


    int level = 0;
    int score = 0;
    int lives = 0;
    int NUM_ALIENS;

    /* Player */
    boolean STOPPED;
    boolean MOVE_RIGHT;
    boolean MOVE_LEFT;

    /* Player shoot */
    ArrayList <PlayerBullet> playerBullets;
    long player_last_shoot_timing = System.currentTimeMillis();
    double player_shoot_frequency = 500;    //shoot at most 2x in 1s
    double player_bullet_speed = 5;

    /* Enemies */
    double ENEMY_LEVEL1_SPEED = 1.25;
    double ENEMY_LEVEL2_SPEED = 3;
    double ENEMY_LEVEL3_SPEED = 4;

    double X_MAX = 750;
    double X_MIN = 0;

    boolean ENEMIES_MOVE_RIGHT;
    boolean ENEMIES_MOVE_DOWN;
    double ENEMY_SPEED = 0;
    double ENEMY_DOWN_SPEED = 15;
    private Enemy [][] enemies;


    /* Enemy shoot */
    ArrayList <EnemyBullet> enemyBullets;
    long enemies_last_shoot_timing = System.currentTimeMillis();
    long enemy_shoot_frequency = 1000;
    double enemy_bullet_speed = 5;

    /* GAME SCREEN */

    GameScreen(){
        root = new Group();
        gameScreen = new Scene(root, Color.BLACK);
    };

    public void setLevel(int lev){
        level = lev;
    }

    public void populateGameScreen(){
        lives = 3;
        NUM_ALIENS = 50;
        STOPPED = true;
        MOVE_RIGHT = false;
        MOVE_LEFT = false;
        ENEMIES_MOVE_RIGHT = true;
        ENEMIES_MOVE_DOWN = true;
        GAME_OVER = false;
        WINNER = false;
        ENEMIES_REACHED_BOTTOM = false;
        player_bullet_speed = 5;

        /* Set up sounds */
        explosion = new SoundEffect("sounds/explosion.wav");
        shoot = new SoundEffect("sounds/shoot.wav");
        enemyKilled = new SoundEffect("sounds/invaderkilled.wav");


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


        gameScreen.setOnKeyPressed(keyEvent -> {
            //GO LEFT
            if (STOPPED && (keyEvent.getCode() == KeyCode.A ||  keyEvent.getCode() == KeyCode.LEFT)) {
                STOPPED = false;
                MOVE_LEFT = true;
            }
            //GO RIGHT
            if (STOPPED && (keyEvent.getCode() == KeyCode.D ||  keyEvent.getCode() == KeyCode.RIGHT)) {
                STOPPED = false;
                MOVE_RIGHT = true;
            }
            //SHOOT
            if ((keyEvent.getCode() == KeyCode.SPACE) && !GAME_OVER){
                playerShoot();
            }
            //Quit Game
            if (keyEvent.getCode() == KeyCode.Q){
                System.exit(0);
            }

            //Advance to Next Level
            if (keyEvent.getCode() == KeyCode.ENTER && GAME_OVER && WINNER && (level < 3)){
                setLevel(level + 1);
                root.getChildren().remove(gameover);
                root.getChildren().clear();
                populateGameScreen();
            }
            //Completed the Game so Restart Game at Level 1
            if (keyEvent.getCode() == KeyCode.ENTER && GAME_OVER && WINNER){
                setLevel(0);
                score = 0;
                root.getChildren().remove(gameover);
                root.getChildren().clear();
                populateGameScreen();
            }
            if (keyEvent.getCode() == KeyCode.ENTER && GAME_OVER && !WINNER){
                setLevel(level);
                score = 0;
                root.getChildren().remove(gameover);
                root.getChildren().clear();
                populateGameScreen();
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

        timer = new AnimationTimer(){

            @Override
            public void handle(long now) {
                movePlayer();
                moveEnemies();
                movePlayerShots();

                checkIfPlayerHitEnemy();

                enemiesShoot();
                moveEnemiesShots();
                checkIfEnemyHitPlayer();

                if (GAME_OVER){
                    timer.stop();
                    gameOverDisplay();
                }
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
            shoot.playSound();
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
        for (int i = 0; i < 5; i++) {
            for (int j=0; j < 10; j++) {
                for (PlayerBullet bullet: playerBullets) {
                    Node enemy = enemies[i][j].getEnemy();
                    if (enemies[i][j].alive) {
                        if (objectsCollide(bullet.getPlayerBullet(), enemy)){
                            enemyKilled.playSound();
                            deleteBullet(bullet);
                            playerBullets.remove(bullet);
                            deleteEnemy(enemy);
                            enemies[i][j].alive = false;
                            NUM_ALIENS--;
                            score += 10;
                            header.updateScore(score);
                            ENEMY_SPEED += 0.10;
                            player_bullet_speed += 0.05;
                            checkGameOver();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void respawnPlayer(){
        root.getChildren().remove(player.getPlayer());
        player = new Player();
        root.getChildren().add(player.getPlayer());
    }

    boolean objectsCollide(Node a, Node b){
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    /* ENEMIES */
    private void createEnemiesGroup(){
        enemies = new Enemy[5][10];

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 10; j++){
                Enemy enemy;
                if (i == 0){
                    enemy = new Enemy(3);
                }else if (i == 1 || i == 2){
                    enemy = new Enemy(2);
                }else{
                    enemy = new Enemy(1);
                }
                enemies[i][j] = enemy;
                enemy.positionEnemy((j * 40) + 190, (i * 30 )+ 90);
                enemiesGroup.getChildren().add(enemy.getEnemy());

            }
        }

    }

    private void moveEnemies(){
        //Check Enemies Boundaries
        double enemies_left_boundary = 0;
        double enemies_right_boundary = 0;
        double enemies_down_boundary = 0;
        outerloop:
        for (int j = 0; j < 10; j ++){
            for (int i=0; i < 5; i++){
                if (enemies[i][j].alive){
                    enemies_left_boundary= enemies[i][j].getX();
                    break outerloop;
                }
            }
        }

        outerloop2:
        for (int j = 9; j >= 0; j --){
            for (int i= 0 ;i < 5; i++){
                if (enemies[i][j].alive){
                    enemies_right_boundary= enemies[i][j].getX();
                    break outerloop2;
                }
            }
        }

        outerloop3:
        for (int i = 4; i >= 0; i --){
            for (int j = 0; j < 10; j++){
                if (enemies[i][j].alive){
                    enemies_down_boundary = enemies[i][j].getY();
                    break outerloop3;
                }
            }
        }

        if (ENEMIES_MOVE_RIGHT && ((enemies_right_boundary + ENEMY_SPEED) >= X_MAX)){  //CHECK RIGHT BOUNDARY
            ENEMIES_MOVE_RIGHT = false;
            ENEMIES_MOVE_DOWN = true;
        }else if (!ENEMIES_MOVE_RIGHT && (enemies_left_boundary - ENEMY_SPEED <= X_MIN)) {      //CHECK LEFT BOUNDARY
            ENEMIES_MOVE_RIGHT = true;
            ENEMIES_MOVE_DOWN = true;
        }

        //Move Enemies RIGHT , LEFT , or DOWN

        //Move Enemies down 1 Row
        if (ENEMIES_MOVE_DOWN && (enemies_down_boundary + ENEMY_DOWN_SPEED <= 500)){
        for (int i=0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                enemies[i][j].incrementEnemyPosBy(0, ENEMY_DOWN_SPEED);
            }
        }
        ENEMIES_MOVE_DOWN = false;

        } else if (ENEMIES_MOVE_DOWN){
            ENEMIES_REACHED_BOTTOM = true;
            checkGameOver();
        }
        else if (ENEMIES_MOVE_RIGHT){        //MOVE RIGHT
            //Update enemy positions
            for (int i=0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    enemies[i][j].incrementEnemyPosBy(ENEMY_SPEED, 0);
                }
            }
        }else {
            //Update enemy positions
            for (int i=0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    enemies[i][j].incrementEnemyPosBy(-ENEMY_SPEED, 0);
                }
            }
        }
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
                    enemyBullet.setPosition(posX + 17 , posY + 25);
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
        root.getChildren().remove(bullet.getEnemyBulletView());
    }

    private void deleteEnemy(Node enemy){

        enemiesGroup.getChildren().remove(enemy);
    }

    private void checkIfEnemyHitPlayer(){

        for (EnemyBullet bullet: enemyBullets){
            if (objectsCollide(bullet.getEnemyBulletView(), player.getPlayer())){
                explosion.playSound();
                deleteEnemyBullet(bullet);
                enemyBullets.remove(bullet);
                respawnPlayer();
                header.updateLives(this.lives - 1);
                lives --;
                if (score - 25 > 0){
                    score -= 25;
                    header.updateScore(score);
                }
                checkGameOver();
                break;
            }
        }
    }


    /* GAME OVER */
    private void checkGameOver(){
        if ((lives == 0) || ENEMIES_REACHED_BOTTOM || (NUM_ALIENS == 0)){
            GAME_OVER = true;
        }
        if (NUM_ALIENS == 0){
            WINNER = true;
        }

    }

    private void gameOverDisplay(){
        gameover = new GameOver(score, level, WINNER);
        root.getChildren().add(gameover.getGameOver());
    }
}
