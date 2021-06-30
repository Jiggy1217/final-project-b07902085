/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.model;

import com.lovingshiba.model.proxy.ProxyImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Random;
import java.util.List;
import java.util.Arrays;


public class Heart extends GameObject {
    private ProxyImage proxyImage;

    public Heart(int x, int y) {
        super(x, y);
        if (proxyImage == null) {
            proxyImage = new ProxyImage("/assets/round1/image/heart.png");
        }

        this.image = proxyImage.loadImage().getImage();
        this.width = image.getWidth(null) - 20;
        this.height = image.getHeight(null) - 20;
        this.dx = 3;
    }

    @Override
    public void tick() {
        this.x -= dx;
    }

    @Override
    public void render(Graphics2D g, ImageObserver obs) {
        g.drawImage(image, x, y, obs);

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
