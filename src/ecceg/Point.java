/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecceg;

import java.math.BigInteger;

/**
 *
 * @author Moch Ginanjar Busiri
 */
public class Point {
    public static final BigInteger A = BigInteger.valueOf(12312);
    public static final BigInteger B = BigInteger.valueOf(5346345);;
    public static final BigInteger P = (BigInteger.valueOf(2).pow(192).subtract(BigInteger.valueOf(2).pow(64))).subtract(BigInteger.valueOf(1));
    public static final BigInteger INF = new BigInteger("-1");
    
    private final BigInteger x;
    private final BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = bePositive(x, P);
        this.y = bePositive(y, P);
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }
    
    public static BigInteger gradientAdd(Point P, Point Q) {
        return (P.getY().subtract(Q.getY()).multiply(P.getX().subtract(Q.getX()).modInverse(Point.P))).mod(Point.P);
    }
    
    public static BigInteger resultAdd_X(Point P, Point Q) {
        return ((gradientAdd(P, Q).pow(2)).subtract(P.getX())).subtract(Q.getX()).mod(Point.P);
    }
    
    public static BigInteger resultAdd_Y(Point P, Point Q) {
        return (gradientAdd(P, Q).multiply(P.getX().subtract(resultAdd_X(P, Q)))).subtract(P.getY()).mod(Point.P);
    }
    
    public static BigInteger gradientDouble(Point P) {
        BigInteger param1 = P.getX().pow(2).multiply(new BigInteger("3"));
        BigInteger param2 = P.getY().multiply(new BigInteger("2"));
        return param1.add(A).multiply(param2.modInverse(Point.P));
//        if (param1.gcd(param2).equals(BigInteger.ONE)) {
//            System.out.println("relative prima");
//            return param1.add(A).modInverse(param2);
//        }
//        else {
//            System.out.println("tidak rel prima");
//            return BigInteger.ZERO;
//        }
            
    }
    
    public static BigInteger resultDouble_X(Point P) {
        return (gradientDouble(P).pow(2)).subtract(P.getX().multiply(new BigInteger("2"))).mod(Point.P);
    }
    
    public static BigInteger resultDouble_Y(Point P) {
        return (gradientDouble(P).multiply(P.getX().subtract(resultDouble_X(P)))).subtract(P.getY()).mod(Point.P);
    }
    
    public static Point multiplyPoint(BigInteger k, Point point) {
        if (k.equals(BigInteger.ZERO))
            return new Point(INF, INF);
        if (k.equals(BigInteger.ONE))
            return point;
        
        Point point2;
        if (point.getY().compareTo(BigInteger.valueOf(0)) != 0)
        {
            BigInteger x = resultDouble_X(point);
            BigInteger y = resultDouble_Y(point);
            point2 = multiplyPoint(k.divide(BigInteger.valueOf(2)), new Point(x, y));
        }
        else
        {
            point2 = new Point(INF, INF);
        }
        if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
            if (point.getX().compareTo(BigInteger.valueOf(0)) != 0)
                point2 = new Point(resultAdd_X(point2, point), resultAdd_Y(point2, point));
            else
                point2 = new Point(INF, INF);
        }
        return point2;
    }
    
    public static Point addPoint(Point P, Point Q) {
        Point result = new Point(resultAdd_X(P, Q), resultAdd_Y(P, Q));
        if (P.getX().compareTo(BigInteger.valueOf(0)) != 0)
            return result;
        else
            return new Point(INF, INF);
    }
    
    private BigInteger bePositive(BigInteger a, BigInteger mod) {
        while ( a.compareTo(BigInteger.valueOf(0)) == -1 ) {
            a = a.add(mod);
        }
        return a;
    }
    
    @Override
    public String toString(){
        return x.toString() + "," + y.toString();
    }
}
