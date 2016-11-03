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
    private BigInteger a;
    private BigInteger b;
    private BigInteger p;
    
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
    
    public BigInteger gradientAdd(Point P, Point Q) {
        return (P.getY().subtract(Q.getY()).divide(P.getX().subtract(Q.getX())));
    }
    
    public BigInteger resultAdd_X(Point P, Point Q) {
        return ((gradientAdd(P, Q).pow(2)).subtract(P.getX())).subtract(Q.getX());
    }
    
    public BigInteger resultAdd_Y(Point P, Point Q) {
        return (gradientAdd(P, Q).multiply(P.getX().subtract(resultAdd_X(P, Q)))).subtract(P.getY());
    }
}
