/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 */

public class Country {
    public Country (long from, long to, String code2, String code3 , String name){
        this.from = from ;
        this.to = to ;
        this.code2 = code2;
        this.code3 = code3 ;
        this.name = name ;
    }

    public Country (long ip) {
        this.from = ip ;
        this.to = ip ;
    }

    public long getFrom () { return from ; }
    public long getTo ()   { return to   ; }
    public String getCode2 () { return code2 ; }
    public String getCode3 () { return code3; }
    public String getName() { return name ; }

    public void setFrom (long from) { this.from = from ; }
    public void setTo (long to) { this.to =  to; }
    public void setCode2 (String code2) { this.code2 = code2 ; }
    public void setCode3 (String code3) { this.code3 = code3 ; }
    public void setName (String name) { this.name = name ; }

    public void releaseMemory () {
        from=to=0;
        name=code2=code3 = null ;
    }
    
    @Override
    public String toString() { return getFrom() + ":" + getName() ; }

    private long from, to;
    private String name,code2,code3 ;
}
