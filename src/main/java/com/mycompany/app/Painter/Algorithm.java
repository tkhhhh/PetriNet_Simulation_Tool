package com.mycompany.app.Painter;


import java.util.HashSet;
import java.util.Random;

public class Algorithm {
    public static HashSet<int[]> calculatePoints(int x, int y, int dimension, int marking) {
        HashSet<int[]> result = new HashSet<>();
        int central_x = x + dimension / 2;
        int central_y = y + dimension / 2;
        Random rand = new Random();
        while(marking > 0){
            int distance = 5;
            while(distance >= -dimension*dimension/25) {
                int x_rand = rand.nextInt(dimension) + x;
                int y_rand = rand.nextInt(dimension) + y;
                int r_rand = (x_rand - central_x)*(x_rand - central_x) + (y_rand - central_y)*(y_rand - central_y);
                distance = r_rand - dimension*dimension/4;
                if(distance < -dimension*dimension/25) {
                    int[] position = new int[]{x_rand, y_rand};
                    if(!result.contains(position)){
                        result.add(position);
                        break;
                    }
                }
            }
            marking--;
        }
        return result;
    }

    public static HashSet<int[]> calculatePointsSquare(int x, int y, int xDimension, int yDimension, int marking) {
        HashSet<int[]> result = new HashSet<>();
        Random rand = new Random();
        while(marking > 0){
            int x_rand = rand.nextInt(xDimension - 10) + x + 5;
            int y_rand = rand.nextInt(yDimension - 10) + y + 5;
            int[] position = new int[]{x_rand, y_rand};
            if(!result.contains(position)){
                result.add(position);
            }
            marking--;
        }
        return result;
    }
}
