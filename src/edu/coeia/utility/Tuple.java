/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.utility;

/**
 *
 * @author wajdyessam
 */

public class Tuple<A,B> {
    private A a ;
    private B b ;

    public Tuple () {
    }

    public Tuple(A a, B b) {
	this.a = a ;
	this.b = b ;
    }

    public void setA (A a) { this.a = a ; }
    public void setB (B b) { this.b = b ; }

    public A getA() { return a ; }
    public B getB() { return b ; }
}
