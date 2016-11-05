/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecceg;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author Moch Ginanjar Busiri
 */
public class Ecceg {
    private static final int KOBLITZ = 20;
    
    public static void encode(String plain, ArrayList<Point> Pm) {
        for (int i = 0; i < plain.length(); i+=10) {
            BigInteger m;
            if (i+10 <= plain.length())
                m = new BigInteger(plain.substring(i, i+10).getBytes());
            else
                m = new BigInteger(plain.substring(i, plain.length()).getBytes());
            
            int it = 1;
            BigInteger x = (m.multiply(BigInteger.valueOf(KOBLITZ))).add(BigInteger.valueOf(it));
            BigInteger y = calculatePm_Y(x, Point.A, Point.B, Point.P);
            while (!inCurve(x, y)) {
                it++;
                x = (m.multiply(BigInteger.valueOf(KOBLITZ))).add(BigInteger.valueOf(it));
                y = calculatePm_Y(x, Point.A, Point.B, Point.P);
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
            
            Point temp1 = new Point(C1.getX(), C1.getY().multiply(BigInteger.valueOf(-1)));
            Point temp2 = Point.multiplyPoint(key, temp1);
            
            Point C3 = Point.addPoint(C2, temp2);
            Pm.add(C3);
        }
    }
    
    public static String decode(ArrayList<Point> Pm) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Pm.size(); i++) {
            BigInteger result = (Pm.get(i).getX().subtract(BigInteger.valueOf(1))).divide(BigInteger.valueOf(KOBLITZ));
//            System.out.println("RESULT = "+result);
//            String resultByte = new String(result.toByteArray());
            byte[] resultByte = result.toByteArray();
            if (resultByte[0] == 0) {
                byte[] tmp = new byte[resultByte.length - 1];
                System.arraycopy(resultByte, 1, tmp, 0, tmp.length);
                resultByte = tmp;
            }
            sb = sb.append(new String(resultByte,"UTF-8"));
        }
        return sb.toString();
    }
    
    public static BigInteger calculatePm_Y(BigInteger x, BigInteger a, BigInteger b, BigInteger p) {
        BigInteger temp = x.pow(3).add(a.multiply(x)).add(b).mod(p);
        return temp.modPow(p.add(BigInteger.valueOf(1)).divide(BigInteger.valueOf(4)), p);
    }
    
    public static boolean inCurve(BigInteger x, BigInteger y) {
        return (y.pow(2).mod(Point.P)).equals(x.pow(3).add(Point.A.multiply(x)).add(Point.B).mod(Point.P));
    }
}