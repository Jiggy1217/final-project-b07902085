/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.model;

import com.lovingshiba.audio.AudioPlayer;
import com.lovingshiba.model.proxy.ProxyImage;
import com.lovingshiba.view.Window;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class SpaceShiba extends GameObject {

    private ProxyImage proxyImage;
    private ArrayList<Bullet> bullets;
    private AudioPlayer shootBGM = new AudioPlayer("/assets/round3/audio/shoot.wav");

    public SpaceShiba(int x, int y){
        super(x, y);
        if(proxyImage == null) {
            proxyImage = new ProxyImage("/assets/round3/image/spaceDog.gif");
        }
        this.image = proxyImage.loadImage().getImage();
        this.width = image.getWidth(null) - 10;
        this.height = image.getHeight(null) - 10;
        this.x -= width;
        this.y -= height;
        this.bullets = new ArrayList<>();
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    public void removeBullets(int idx) {
        bullets.remove(idx);
    }

    @Override
    public void tick() {
        checkWindowBorder();
        for (Bullet b: bullets) {
            b.tick();
        }
    }
    public void up() {
        this.y -= 30;
    }
    public void down() {
        if (this.y < Window.HEIGHT - 2 * height) {
            this.y += 30;
        }

    }
    public void attack() {
        shootBGM.play();
        Bullet newBullet = new Bullet(this.x + this.width - 20, this.y + this.height / 2);
        this.bullets.add(newBullet);
    }

    private void checkWindowBorder() {
        if(this.x > Window.WIDTH) {
            this.x = Window.WIDTH;
        }
        if(this.x < 0) {
            this.x = 0;
        }
        if(this.y > Window.HEIGHT - 50) {
            this.y = Window.HEIGHT - 50;
        }
        if(this.y < 0) {
            this.y = 0;
        }
    }

    @Override
    public void render(Graphics2D g, ImageObserver obs) {
        g.drawImage(image, x, y, obs);
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
