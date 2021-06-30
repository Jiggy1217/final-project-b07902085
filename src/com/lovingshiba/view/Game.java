package com.lovingshiba.view;



import com.lovingshiba.controller.Controller;
import com.lovingshiba.model.SkyShiba;
import com.lovingshiba.model.TubeColumn;
import com.lovingshiba.model.proxy.ProxyImage;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import javax.swing.JPanel;

public abstract class Game extends JPanel implements ActionListener{
    private boolean isRunning = false;
    private ProxyImage proxyImage;
    private Image background;
    private SkyShiba shiba;
    private TubeColumn tubeColumn;
    private int score;
    private int highScore;
    abstract void restartGame();
    abstract void startFocus();
    public Window window;
    Game(Window window){
        this.window = window;
    }

    private class GameKeyAdapter extends KeyAdapter {

        private final Controller controller;

        public GameKeyAdapter() {
            controller = new Controller();
        }

    }
}
