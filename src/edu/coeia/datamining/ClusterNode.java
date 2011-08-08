/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.datamining;

import com.pff.PSTMessage;

/**
 *
 * @author wajdyessam
 */

public class ClusterNode {
    private String nodeName ;
    private PSTMessage message ;

    public ClusterNode (String name) { nodeName = name; }
    public ClusterNode (String name, PSTMessage msg) { nodeName = name;  message = msg; }

    public PSTMessage getMessage () { return message ; }

    @Override
    public String toString(){
        return nodeName ;
    }
}
