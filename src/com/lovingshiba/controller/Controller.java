/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.controller;

import com.lovingshiba.model.GameObject;
import com.lovingshiba.model.SkyShiba;
import com.lovingshiba.model.SpaceShiba;
import com.lovingshiba.model.WaterShiba;

import java.awt.event.KeyEvent;


public class Controller implements IStrategy {

    @Override
    public void controller(SkyShiba bird, KeyEvent kevent) {
    }

    @Override
    public void controllerReleased(GameObject shiba, KeyEvent kevent) {
        if(kevent.getKeyCode() == KeyEvent.VK_SPACE) {
            if(shiba instanceof SkyShiba){
                SkyShiba tempShiba = (SkyShiba) shiba;
                tempShiba.jump();
            }
            else if (shiba instanceof SpaceShiba) {
                SpaceShiba tempShiba = (SpaceShiba) shiba;
                tempShiba.attack();
            }
        }
        if(kevent.getKeyCode() == KeyEvent.VK_DOWN) {
            if(shiba instanceof WaterShiba){
                WaterShiba tempShiba = (WaterShiba) shiba;
                tempShiba.down();
            }
            else if (shiba instanceof SpaceShiba) {
                SpaceShiba tempShiba = (SpaceShiba) shiba;
                tempShiba.down();
            }
        }
        if(kevent.getKeyCode() == KeyEvent.VK_UP) {
            if(shiba instanceof WaterShiba){
                WaterShiba tempShiba = (WaterShiba) shiba;
                tempShiba.up();
            }
            else if (shiba instanceof SpaceShiba) {
                SpaceShiba tempShiba = (SpaceShiba) shiba;
                tempShiba.up();
            }
        }
    }
    
}
