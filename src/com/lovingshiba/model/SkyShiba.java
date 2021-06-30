/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.model;

import com.lovingshiba.model.proxy.ProxyImage;
import com.lovingshiba.view.Window;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;


public class SkyShiba extends GameObject {
    private  ProxyImage flyProxyImage = new ProxyImage("/assets/round1/image/skyShiba_fly.png");
    private Image flyImage = flyProxyImage.loadImage().getImage();
    private  ProxyImage secondProxyImage = new ProxyImage("/assets/round1/image/skyShiba2.png");
    private Image secondImage = secondProxyImage.loadImage().getImage();;
    private int animation = 0;
    private ProxyImage proxyImage;
    private int isJump = 0;
    public SkyShiba(int x, int y){
        super(x, y);
        if(proxyImage == null) {
            proxyImage = new ProxyImage("/assets/round1/image/skyShiba.png");
        }
        this.image = proxyImage.loadImage().getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.x -= width;
        this.y -= height;
        this.dy = 4;
    }
    @Override
    public void tick() {
        if(dy < 5) {
            dy += 2;
        }
        this.y += dy;
        checkWindowBorder();
    }
    public void jump() {
        if(dy > 0) {
            dy = 0;
        }
        dy -= 15;
        isJump = 0;
    }
    
    private void checkWindowBorder() {
        if(this.x > Window.WIDTH) {
            this.x = Window.WIDTH;
        }
        if(this.x < 0) {
            this.x = 0;
        }
        if(this.y > Window.HEIGHT - 160) {
            this.y = Window.HEIGHT - 160;
        }
        if(this.y < 0) {
            this.y = 0;
        }
    }

    @Override
    public void render(Graphics2D g, ImageObserver obs) {
        if(isJump < 5){
            g.drawImage(this.flyImage, x, y, obs);
            isJump ++;
        }else{
            if(animation < 3){
                g.drawImage(this.image, x, y, obs);
                animation ++;
            }else{
                g.drawImage(this.secondImage, x, y, obs);
                animation ++;
                if(animation == 6){
                    animation = 0;
                }

            }

        }


    }
    
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width - 30, height-30);
    }
}
