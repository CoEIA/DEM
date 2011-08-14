/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.parse;

/**
 *
 * @author wajdyessam
 *
 */

import java.io.InputStreamReader ;
import java.io.InputStream;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document ;

import org.jsoup.Jsoup;

public class HTMLParser implements TypeParser {
    private String filePath ;

    public HTMLParser (String fp) {
        filePath = fp ;
    }

    public static String getStringFromHTMLPage (String page) {
        org.jsoup.nodes.Document doc = Jsoup.parse(page);
        String text = doc.body().text();

        return text ;
    }
    
    public Document parseDocument (InputStream is) throws Exception {
        //System.out.println("Parse File: " + filePath );
        
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setShowErrors(0);
        
        InputStreamReader file = new InputStreamReader(is,"UTF8");

        org.w3c.dom.Document root = tidy.parseDOM(file, null);
        Element rawDoc = root.getDocumentElement();

        Document doc =  new Document();
     
        String title = getTitle(rawDoc);
        String body = getBody(rawDoc);

        if ((title != null) && (!title.equals(""))) {
//            doc.add(Field.Text("title", title));
        }
        if ((body != null) && (!body.equals(""))) {
//            doc.add(Field.UnStored("body", body));
//            doc.add(Field.Keyword("filename", filePath));
        }
 
        return doc;
    }

    /**
    * Gets the title text of the HTML document.
    *
    * @rawDoc the DOM Element to extract title Node from
    * @return the title text
    */
    protected String getTitle(Element rawDoc) {
        if (rawDoc == null) {
            return null;
        }

        String title = "";

        NodeList children = rawDoc.getElementsByTagName("title");
        if (children.getLength() > 0) {
            Element titleElement = ((Element) children.item(0));
            Text text = (Text) titleElement.getFirstChild();
            if (text != null) {
                title = text.getData();
            }
        }

        return title;
     }

    /**
    * Gets the body text of the HTML document.
    *
    * @rawDoc the DOM Element to extract body Node from
    * @return the body text
    */
    protected String getBody(Element rawDoc) {
        if (rawDoc == null) {
            return null;
        }

        String body = "";
        NodeList children = rawDoc.getElementsByTagName("body");
        if (children.getLength() > 0) {
            body = getText(children.item(0));
        }
        
        return body;
    }

    /**
    * Extracts text from the DOM node.
    *
    * @param node a DOM node
    * @return the text value of the node
    */
    protected String getText(Node node) {
        NodeList children = node.getChildNodes();
        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    sb.append(getText(child));
                    sb.append(" ");
                break;


                case Node.TEXT_NODE:
                    sb.append(((Text) child).getData());
                break;
            }
        }

        return sb.toString();
    }
}
