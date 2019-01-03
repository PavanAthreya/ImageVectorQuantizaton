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

//Class that encapsulates each cluster and give functions to add and remove pixels and create mapping for quantized image
public class Cluster {
    //Parameters
    int average;
    int averageRed;
    int averageGreen;
    int averageBlue;
    int totalRed;
    int totalGreen;
    int totalBlue;
    int pixelNumber;
    int id;

    //Init method which takes id of the cluster
    public Cluster(int id, int pixel, boolean isRGB){
        this.id = id;
        averageRed = (pixel >> 16) & 0xff;
        if (isRGB) {
            averageGreen = (pixel >> 8) & 0xff;
            averageBlue = (pixel & 0xff);
        }else{
            averageGreen = averageRed;
            averageBlue = averageRed;
        }
        average = 0Xff000000 | ((averageRed & 0xff) << 16) | ((averageGreen & 0xff) << 8) | (averageBlue & 0xff) << 0;
        addPixel(pixel, isRGB);
    }

    //Method to get pixel value for that cluster
    int getPixel(){
        int r = averageRed;
        int g = averageGreen;
        int b = averageBlue;
        return 0Xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    //Method to get id of the cluster
    int getId(){
        return id;
    }

    //Method to add new pixels and compute the neighbouring value averages
    void addPixel(int pixel, boolean isRGB){
        int red = (pixel >> 16) & 0xff;
        int green, blue;
        if (isRGB) {
            green = (pixel >> 8) & 0xff;
            blue = (pixel & 0xff);
        }else{
            green = red;
            blue = red;
        }
        pixelNumber = pixelNumber + 1;
        totalRed = totalRed + red;
        totalGreen = totalGreen + green;
        totalBlue = totalBlue + blue;
        averageRed = totalRed/pixelNumber;
        averageGreen = totalGreen/pixelNumber;
        averageBlue = totalBlue/pixelNumber;
    }

    //Method to remove new pixels and re-compute the neighbouring value averages
    void removePixel(int pixel, boolean isRGB){
        int red = (pixel >> 16) & 0xff;
        int green, blue;
        if (isRGB) {
            green = (pixel >> 8) & 0xff;
            blue = (pixel & 0xff);
        }else{
            green = red;
            blue = red;
        }
        pixelNumber = pixelNumber - 1;
        totalRed = totalRed - red;
        totalGreen = totalGreen - green;
        totalBlue = totalBlue - blue;
        averageRed = totalRed/pixelNumber;
        averageGreen = totalGreen/pixelNumber;
        averageBlue = totalBlue/pixelNumber;
    }

    //Method to calculate the distance between two pixels
    int distance(int pixel){
        int px = pixel & 0xFFFFFFFF;
        return Math.abs(average - px);
    }
}
