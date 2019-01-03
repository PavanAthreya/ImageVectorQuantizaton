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

//Main entry class of the assignment
public class Main {

    //Main entry point of the application
    //All command line arguments are passed here
    public static void main(String[] args) {
        //Initialize the image display class used in Assignment 1
        ImageDisplay imgDisplay = new ImageDisplay();

        //Perform Vector Quantization
        VectorQuantization quantization = new VectorQuantization();
        VectorQuantization.N = praseNumeberofCodeWords(args[1]);
        VectorQuantization.fileName = parseImageName(args[0]);
        VectorQuantization.mode = parseMode(args[2]);

        //Print program parameters
        System.out.printf("Assignment Parameters:\n");
        System.out.printf("\tImage Name: %s\n",VectorQuantization.fileName);
        if (VectorQuantization.fileName.endsWith(".rgb")){
            System.out.printf("\tImage Type: Color\n");
        }else {
            System.out.printf("\tImage Type: Grayscale\n");
        }
        System.out.printf("\tNumber of Clusters: %d\n",VectorQuantization.N);
        System.out.printf("\tMode: %d\n",VectorQuantization.mode);

        System.out.println("Beginning quantization process");
        //Process image
        quantization.ProcessImage(imgDisplay);

        //At the end show both images
        imgDisplay.showIms();
    }

    //Method to parse and validate image name
    private static String parseImageName(String imageName) {
        if (imageName != null){
            if (imageName.length() == 0){
                QuitProgram("Invalid file name");
            }else if (imageName.endsWith(".raw") || imageName.endsWith(".rgb")){
                return imageName;
            }
            else{
                QuitProgram("Invalid file name");
            }
        }else{
            QuitProgram("Invalid file name");
        }
        return "";
    }

    //Method to parse and validate number of clusters/codewords
    private static int praseNumeberofCodeWords(String parameter) {
        int N = Integer.parseInt(parameter);
        if (N <= 0){
            QuitProgram("Invalid number of clusters");
        }else if (!isPowerOfTwo(N)){
            QuitProgram("Number is not in powers of 2");
        }
        return N;
    }

    // Function to check if x is power of 2
    private static boolean isPowerOfTwo(int n)
    {
        if (n == 0) {
            return false;
        }
        while (n != 1)
        {
            if (n % 2 != 0) {
                return false;
            }
            n = n / 2;
        }
        return true;
    }

    //Method to parse and validate mode
    private static int parseMode(String mode){
        int m = Integer.parseInt(mode);
        if (m > 0 && m < 4){
            return m;
        }else{
            QuitProgram("Mode not in range");
            return 0;
        }
    }

    //Reusable method to print the error message and quit the program
    public static void QuitProgram(String message){
        System.out.println(message);
        System.exit(1);
    }
}
