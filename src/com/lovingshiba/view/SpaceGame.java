package com.lovingshiba.view;

import com.lovingshiba.audio.AudioPlayer;
import com.lovingshiba.controller.Controller;
import com.lovingshiba.model.*;

import com.lovingshiba.model.proxy.ProxyImage;
import com.lovingshiba.view.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.channels.Pipe;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class SpaceGame extends Game{
    private boolean isRunning = false;
    private ProxyImage proxyImage;
    private Image background;
    private SpaceShiba shiba;
    private List<Monster> monsters;
    private List<Heart> hearts;
    private List<Poison> poisons;
    private int score;
    private int winScore = 3;
    private boolean win = false;
    private boolean isPass = false;
    private SpaceGame.GameKeyAdapter keyAdapter= new com.lovingshiba.view.SpaceGame.GameKeyAdapter();

    private int monsterCounter;
    private Random rand;

    private AudioPlayer bgm;
    private AudioPlayer getPointBGM;
    private AudioPlayer poisonBGM;
    private AudioPlayer bombBGM;
    private AudioPlayer gameoverBGM;
    private AudioPlayer readyMusic = new AudioPlayer("/assets/round3/audio/readygo.wav");

    private AudioPlayer winfinalMusic = new AudioPlayer("/assets/round3/audio/winfinal.wav");



    public SpaceGame(Window window) {
        super(window);
        proxyImage = new ProxyImage("/assets/round3/image/spaceBackground.jpeg");
        background = proxyImage.loadImage().getImage();
        Timer timer = new Timer(15, this);
        timer.start();
        monsterCounter = 0;
        rand = new Random();
        bgm = new AudioPlayer("/assets/round3/audio/bgm.wav");
        getPointBGM = new AudioPlayer("/assets/round3/audio/getpoint.wav");
        poisonBGM = new AudioPlayer("/assets/round3/audio/poisoned.wav");
        bombBGM = new AudioPlayer("/assets/round3/audio/bomb.wav");
        gameoverBGM = new AudioPlayer("/assets/round3/audio/gameover.wav");
    }
    public void continueGame() {
        this.window.changeFrame(new SkyGame(this.window));
        winfinalMusic.close();
        removeKeyListener(keyAdapter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().sync();
        if (isRunning) {
            // shiba tick
            shiba.tick();

            // monster tick
            List<Monster> monsterCenter = new ArrayList<>();
            for(int i = 0; i < monsters.size(); i++) {
                monsters.get(i).tick();
                if (monsters.get(i).getX() < -5) {
                    monsters.remove(i);
                }
                else if (monsters.get(i).getX() <= Window.WIDTH / 2) {
                    monsterCenter.add(monsters.get(i));
                }
            }
            if (monsterCenter.size() == monsters.size())
                addMonster();

            // heart tick
            for (int i = 0; i < hearts.size(); i++) {
                Heart h = hearts.get(i);
                h.tick();
                if (h.getX() < -5) {
                    hearts.remove(i);
                }
            }

            // poison tick
            for (int i = 0; i < poisons.size(); i++) {
                Poison p = poisons.get(i);
                p.tick();
                if (p.getX() < -5) {
                    poisons.remove(i);
                    addPoison();
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

            for (Monster monster : this.monsters) {
                monster.render(g2, this);
            }
            for (Heart heart: this.hearts) {
                heart.render(g2, this);
            }
            for (Poison poison: this.poisons) {
                poison.render(g2, this);
            }
            for (Bullet b: shiba.getBullets()) {
                b.render(g2, this);
            }

            int dist = -30;
            g2.setColor(Color.white);
            g.setFont(new Font("04b_19", Font.BOLD, 20));
            g2.drawString("Score:", 13, 25);
            for (int i = 0; i < score; i++) {
                Heart heartScore = new Heart(115 + dist, 6);
                heartScore.render(g2, this);

                dist += 30;
            }
        } else if (!win) {
            proxyImage = new ProxyImage("/assets/round3/image/round3_rule.png");
            g2.drawImage(proxyImage.loadImage().getImage(),0,0,null);
        } else {
            proxyImage = new ProxyImage("/assets/round3/image/finalpass.png");
            g2.drawImage(proxyImage.loadImage().getImage(),0,0,null);
            proxyImage = new ProxyImage("/assets/round3/image/shiba_love.gif");
            g2.drawImage(proxyImage.loadImage().getImage(),600,0,null);

        }

        g.dispose();
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
            this.score = 0;
            this.isRunning = true;
            this.shiba = new SpaceShiba(com.lovingshiba.view.Window.WIDTH / 2 - 100, Window.HEIGHT / 2);
            this.monsters = new ArrayList<>();
            this.hearts = new ArrayList<>();
            this.poisons = new ArrayList<>();
            this.win = false;
            bgm.play();
            addMonster();
            addPoison();
        }
    }
    private void endGame() {
        if(isRunning) {
            if(this.win){
                this.isPass = true;
                this.isRunning = false;

                winfinalMusic.loopPlay();
            }else{
                this.isRunning = false;
                gameoverBGM.play();
            }

            bgm.stop();
        }
    }


    private void addMonster() {
        int totalMonster = 4;
        int height = com.lovingshiba.view.Window.HEIGHT / 5;

        Monster tempMonster;

        int random = (rand.nextInt(3) + 1) * 50;

        for (int i = 0; i < totalMonster; i++) {
            tempMonster = new Monster(Window.WIDTH, height * i + random);
            monsters.add(tempMonster);
        }
    }

    private void addPoison() {
        int height = com.lovingshiba.view.Window.HEIGHT / 5;
        int random = (rand.nextInt(3) + 1) * 50;
        Poison tempPoison = new Poison(Window.WIDTH, height + random);
        poisons.add(tempPoison);
    }


    private void checkColision() {

        if(this.isRunning) {
            Rectangle rectShiba = this.shiba.getBounds();
            Rectangle rectMonster;
            Rectangle rectBullet;

            // check Shiba and Monster collision
            for (Monster m: this.monsters) {
                rectMonster = m.getBounds();
                if (rectShiba.intersects(rectMonster)) {
                    endGame();
                }
            }

            // check Shiba and Heart collision
            for (int i = 0; i < this.hearts.size(); i++) {
                Heart h = this.hearts.get(i);
                if (rectShiba.intersects(h.getBounds())) {
                    score += 1;
                    getPointBGM.play();
                    hearts.remove(i);

                }
            }

            // check Shiba and Poison collision
            for (int i = 0; i < this.poisons.size(); i++) {
                Poison p = this.poisons.get(i);
                if (rectShiba.intersects(p.getBounds())) {
                    score -= 1;
                    poisonBGM.play();
                    poisons.remove(i);
                    if (score < 0)
                        endGame();
                }
            }

            //check Bullet and Monster collision
            for (int i = 0; i < this.shiba.getBullets().size(); i++) {
                Bullet b = this.shiba.getBullets().get(i);
                for (int j = 0; j < this.monsters.size(); j++) {
                    Monster m = this.monsters.get(j);
                    if (b.getBounds().intersects(m.getBounds())) {
                        bombBGM.play();
                        Random rand = new Random();
                        int random = rand.nextInt(2);
                        if (random == 0) {
                            Heart h = new Heart(m.getX(), m.getY());
                            hearts.add(h);
                        }
                        else {
                            Poison p = new Poison(m.getX(), m.getY());
                            poisons.add(p);
                        }

                        this.monsters.remove(j);
                        this.shiba.removeBullets(i);

                    }
                }
            }


            // check if win
            if (score >= winScore) {
                win = true;
                endGame(); // win
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
                if(win){
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
