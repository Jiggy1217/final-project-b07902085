/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lovingshiba.view;



import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;


public class Window {
    public static int WIDTH = 1000;
    public static int HEIGHT = 640;
    public JFrame frame = new JFrame();
    public Window(int width, int height, String title) {

        this.frame.setTitle(title);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setMaximumSize(new Dimension(width, height));
        this.frame.setPreferredSize(new Dimension(width, height));
        this.frame.setMinimumSize(new Dimension(width, height));
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        
    }
    public void changeFrame(Game newGame){
        this.frame.getContentPane().removeAll();
        this.frame.add(newGame);
        this.frame.setVisible(true);
        newGame.startFocus();
    }

    public static void main(String[] args) {

        /* Set the System look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */

        java.awt.EventQueue.invokeLater(() -> {
            Window window = new Window(WIDTH, HEIGHT, "Loving Shiba");
            Game skygame = new SkyGame(window);
            window.changeFrame(skygame);

        });

    }
}
