package com.example.androidpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View{

    private Sprite playerBird;
    private ArrayList<Sprite> enemyBirds;
    private Sprite apple;
    private int level = 1;
    private int countEnemyBird = 5;
    private int viewWidth;
    private int viewHeight;
    private boolean gameOver = false;   //завершение игры
    private boolean pause = false;      //постановка игры на паузу

    private int points = 0;
    private int speedPlayer = 100;
    private int speedMaxPlayer = 300;
    private final int timerInterval = 30;

    public static double getRandomInt(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }

    public GameView(Context context) {

        super(context);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.raw.player); //получение данных
        //изображения, которое используется под визуализацию игрока

        //ширина и высота фрейма
        int w = b.getWidth()/5;
        int h = b.getHeight()/3;
        Rect firstFrame = new Rect(0, 0, w, h); //коориданаты четырёхугольника, при
        //наложении которого на картинку R.raw.player будет отмечено определённое состояние игрока
        //в один из промежутков времени.

        playerBird = new Sprite(10, 0, 0, speedPlayer, firstFrame, b);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i ==0 && j == 0) {
                    continue;
                }

                if (i ==2 && j == 3) {
                    continue;
                }
                playerBird.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }


        b = BitmapFactory.decodeResource(getResources(), R.raw.enemy);
        w = b.getWidth()/5;
        h = b.getHeight()/3;
        firstFrame = new Rect(4*w, 0, 5*w, h);

        enemyBirds = new ArrayList<Sprite>();
        for(int i = 0; i < countEnemyBird; i++){
            enemyBirds.add(new Sprite(2000,
                    getRandomInt(100, viewHeight),
                    getRandomInt(-150, -50), 0, firstFrame, b));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }

                if (i ==2 && j == 0) {
                    continue;
                }

                for(int k = 0; k < enemyBirds.size(); k++){
                    enemyBirds.get(k).addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
                }
            }
        }

        b = BitmapFactory.decodeResource(getResources(), R.raw.apple);
        w = b.getWidth();
        h = b.getHeight();
        firstFrame = new Rect(0, 0, w, h);

        apple = new Sprite(1500, 400, -100, 0, firstFrame, b);

        Timer t = new Timer();
        t.start();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    protected void pause(){
        pause = !pause;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(250, 255, 255, 255);
        playerBird.draw(canvas);
        for(int i = 0; i < enemyBirds.size(); i++){
            enemyBirds.get(i).draw(canvas);
        }
        apple.draw(canvas);

        Paint p = new Paint();

        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.BLACK);
        canvas.drawText("Level: " + level + "  Score: " + points + "", viewWidth - 700, 70, p);

        if(gameOver == true){
            p.setTextSize(80.0f);
            p.setColor(Color.RED);
            canvas.drawText("GAME OVER", viewWidth /4, viewHeight / 2, p);
        }

        if(pause == true){
            p.setTextSize(80.0f);
            p.setColor(Color.RED);
            canvas.drawText("PAUSE", viewWidth /3, viewHeight / 2, p);
        }
    }

    protected void update () {
        playerBird.update(timerInterval);
        for(int i = 0; i < enemyBirds.size(); i++){
            enemyBirds.get(i).update(timerInterval);
        }
        apple.update(timerInterval);

        if (playerBird.getY() + playerBird.getFrameHeight() > viewHeight) {
            playerBird.setY(viewHeight - playerBird.getFrameHeight());
            playerBird.setVy(-playerBird.getVy());
            points--;
        }

        else if (playerBird.getY() < 0) {
            playerBird.setY(0);
            playerBird.setVy(-playerBird.getVy());
            points--;
        }

        boolean flag = false;
        if(apple.getX() < - apple.getFrameWidth()){
            teleportObject(apple);
        }

        if(apple.intersect(playerBird)){
            teleportObject(apple);
            points += 100;
            if(speedPlayer <= speedMaxPlayer) {
                speedPlayer += 20;
                flag = true;
            }
        }

        for(int i = 0; i < enemyBirds.size(); i++){
            if (enemyBirds.get(i).getX() < - enemyBirds.get(i).getFrameWidth()) {
                teleportObject(enemyBirds.get(i));
                points +=10;
            }

            if (enemyBirds.get(i).intersect(playerBird)) {
                teleportObject(enemyBirds.get(i));
                points -= 40;
                if(speedPlayer > 100) {
                    speedPlayer -= 20;
                    flag = true;
                }
            }
        }

        if((level == 1) && (points >= 500)){
            points = 0;
            level++;
            for(int i = 0; i < enemyBirds.size(); i++){
                enemyBirds.get(i).setVx(enemyBirds.get(i).getVx() - getRandomInt(100, 300));
            }
        }else if((level == 2) && (points >= 700)){
            points = 0;
            level++;
            for(int i = 0; i < enemyBirds.size(); i++){
                enemyBirds.get(i).setVx(enemyBirds.get(i).getVx() - getRandomInt(100, 300));
            }
        }else if((level == 3) && (points >= 1200)){
            points = 0;
            level++;
            for(int i = 0; i < enemyBirds.size(); i++){
                enemyBirds.get(i).setVx(enemyBirds.get(i).getVx() - getRandomInt(100, 300));
            }
        }else if((level == 4) && (points >= 1500)){
            points = 0;
            level++;
            for(int i = 0; i < enemyBirds.size(); i++){
                enemyBirds.get(i).setVx(enemyBirds.get(i).getVx() - getRandomInt(100, 300));
            }
        }else if((level == 5) && (points >= 2000)){
            points = 0;
            level++;
            for(int i = 0; i < enemyBirds.size(); i++){
                enemyBirds.get(i).setVx(enemyBirds.get(i).getVx() - getRandomInt(100, 300));
            }
        }

        if((level != 1) && (points <= -100)){
            points = 0;
            level--;
            if(speedPlayer > 150) {
                speedPlayer -= 100;
                flag = true;
            }
        }else if((level == 1) && (points <= -100)){
            points = 0;
            gameOver = true;
        }

        if((flag == true) && (playerBird.getVy() < 0) && (speedPlayer > -speedMaxPlayer))
            playerBird.setVy(-speedPlayer);
        else if((flag == true) && (playerBird.getVy() >= 0) && (speedPlayer < speedMaxPlayer))
            playerBird.setVy(speedPlayer);

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN)  {
            if (event.getY() < playerBird.getBoundingBoxRect().top) {
                if(playerBird.getVy() < 0)
                    playerBird.setVy(playerBird.getVy());
                else
                    playerBird.setVy(playerBird.getVy() * (-1));
                points--;
            }
            else if (event.getY() > (playerBird.getBoundingBoxRect().bottom)) {
                if(playerBird.getVy() < 0)
                    playerBird.setVy(playerBird.getVy() * (-1));
                else
                    playerBird.setVy(playerBird.getVy());
                points--;
            }

            for(int i = 0; i < enemyBirds.size(); i++){
                if(enemyBirds.get(i).clicked(event.getX(), event.getY(), 25)){
                    teleportObject(enemyBirds.get(i));
                    points += 50;
                }
            }
        }

        return true;
    }


    private void teleportObject (Sprite sprite) {
        sprite.setX(viewWidth + Math.random() * 500);
        sprite.setY(Math.random() * (viewHeight - sprite.getFrameHeight()));
    }

    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if((pause == true) && (gameOver == false)){
                invalidate();
                return;
            }

            if(gameOver == false){
                update ();
            }else{
                super.cancel();
            }
        }

        @Override
        public void onFinish() {
        }
    }
}