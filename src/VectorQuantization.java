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

import java.awt.image.*;
import java.io.*;
import java.util.*;

//Class that performs the vector quantization
public class VectorQuantization {

    //Image Parameters
    static int imageWidth = 352;
    static int imageHeight = 288;

    //Command line parameters
    static int N;
    static int mode;
    static String fileName;
    static Cluster[] clusters;
    static int multiplier = 1;
    static boolean isRGB = false;

    //Images that are displayed, left side and right side
    static BufferedImage img1;
    static BufferedImage img2;

    //Creates the number of clusters required for the program
    public void createCulsters(BufferedImage image){
        clusters = new Cluster[N];
        int x = 0;
        int y = 0;
        int dx = imageWidth/N;
        int dy = imageHeight/N;
        for (int i = 0; i < N; i++) {
            clusters[i] = new Cluster(i,image.getRGB(x,y), isRGB);
            x = x + (dx);
            y = y + (dy);
        }
    }

    //Main method that perfoms vector quantization
    public void ProcessImage(ImageDisplay imgDisplay){
        imgDisplay.PrepareImages();
        if (fileName.endsWith(".rgb")){
            isRGB = true;
        }
        try {
            //Input for image file
            File imageFile = new File(fileName);
            InputStream fileStream = new FileInputStream(imageFile);
            long len = imageFile.length();
            byte[] bytes = new byte[(int)len];
            int offset = 0;
            int numberRead;
            int index = 0;
            while (offset < bytes.length && (numberRead=fileStream.read(bytes, offset, bytes.length-offset)) >= 0){
                offset = offset + numberRead;
            }
            //Read the image file
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    byte r = bytes[index];
                    byte g = bytes[index];
                    byte b = bytes[index];
                    //To read an image file with color
                    if (fileName.endsWith(".rgb")){
                        g = bytes[index+(imageWidth*imageHeight)];
                        b = bytes[index+(2*imageWidth*imageHeight)];
                    }
                    int pixel = 0Xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff) << 0;
                    img1.setRGB(x,y,pixel);
                    index = index + 1;
                }
            }
            //Close the image file
            fileStream.close();
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        //Computer vector space as defined in the assignment question
        int[] vectorSpace = determineVectorSpace(VectorQuantization.img1);

        //Create the new image using the vector space mapping
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int clusterid = vectorSpace[imageWidth * y + x];
                img2.setRGB(x, y, clusters[clusterid].getPixel());
            }
        }
    }

    //Method to calculate the vector space
    public int[] determineVectorSpace(BufferedImage image){
        createCulsters(image);
        int[] vectorSpace = new int[imageWidth*imageHeight];
        //init all values to -1
        Arrays.fill(vectorSpace,-1);
        boolean shouldRefine = true;
        while(shouldRefine){
            shouldRefine = false;
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    if (1 == mode) { //adjacent pixel pair - 2 pixels
                        int pixel = image.getRGB(x, y);
                        Cluster cluster = nearestCluster(pixel);
                        if (cluster.getId() != vectorSpace[imageWidth * y + x]) {
                            if (-1 != vectorSpace[imageWidth * y + x]) {
                                clusters[vectorSpace[imageWidth * y + x]].removePixel(pixel, isRGB);
                            }
                            cluster.addPixel(pixel, isRGB);
                            shouldRefine = true;
                            vectorSpace[imageWidth * y + x] = cluster.getId();
                        }
                    }else if (2 == mode){ // mode 2 execution 2x2 blocks - 4 pixels
                        int pixel1 = image.getRGB(x, y);
                        int pixel2 = 0;
                        int pixel3 = 0;
                        int pixel4 = 0;
                        boolean shouldAddPixels = false;
                        if (x+1 < imageWidth && y+1 < imageHeight) {
                            pixel2 = image.getRGB(x + 1, y);
                            pixel3 = image.getRGB(x, y + 1);
                            pixel4 = image.getRGB(x + 1, y + 1);
                            shouldAddPixels = true;
                        }
                        Cluster cluster = nearestCluster(pixel1);
                        if (cluster.getId() != vectorSpace[imageWidth * y + x]) {
                            if (-1 != vectorSpace[imageWidth * y + x]) {
                                clusters[vectorSpace[imageWidth * y + x]].removePixel(pixel1, isRGB);
                            }
                            cluster.addPixel(pixel1, isRGB);
                            if (shouldAddPixels) {
                                cluster.addPixel(pixel2, isRGB);
                                cluster.addPixel(pixel3, isRGB);
                                cluster.addPixel(pixel4, isRGB);
                            }
                            shouldRefine = true;
                            vectorSpace[imageWidth * y + x] = cluster.getId();
                        }
                    }else { //mode 3 execution 4x4 blocks - 16 pixels
                        int pixel1 = image.getRGB(x, y);
                        int pixel2 = 0, pixel3 = 0, pixel5 = 0, pixel6 = 0;
                        int pixel7 = 0, pixel8 = 0, pixel9 = 0, pixel10 = 0;
                        int pixel11 = 0, pixel12 = 0, pixel13 = 0, pixel4 = 0;
                        int pixel14 = 0, pixel15 = 0, pixel16 = 0;
                        boolean shouldAddPixels1 = false;
                        boolean shouldAddPixels2 = false;
                        boolean shouldAddPixels3 = false;
                        if ((x+1 < imageWidth) && (y+1 < imageHeight)) {
                            pixel2 = image.getRGB(x + 1, y);
                            pixel3 = image.getRGB(x, y + 1);
                            pixel4 = image.getRGB(x + 1, y + 1);
                            shouldAddPixels1 = true;
                        }
                        if ((x+2 < imageWidth) && (y+2 < imageHeight)) {
                            pixel5 = image.getRGB(x , y+2);
                            pixel6 = image.getRGB(x + 1, y + 2);
                            pixel7 = image.getRGB(x + 2, y + 2);
                            pixel8 = image.getRGB(x + 2, y + 1);
                            pixel9 = image.getRGB(x + 2, y);
                            shouldAddPixels2 = true;
                        }
                        if ((x+3 < imageWidth) && (y+3 < imageHeight)) {
                            pixel10 = image.getRGB(x + 3, y);
                            pixel11 = image.getRGB(x + 3, y + 1);
                            pixel12 = image.getRGB(x + 3, y + 2);
                            pixel13 = image.getRGB(x + 3, y + 3);
                            pixel14 = image.getRGB(x + 2, y + 3);
                            pixel15 = image.getRGB(x + 1, y + 3);
                            pixel16 = image.getRGB(x, y + 3);
                            shouldAddPixels3 = true;
                        }
                        Cluster cluster = nearestCluster(pixel1);
                        if (cluster.getId() != vectorSpace[imageWidth * y + x]) {
                            if (-1 != vectorSpace[imageWidth * y + x]) {
                                clusters[vectorSpace[imageWidth * y + x]].removePixel(pixel1, isRGB);
                            }
                            cluster.addPixel(pixel1, isRGB);
                            if (shouldAddPixels1) {
                                cluster.addPixel(pixel2, isRGB);
                                cluster.addPixel(pixel3, isRGB);
                                cluster.addPixel(pixel4, isRGB);
                            }
                            if (shouldAddPixels2){
                                cluster.addPixel(pixel5, isRGB);
                                cluster.addPixel(pixel6, isRGB);
                                cluster.addPixel(pixel7, isRGB);
                                cluster.addPixel(pixel8, isRGB);
                                cluster.addPixel(pixel9, isRGB);
                            }
                            if (shouldAddPixels3){
                                cluster.addPixel(pixel10, isRGB);
                                cluster.addPixel(pixel11, isRGB);
                                cluster.addPixel(pixel12, isRGB);
                                cluster.addPixel(pixel13, isRGB);
                                cluster.addPixel(pixel14, isRGB);
                                cluster.addPixel(pixel15, isRGB);
                                cluster.addPixel(pixel16, isRGB);
                            }
                            shouldRefine = true;
                            vectorSpace[imageWidth * y + x] = cluster.getId();
                        }
                    }
                }
            }
        }
        return vectorSpace;
    }

    //Returns the nearest cluster
    public Cluster nearestCluster(int pixel){
        Cluster cluster = null;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {
            int distance = clusters[i].distance(pixel);
            if (min > distance){
                min = distance;
                cluster = clusters[i];
            }
        }
        return cluster;
    }
}
