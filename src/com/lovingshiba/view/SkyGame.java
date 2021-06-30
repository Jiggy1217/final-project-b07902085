/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.view;
import com.lovingshiba.Audio.AudioPlayer;
import com.lovingshiba.controller.Controller;
import com.lovingshiba.model.SkyShiba;
import com.lovingshiba.model.Tube;
import com.lovingshiba.model.TubeColumn;
import com.lovingshiba.model.proxy.ProxyImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;


public class SkyGame extends Game {

    private boolean isRunning = false;
    private ProxyImage proxyImage;
    private Image background;
    private SkyShiba shiba;
    private TubeColumn tubeColumn;

    private int score;
    private int highScore;
    private boolean isStart = true;
    private boolean isEnd = false;
    private boolean isPass = false;
    private GameKeyAdapter keyAdapter= new com.lovingshiba.view.SkyGame.GameKeyAdapter();
    private AudioPlayer winMusic = new AudioPlayer("/assets/round1/audio/win.wav");


    private int winScore = 15;
    private boolean win = false;
    private AudioPlayer bgm = new AudioPlayer("/assets/round1/audio/bgm.wav");
    private AudioPlayer startMusic = new AudioPlayer("/assets/round1/audio/startMusic.wav");
    private AudioPlayer loseMusic = new AudioPlayer("/assets/round1/audio/dead.wav");
    private AudioPlayer readyMusic = new AudioPlayer("/assets/round1/audio/readygo.wav");

    public SkyGame(Window window) {
        super(window);
        proxyImage = new ProxyImage("/assets/round1/image/background.png");
        background = proxyImage.loadImage().getImage();
        setFocusable(true);
        setDoubleBuffered(false);
        Timer timer = new Timer(15, this);
        timer.start();
    }
    @Override
    public void startFocus(){
        startMusic.loopPlay();
        requestFocus();
        requestFocusInWindow();
        setFocusable(true);
        addKeyListener(this.keyAdapter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().sync();
        if (isRunning) {
            shiba.tick();
            tubeColumn.tick();
            checkColision();
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, null);
        if(isStart){
            readyMusic.stop();
            proxyImage = new ProxyImage("/assets/round1/image/start.gif");
            g2.drawImage(proxyImage.loadImage().getImage(),0,0,null);
        }
        else if (isRunning) {
            this.shiba.render(g2, this);
            this.tubeColumn.render(g2, this);
            g2.setColor(Color.black);
            g.setFont(new Font("04b_19", Font.BOLD, 50));
            g2.drawString("" + this.tubeColumn.getPoints(), Window.WIDTH/2, 60);
            if (this.tubeColumn.getPoints() >= winScore) {
                this.win = true;
                endGame();
            }
        }else if(isPass){

            tubeColumn.isEnd = true;
            proxyImage = new ProxyImage("/assets/round1/image/pass.png");
            g2.drawImage(proxyImage.loadImage().getImage(),-40,0,null);


        }
        else {

            proxyImage = new ProxyImage("/assets/round1/image/round1_rule.png");
            g2.drawImage(proxyImage.loadImage().getImage(),0,0,null);

        }
        g.dispose();
    }
    @Override
    public void restartGame() {
        if (!isRunning) {
            readyMusic.play();
            bgm.loopPlay();
            this.isRunning = true;
            this.shiba = new SkyShiba(Window.WIDTH / 2, Window.HEIGHT / 2);
            this.tubeColumn = new TubeColumn();
            this.win = false;
        }
    }
    public void continueGame() {
        this.window.changeFrame(new WaterGame(this.window));
        bgm.stop();
        removeKeyListener(keyAdapter);
    }
    private void endGame() {
        if(this.win) {
            this.isRunning = false;
            this.isPass = true;
            winMusic.play();
        }else{
            bgm.stop();
            loseMusic.play();
            this.isRunning = false;
            this.tubeColumn.setPoints(0);
        }

    }

    private void checkColision() {
        if(this.isRunning) {
            Rectangle rectBird = this.shiba.getBounds();
            Rectangle rectTube;
            for (int i = 0; i < this.tubeColumn.getTubes().size(); i++) {
                Tube tempTube = this.tubeColumn.getTubes().get(i);
                rectTube = tempTube.getBounds();
                if (rectBird.intersects(rectTube)) {
                    endGame();
                }
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
                if(isStart){
                    isStart = false;
                    startMusic.close();
                }
                else if(isPass){
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
