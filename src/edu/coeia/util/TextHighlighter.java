/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.util.Version;

public class TextHighlighter {
    public static String getHighlightString (String text, String keyword) throws IOException {
        TermQuery query = new TermQuery(new Term("f", keyword));
	QueryScorer scorer = new QueryScorer(query);
	SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">","</span>");
	Highlighter highlighter = new Highlighter(formatter, scorer);
	Fragmenter fragmenter = new SimpleFragmenter(50);
	highlighter.setTextFragmenter(fragmenter);
	TokenStream tokenStream = new StandardAnalyzer(Version.LUCENE_20).tokenStream("f", new StringReader(text));
	//String result = highlighter.getBestFragments(tokenStream, text, 30, "...");

	StringBuilder writer = new StringBuilder("");
	writer.append("<html>");
	writer.append("<style>\n" +
		".highlight {\n" +
		" background: yellow;\n" +
		"}\n" +
		"</style>");
	writer.append("<body>");
	writer.append("");
	writer.append("</body></html>");

	return ( writer.toString() );
    }

/* lucene 3
    public static String highlightHTML(String htmlText, Query query) {
        QueryScorer scorer =  new QueryScorer(query, FIELD_NAME, FIELD_NAME);
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("\">", "");
        Highlighter highlighter = new Highlighter(htmlFormatter, scorer);

        // Nullfragmenter for highlighting entire document.
        highlighter.setTextFragmenter(new NullFragmenter());
        TokenStream ts = analyzer.tokenStream(FIELD_NAME, new HTMLStripReader(new StringReader(htmlText)));

        try {
            String highlightedText = highlighter.getBestFragment(ts, htmlText);
            if (highlightedText != null) {
                return highlightedText;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return htmlText;
    }
 *
 */
}
