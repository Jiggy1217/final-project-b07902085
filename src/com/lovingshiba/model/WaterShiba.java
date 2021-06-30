/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.model;

import com.lovingshiba.model.proxy.ProxyImage;
import com.lovingshiba.view.Window;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.image.ImageObserver;


public class WaterShiba extends GameObject {

    private ProxyImage proxyImageNormal;
    private ProxyImage proxyImageUp;
    private ProxyImage proxyImageDown;
    private Image imageUp;
    private Image imageDown;

    private int isUp;
    private int isDown;

    public WaterShiba(int x, int y){
        super(x, y);

        if(proxyImageNormal == null) {
            proxyImageNormal = new ProxyImage("/assets/round2/image/swimDog.gif");
            proxyImageUp = new ProxyImage("/assets/round2/image/swimDogUp.gif");
            proxyImageDown = new ProxyImage("/assets/round2/image/swimDogDown.gif");
        }

        this.image = proxyImageNormal.loadImage().getImage();
        this.imageUp = proxyImageUp.loadImage().getImage();
        this.imageDown = proxyImageDown.loadImage().getImage();

        this.width = image.getWidth(null) - 10;
        this.height = image.getHeight(null) - 10;
        this.x -= width;
        this.y -= height;

        this.isUp = 0;
        this.isDown = 0;
    }
    @Override
    public void tick() {
        checkWindowBorder();
    }

    public void up() {
        if (this.y - this.height >= 0) {
            this.y -= 30;
            this.isUp = 15;
            this.isDown = 0;
        }
    }

    public void down() {
        if (this.y + 2 * this.height < com.lovingshiba.view.Window.HEIGHT) {
            this.y += 30;
            this.isDown = 15;
            this.isUp = 0;
        }
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
        if (isUp > 0) {
            g.drawImage(imageUp, x, y, obs);
            isUp--;
        }

        else if (isDown > 0) {
            g.drawImage(imageDown, x, y, obs);
            isDown--;
        }

        else {
            g.drawImage(image, x, y, obs);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
