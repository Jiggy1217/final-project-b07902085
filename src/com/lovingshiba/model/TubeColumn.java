
package com.lovingshiba.model;

import com.lovingshiba.audio.AudioPlayer;
import com.lovingshiba.view.Window;
import java.awt.Graphics2D;

import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;


public class TubeColumn {

    private int base = Window.HEIGHT - 80;
    private ArrayList<Tube> tubes;
    private ArrayList<Tube> tubes2;

    private Random random;
    private int points = 0;
    private int speed = 6;
    private int speed2 = speed;
    private int changeSpeed = 2;
    private Boolean success1 = false;
    private Boolean success2 = false;
    public boolean isEnd = false;

    private AudioPlayer getPointMusic = new AudioPlayer("/assets/round1/audio/getpoint.wav");
    public TubeColumn() {
        tubes = new ArrayList<>();
        tubes2 = new ArrayList<>();
        random = new Random();
        initTubes(tubes);
    }

    private void initTubes(ArrayList<Tube> tubes) {

        int last = base;
        int randWay = random.nextInt(10);
        int tempspeed = speed;
        if(tubes == this.tubes){
            this.success1 = false;
        }else{
            this.success2 = false;
            tempspeed = speed2;
        }
        for (int i = 0; i < 20; i++) {

            Tube tempTube = new Tube(900, last,"body");
            tempTube.setDx(tempspeed);
            last = tempTube.getY() - tempTube.getHeight();
            if (i < randWay || i > randWay + 4) {
                if(i == randWay-1){
                    tempTube.type = "up";
                }else if (i == randWay+5){
                    tempTube.type = "down";
                }
                tubes.add(tempTube);
            }

        }

    }

    public void tick() {

        for (int i = 0; i < this.tubes.size(); i++) {
            this.tubes.get(i).tick();
            if (this.tubes.get(i).getX() < -60) {
                this.tubes.remove(this.tubes.get(i));
            }
        }
        for (int i = 0; i < this.tubes2.size(); i++) {
            this.tubes2.get(i).tick();
            if (this.tubes2.get(i).getX() < -60) {
                this.tubes2.remove(this.tubes2.get(i));
            }
        }

        if (this.tubes.size() != 0 && this.tubes.get(0).getX() < Window.WIDTH / 2 - 50 && this.success1 == false) {
            this.points += 1;
            getPointMusic.play();
            this.success1 = true;
        }
        else if (this.tubes2.size() != 0 && this.tubes2.get(0).getX() < Window.WIDTH / 2 - 50 && this.success2 == false) {
            this.points += 1;
            getPointMusic.play();
            this.success2 = true;
        }

        if (this.tubes.isEmpty()) {

            initTubes( this.tubes);
            this.speed2 = speed;
        }
        int randDistance = random.nextInt(100);
        if(this.tubes.get(0).x < 350+randDistance && this.tubes2.isEmpty()){
            initTubes(this.tubes2);
        }

    }

    public void render(Graphics2D g, ImageObserver obs) {
        for (int i = 0; i < this.tubes.size(); i++) {
            this.tubes.get(i).render(g, obs);
        }
        for (int i = 0; i < tubes2.size(); i++) {
            this.tubes2.get(i).render(g, obs);
        }

    }

    public ArrayList<Tube> getTubes() {
        ArrayList<Tube> tempTubes = new ArrayList<>();
        for(Tube tube : this.tubes){
            tempTubes.add(tube);
        }
        for(Tube tube : this.tubes2){
            tempTubes.add(tube);
        }
        return tempTubes;
    }

    public void setTubes(ArrayList<Tube> tubes) {
        this.tubes = tubes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
