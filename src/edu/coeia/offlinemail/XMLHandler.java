/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.email;


/**
 *
 * @author wajdyessam
 *
 */

import java.io.File ;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException ;

import java.nio.charset.Charset ;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException ;

import java.util.Iterator ;
import java.util.Set ;
import java.util.Map ;

public class XMLHandler {
     public XMLHandler (Map<String,Integer> data, File name, String user) throws XMLStreamException,FileNotFoundException {
        this.data = data ;
        this.fileName = name ;
        this.user = user ;
        
        // write data to file
        writeXMLFile();
    }

    private void writeXMLFile () throws XMLStreamException,FileNotFoundException {
        // open xml file
        openXMLFile();

        // write header
        writeHeader();

        // write node
        writeNodes();

        // write egdes
        writeEdges();

        // close XML file
        closeXML();
    }

    private void closeXML () throws XMLStreamException {
        xtw.writeEndElement();
        xtw.writeEndElement();
        xtw.writeEndDocument();
        xtw.flush();
        xtw.close();
    }
    
//    private void writeEdges() throws XMLStreamException {
//        Set set = data.entrySet();
//        Iterator itr = set.iterator();
//        int id = 0 ;
//
//        while ( itr.hasNext() ) {
//            itr.next();
//            writeEgde(getSource(),id);
//            id++;
//        }
//    }
//
//    private void writeEgde (int source, int target) throws XMLStreamException {
//        xtw.writeStartElement("edge");
//        xtw.writeAttribute("source", source + "");
//        xtw.writeAttribute("target", target + "");
//        xtw.writeEndElement();
//    }

    private void writeEdges () throws XMLStreamException {
        Set set = data.entrySet();
        Iterator itr = set.iterator();
        int id = 0 ;

        while ( itr.hasNext() ) {
            Map.Entry me = (Map.Entry) itr.next();
            int size = (Integer) me.getValue();
            
            writeEdge(getSource(),size, id);
            id++;
        }
    }

    private void writeEdge(int source, int size, int id) throws XMLStreamException {
        xtw.writeStartElement("edge");

        xtw.writeAttribute("id", id + "");
        xtw.writeAttribute("source", source + "");
        xtw.writeAttribute("target", id + "");

        xtw.writeStartElement("data");
        xtw.writeAttribute("key","edge_size");
        xtw.writeCharacters(size+"");
        xtw.writeEndElement();

        xtw.writeEndElement();
    }

    private void writeNodes () throws XMLStreamException {
        Set set = data.entrySet();
        Iterator itr = set.iterator();
        int id = 0 ;
        boolean foundUser = false ;

        while ( itr.hasNext() ) {
            Map.Entry me = (Map.Entry) itr.next();
            String name = (String) me.getKey();
            int size = (Integer) me.getValue();

            if ( name.equals(user)) {
                foundUser = true ;
                sourceId = id ;
            }
            
            writeNode(id,name,size);
            id++;
        }

        if ( ! foundUser ) {
            writeNode(id,user,5);   // default size
            sourceId = id ;
        }
    }

    private void writeNode (int id, String name, int size)throws XMLStreamException  {
        xtw.writeStartElement("node");
        xtw.writeAttribute("id", id + "");

        xtw.writeStartElement("data");
        xtw.writeAttribute("key","name");
        xtw.writeCharacters(name);
        xtw.writeEndElement();

        xtw.writeStartElement("data");
        xtw.writeAttribute("key","size");
        xtw.writeCharacters(size+"");
        xtw.writeEndElement();
        
        xtw.writeEndElement();
    }

    private int getSource () {
        return sourceId ;
    }
    
    private void openXMLFile () throws XMLStreamException,FileNotFoundException {
        xof = XMLOutputFactory.newInstance();
        xtw = xof.createXMLStreamWriter(new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("UTF-8")));
        xtw.writeStartDocument("UTF-8", "1.0");
    }

    private void writeHeader () throws XMLStreamException {
        // header
        xtw.writeStartElement("graphml");
        xtw.writeAttribute("xmlns","http://graphml.graphdrawing.org/xmlns");
        xtw.writeStartElement("graph");
        xtw.writeAttribute("edgedefault","undirected");

        // name values
        xtw.writeStartElement("key");
        xtw.writeAttribute("id","name");
        xtw.writeAttribute("for","node");
        xtw.writeAttribute("attr.name","name");
        xtw.writeAttribute("attr.type","string");
        xtw.writeEndElement();

        // size value
        xtw.writeStartElement("key");
        xtw.writeAttribute("id","size");
        xtw.writeAttribute("for","node");
        xtw.writeAttribute("attr.name","size");
        xtw.writeAttribute("attr.type","int");
        xtw.writeEndElement();

        // edge value
        xtw.writeStartElement("key");
        xtw.writeAttribute("id","edge_size");
        xtw.writeAttribute("for","edge");
        xtw.writeAttribute("attr.name","edge_size");
        xtw.writeAttribute("attr.type","int");
        xtw.writeEndElement();
    }
    
    private Map<String,Integer> data ;
    private File fileName ;
    private String user ;
    private XMLOutputFactory xof ;
    private XMLStreamWriter xtw ;
    private int sourceId = 0 ;
}
