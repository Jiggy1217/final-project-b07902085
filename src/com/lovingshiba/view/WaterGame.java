package com.lovingshiba.view;

import com.lovingshiba.Audio.AudioPlayer;
import com.lovingshiba.controller.Controller;
import com.lovingshiba.model.WaterShiba;
import com.lovingshiba.model.Fish;
import com.lovingshiba.model.Heart;
import com.lovingshiba.model.proxy.ProxyImage;
import com.lovingshiba.view.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class WaterGame extends Game {

    private boolean isRunning = false;
    private ProxyImage proxyImage;
    private Image background;
    private WaterShiba shiba;

    private List<Fish> fishes;
    private Heart heart;

    private WaterGame.GameKeyAdapter keyAdapter= new com.lovingshiba.view.WaterGame.GameKeyAdapter();
    private boolean isPass = false;
    private int score;
    private int winScore = 3;
    private boolean win = false;
    private boolean restart = false;

    private int fishCounter;
    private boolean addHeart = false;
    private Random rand;

    private AudioPlayer bgm;
    private AudioPlayer obtainedHeartMusic;
    private AudioPlayer collisionMusic;
    private AudioPlayer readyMusic = new AudioPlayer("/assets/round2/audio/readygo.wav");
    private AudioPlayer winMusic = new AudioPlayer("/assets/round2/audio/win.wav");

    public WaterGame(Window window) {
        super(window);
        proxyImage = new ProxyImage("/assets/round2/image/background.png");
        background = proxyImage.loadImage().getImage();

        Timer timer = new Timer(15, this);
        timer.start();
        rand = new Random();
        this.bgm = new AudioPlayer("/assets/round2/audio/bgm.wav");
        this.obtainedHeartMusic = new AudioPlayer("/assets/round2/audio/getpoint.wav");
        this.collisionMusic = new AudioPlayer("/assets/round2/audio/collision.wav");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().sync();
        if (isRunning) {
            shiba.tick();

            int total = fishes.size();
            List<Fish> fishToBeRemoved = new ArrayList<>();
            List<Fish> fishCenter = new ArrayList<>();

            for (Fish fish : fishes) {
                fish.tick();

                if (fish.getX() < (0 - fish.getWidth())) {
                    fishToBeRemoved.add(fish);
                }

                if (fish.getX() <= Window.WIDTH / 2) {
                    fishCenter.add(fish);
                }
            }

            for (Fish fish : fishToBeRemoved) {
                fishes.remove(fish);

                fishCounter += 1;
    
                if (fishCounter % 3 == 0 && heart == null) {
                    addHeart = true;
                }
            }

            if (fishCenter.size() == total) {
                addFish();
            }

            if (heart != null) {
                heart.tick();

                if (heart.getX() < -5) {
                    heart = null;
                }
            }

            checkColision();

        }

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, null);

        if (isRunning) {
            this.shiba.render(g2, this);
            for (Fish fish : this.fishes) {
                fish.render(g2, this);
            }
            if (this.heart != null) {
                this.heart.render(g2, this);
            }
            // heart score

            int dist = -30;

            g2.setColor(Color.white);
            g.setFont(new Font("Arial", 1, 20));
            g2.drawString("Score:", 13, 25);

            for (int i = 0; i < score; i++) {
                Heart heartScore = new Heart(130 + dist, 6);
                heartScore.render(g2, this);

                dist += 30;
            }

        }
        else if (this.restart) {
            proxyImage = new ProxyImage("/assets/round2/image/round2_rule.png");
            g2.drawImage(proxyImage.loadImage().getImage(),0,0,null);
        } else if(isPass){
            proxyImage = new ProxyImage("/assets/round2/image/pass.png");
            g2.drawImage(proxyImage.loadImage().getImage(),-40,0,null);

        }else {
            proxyImage = new ProxyImage("/assets/round2/image/round2_rule.png");
            g2.drawImage(proxyImage.loadImage().getImage(),0,0,null);
        }

        if (this.win) {
            endGame();
        }

        g.dispose();
    }
    public void continueGame() {
        this.window.changeFrame(new SpaceGame(this.window));
        bgm.close();
        removeKeyListener(keyAdapter);
    }
    @Override
    public void startFocus(){
        requestFocus();
        requestFocusInWindow();
        setFocusable(true);
        addKeyListener(this.keyAdapter);
    }

    @Override
    public void restartGame() {
        if (!isRunning) {
            readyMusic.play();
            this.bgm.play();
            readyMusic.play();
            this.score = 0;
            this.fishCounter = 0;
            this.isRunning = true;
            this.win = false;
            this.addHeart = false;
            this.restart = false;

            this.shiba = new WaterShiba(Window.WIDTH / 2, Window.HEIGHT / 2);
            this.heart = null;
            this.fishes = new ArrayList<Fish>();
            addFish();
        }
    }

    private void addFish() {
        int totalFish = 4;
        int height = com.lovingshiba.view.Window.HEIGHT / 5;
        int heartRandom = -1;

        Fish tempFish;

        int random = (rand.nextInt(3) + 1) * 50;

        if (addHeart == true) {
            heartRandom = rand.nextInt(totalFish);
            addHeart = false;
        }

        for (int i = 0; i < totalFish; i++) {
            if (heartRandom == i) {
                heart = new Heart(Window.WIDTH, height * i + random);
            }

            else {
                tempFish = new Fish(Window.WIDTH, height * i + random);
                fishes.add(tempFish);
            }
        }
    }

    private void endGame() {
        if (isRunning) {
            if (this.win) {
                this.isRunning = false;
                this.isPass = true;
                winMusic.play();
            } else {
                this.restart = true;
                this.isRunning = false;
                this.collisionMusic.play();
            }
        }
    }

    private void checkColision() {

        if(this.isRunning) {
            Rectangle rectShiba = this.shiba.getBounds();
            Rectangle rectFish;

            // check Shiba and Fish collision

            for (int i = 0; i < this.fishes.size(); i++) {
                Fish tempFish = this.fishes.get(i);
                rectFish = tempFish.getBounds();
                if (rectShiba.intersects(rectFish)) {
                    endGame();
                }
            }

            // check Shiba and Heart collision

            if (this.heart != null) {
                Rectangle rectHeart = this.heart.getBounds();
                if (rectShiba.intersects(rectHeart)) {
                    score += 1;
                    this.heart = null;

                    this.obtainedHeartMusic.play();
                }
            }

            // check if win

            if (score == winScore) {
                this.win = true;
            }
        }
    }


    // Key
    private class GameKeyAdapter extends KeyAdapter {

        private final Controller controller;

        public GameKeyAdapter() {
            controller = new Controller();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               if(isPass){
                    continueGame();
                }
                else{
                    restartGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (isRunning) {
                controller.controllerReleased(shiba, e);
            }
        }
    }
}
