/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.model;

import com.lovingshiba.model.proxy.ProxyImage;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;


public class Tube extends GameObject {
    public String type;
    private ProxyImage proxyImage;
    private ProxyImage upProxyImage = new ProxyImage("/assets/round1/image/TubeUp.png");
    private Image upImage = upProxyImage.loadImage().getImage();
    private ProxyImage downProxyImage = new ProxyImage("/assets/round1/image/TubeDown.png");
    private Image downImage = downProxyImage.loadImage().getImage();
    public Tube(int x, int y,String type) {
        super(x, y);
        if (proxyImage == null) {
            proxyImage = new ProxyImage("/assets/round1/image/TubeBody.png");

        }
        this.type = type;
        this.image = proxyImage.loadImage().getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    @Override
    public void tick() {
        this.x -= dx;
    }

    @Override
    public void render(Graphics2D g, ImageObserver obs) {
        if(this.type.equals("up")){
            g.drawImage(this.upImage, x, y-60, obs);
        }else if(this.type.equals("down")){
            g.drawImage(this.downImage, x, y-60, obs);
        }else {
            g.drawImage(image, x, y - 60, obs);
        }

    }

    
    public Rectangle getBounds() {
        return new Rectangle(x, y-60, width, height);
    }
}
