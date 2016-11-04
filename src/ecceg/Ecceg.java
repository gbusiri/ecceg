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
    
    public static void encode(String plain, ArrayList<Point> Pm) {
        for (int i = 0; i < plain.length(); i+=10) {
            System.out.println("i = "+i);
            BigInteger m;
            if (i+10 <= plain.length())
                m = new BigInteger(plain.substring(i, i+10).getBytes());
            else
                m = new BigInteger(plain.substring(i, plain.length()).getBytes());
            
            int it = 1;
            BigInteger x = (m.multiply(BigInteger.valueOf(koblitz))).add(BigInteger.valueOf(it));
            BigInteger y = calculatePm_Y(x, Point.a, Point.b, Point.p);
            while (!inCurve(x, y)) {
                System.out.println("x = "+x+" ---- y = "+y);
                it++;
                x = (m.multiply(BigInteger.valueOf(koblitz))).add(BigInteger.valueOf(it));
                y = calculatePm_Y(x, Point.a, Point.b, Point.p);
            }   
            Pm.add(new Point(x, y));
        }
    }
    
    public static void encrypt(ArrayList<Point> Pm, Point Pb, Point kPb, ArrayList<Pair<Point, Point> > C, BigInteger y) {
        for (int i = 0; i < Pm.size(); i++) {
            Point param1 = Point.multiplyPoint(y, Pb);
            Point param2 = Point.addPoint(Pm.get(i), Point.multiplyPoint(y, kPb));
            C.add(new Pair(param1, param2));
        }
    }
    
    public static void decrypt(ArrayList<Pair<Point, Point> > C, BigInteger key, ArrayList<Point> Pm) {
        for (int i = 0; i < C.size(); i++) {
            Point C1 = C.get(i).getFirst();
            Point C2 = C.get(i).getSecond();
            
            Point temp1 = new Point(C1.getX(), C1.getY());
            Point temp2 = Point.multiplyPoint(key, temp1);
            
            Point C3 = new Point(C2.getX().subtract(temp2.getX()), C2.getY().subtract(temp2.getY()));
            Pm.add(C3);
        }
    }
    
    public static void decode(String cipher, ArrayList<Point> Pm) {
        for (int i = 0; i < Pm.size(); i++) {
            BigInteger result = (Pm.get(i).getX().subtract(BigInteger.valueOf(1))).divide(BigInteger.valueOf(koblitz));
            System.out.println("RESULT = "+result);
//            String temp = new String(result.toByteArray());
            byte[] temp = new byte[result.toByteArray().length - 1];
            cipher.concat(new String(temp));
            System.out.println("CIPHER = "+cipher);
        }
    }
    
    public static BigInteger calculatePm_Y(BigInteger x, BigInteger a, BigInteger b, BigInteger p) {
        BigInteger temp = x.pow(3).add(a.multiply(x)).add(b).mod(p);
        return temp.modPow(p.add(BigInteger.valueOf(1)).divide(BigInteger.valueOf(4)), p);
    }
    
    public static boolean inCurve(BigInteger x, BigInteger y) {
        return (y.pow(2).mod(Point.p)).equals(x.pow(3).add(Point.a.multiply(x)).add(Point.b).mod(Point.p));
    }
}