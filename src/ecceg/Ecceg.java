/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecceg;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author Moch Ginanjar Busiri
 */
public class Ecceg {
    private static final int koblitz = 20;
    
    public static void encode(String plain, ArrayList<Point> listPoint) {
        ArrayList<String> listString = new ArrayList<>();
        
        for (int i = 0; i < plain.length(); i+=25) {
            BigInteger m;
            if (i+25 <= plain.length())
                m = new BigInteger(plain.substring(i, i+25).getBytes());
            else
                m = new BigInteger(plain.substring(i, plain.length()).getBytes());
            
            int it = 1;
            BigInteger x = (m.multiply(BigInteger.valueOf(koblitz))).add(BigInteger.valueOf(it));
            BigInteger y = calculatePm_Y(x, Point.a, Point.b, Point.p);
            while (!x.equals(y)) {
                it++;
                x = (m.multiply(BigInteger.valueOf(koblitz))).add(BigInteger.valueOf(it));
                y = calculatePm_Y(x, Point.a, Point.b, Point.p);
            }   
            listPoint.add(new Point(x, y));
        }
    }
    
    public static void encrypt(ArrayList<Point> listPoint) {
        
    }
    
    public static void decode() {
        
    }
    
    public static void decrypt() {
        
    }
    
    public static BigInteger calculatePm_Y(BigInteger x, BigInteger a, BigInteger b, BigInteger p) {
        BigInteger temp = x.pow(3).add(a.multiply(x)).add(b).mod(p);
        return temp.pow(p.add(BigInteger.valueOf(1)).divide(BigInteger.valueOf(4)).intValue()).mod(p);
    }
}