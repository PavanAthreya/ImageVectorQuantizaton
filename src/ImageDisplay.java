/*
Name: Pavan Athreya Narasimha Murthy
USD ID: 9129210968
Email: pavan.athreya@usc.edu
Course: CSCI 576
Instructor: Prof. Parag Havaldar
Semester: Fall 2018
Project: Assignment 2
*/

//package com.CSCI576.Assigment2;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

//Class used to display the image taken from assignment 1
public class ImageDisplay {

    //Output Display Frame
    JFrame frame;

    //Labels at the top to show left side and the right side of the images
    JLabel lbIm1;
    JLabel lbIm2;

    //Image frames for original and processed images
    int width = VectorQuantization.imageWidth;
    int height = VectorQuantization.imageHeight;

    //Strings to hold label texts based on the program that is executed
    String labelText1;
    String labelText2;

    // Method to Initialize images
    public void PrepareImages(){
        // Initialize a plain white images
        VectorQuantization.img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        VectorQuantization.img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Set background color for the images
        drawBackground(VectorQuantization.img1);
        drawBackground(VectorQuantization.img2);

        //Draw Borders for the images
        DrawBorders(VectorQuantization.img1);
        DrawBorders(VectorQuantization.img2);
    }

    ////
    //	Main method that is executed when the program wants to display the image
    ////
    public void showIms() {
        // Use labels to display the images
        frame = new JFrame();
        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);

        JLabel lbText1 = new JLabel();
        lbText1.setText(this.labelText1);
        lbText1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lbText2 = new JLabel();
        lbText2.setText(labelText2);
        lbText2.setHorizontalAlignment(SwingConstants.CENTER);
        lbIm1 = new JLabel(new ImageIcon(VectorQuantization.img1));
        lbIm2 = new JLabel(new ImageIcon(VectorQuantization.img2));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        frame.getContentPane().add(lbText1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        frame.getContentPane().add(lbText2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        frame.getContentPane().add(lbIm1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        frame.getContentPane().add(lbIm2, c);

        System.out.println("Presenting the Image");
        frame.pack();
        frame.setVisible(true);
        //Exit on Close is set so that when the rendering is closed, the program would exit and release all memory that was allocated
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    ////
    // 	Draws a black line on the given buffered image from the pixel defined by (x1, y1) to (x2, y2)
    ////
    public void drawLine(BufferedImage image, double x1, double y1, double x2, double y2) {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        //System.out.printf("Line Parameters: x1 = %f, y1 - %f, x2 = %f, y2 = %f\n",x1,y1,x2,y2);
        g.drawLine((int) Math.ceil(x1), (int) Math.ceil(y1), (int) Math.ceil(x2), (int) Math.ceil(y2));
        g.drawImage(image, 0, 0, null);
    }

    ////
    //	Method to draw borders for the image passed in the argument
    ////
    private void DrawBorders(BufferedImage image){
        //System.out.println("Drawing borders for both the images");
        drawLine(image, 0, 0, image.getWidth() - 1, 0);                // top edge
        drawLine(image, 0, image.getHeight() - 1, image.getWidth() - 1, image.getHeight() - 1);    // bottom edge
        drawLine(image, 0, 0, 0, image.getHeight() - 1);                // left edge
        drawLine(image, image.getWidth() - 1, image.getHeight() - 1, image.getWidth() - 1, 0);    // right edge
    }

    ////
    //	Deep copy of the passed image to prevent reference retention in shallow copies of the object
    ////
    private BufferedImage CloneImage(BufferedImage img) {
        ColorModel model = img.getColorModel();
        boolean alpha = model.isAlphaPremultiplied();
        WritableRaster rasterScan = img.copyData(null);
        return new BufferedImage(model, rasterScan, alpha, null);
    }

    ////
    //	Utility method to compute the color for the (x,y) position in the image
    ////
    private int GetColor(BufferedImage img, int x, int y, double scale) {
        return img.getRGB((int) (x * scale), (int) (y * scale));
    }

    ////
    //	Method to draw background for the image
    ////
    public void drawBackground(BufferedImage img) {
        // draw background
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                byte r = (byte) 255;
                byte g = (byte) 255;
                byte b = (byte) 255;
                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8)
                        | (b & 0xff);
                img.setRGB(x, y, pix);
            }
        }
    }
}