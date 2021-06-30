/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.controller;

import com.lovingshiba.model.GameObject;
import com.lovingshiba.model.SkyShiba;
import java.awt.event.KeyEvent;


public interface IStrategy {
    
    public void controller(SkyShiba bird, KeyEvent kevent);
    public void controllerReleased(GameObject shiba, KeyEvent kevent);
}
