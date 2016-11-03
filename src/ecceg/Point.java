/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecceg;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Moch Ginanjar Busiri
 */
public class Point {
    public static final BigInteger a = BigInteger.valueOf(12312);
    public static final BigInteger b = BigInteger.valueOf(5346345);;
    public static final BigInteger p = (BigInteger.valueOf(2).pow(192).subtract(BigInteger.valueOf(2).pow(64))).subtract(BigInteger.valueOf(1));
    public static final BigInteger INF = new BigInteger("-1");
    
    private final BigInteger x;
    private final BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }
    
    public static BigInteger gradientAdd(Point P, Point Q) {
        return (P.getY().subtract(Q.getY()).modInverse(P.getX().subtract(Q.getX())));
    }
    
    public static BigInteger resultAdd_X(Point P, Point Q) {
        return ((gradientAdd(P, Q).pow(2)).subtract(P.getX())).subtract(Q.getX());
    }
    
    public static BigInteger resultAdd_Y(Point P, Point Q) {
        return (gradientAdd(P, Q).multiply(P.getX().subtract(resultAdd_X(P, Q)))).subtract(P.getY());
    }
    
    public static BigInteger gradientDouble(Point P, Point Q) {
        return ((P.getX().pow(2).multiply(new BigInteger("3"))).add(a)).modInverse(P.getY().multiply(new BigInteger("2")));
    }
    
    public static BigInteger resultDouble_X(Point P, Point Q) {
        return (gradientDouble(P, Q).pow(2)).subtract(P.getX().multiply(new BigInteger("2")));
    }
    
    public static BigInteger resultDouble_Y(Point P, Point Q) {
        return (gradientDouble(P, Q).multiply(P.getX().subtract(resultDouble_X(P, Q)))).subtract(P.getY());
    }
    
    public static Point multiplyPoint(BigInteger k, Point point) {
        if (k.equals(BigInteger.ZERO))
            return new Point(INF, INF);
        if (k.equals(BigInteger.ONE))
            return point;
        Point point2 = multiplyPoint(k.divide(BigInteger.valueOf(2)), new Point(resultDouble_X(point, point), resultDouble_Y(point, point)));
        if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE))
            point2 = new Point(resultAdd_X(point2, point), resultAdd_Y(point2, point));
        return point2;
    }
    
    public static Point addPoint(Point P, Point Q) {
        Point result = new Point(resultAdd_X(P, Q), resultAdd_Y(P, Q));
        return result;
    }
    
    @Override
    public String toString(){
        return x.toString() + " " + y.toString();
    }
}
