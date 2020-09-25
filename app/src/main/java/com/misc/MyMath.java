package com.misc;

public class MyMath {

    /**
     * Get the highest common factor of 2 integers, the answer is always positive. If either number
     * is 0 the answer is 1.
     * @param x first integer
     * @param y second integer
     * @return the highest common factor
     */
    public static int hcf(int x, int y)
    {
        if (x == 0 || y == 0) return 1;

        if (x < 0) x = -x;
        if (y < 0) y = -y;

        return hcf2 (x, y);
    }
    static int hcf2 (int x, int y)
    {
        if (x == 1 || y == 1) return 1;
        if (x == y) return x;

        if (x > y){
            x = x % y;
            if (x == 0) return y;
        }
        else{
            y = y % x;
            if (y == 0) return x;
        }

        return hcf2 (x, y);
    }
}
