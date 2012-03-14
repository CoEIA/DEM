/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.cases.Case;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.investigation.CommonKeywordsPanel;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.gutil.JTableUtil;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

/**
 *
 * @author wajdyessam
 */
public class CommonKeywordsTask implements Task{
    private final BackgroundProgressDialog dialog ;
    private final Case aCase;
    private final CommonKeywordsPanel panel;
    
    public CommonKeywordsTask(final Case aCase, final CommonKeywordsPanel panel) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
        this.aCase = aCase;
        this.panel = panel;
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask() throws Exception {
        Map<String, Integer> commonKeywordsMap = this.getAllTermFreqFromItems();
        this.setTags(commonKeywordsMap);
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    public Map<String, Integer> getAllTermFreqFromItems() throws IOException {
        Map<String,Integer> map = new HashMap<String,Integer>();
        
        String indexDir = this.aCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_INDEX_FOLDER;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        TermEnum terms = indexReader.terms();
        
        int factor = indexReader.maxDoc() / 100;
        
        while(terms.next()) {
            if ( isCancelledTask() )
                break;
            
            Term term = terms.term();
            
            if ( this.isAllowedFeild(term.field().trim()) ) { 
                String termText = term.text();
                int frequency = indexReader.docFreq(term);

                if ( frequency >= factor) 
                    map.put(termText, frequency);
            }
        }
        
        System.out.println("map size: " + map.size());
        indexReader.close();
        return map;
    }
    
    private boolean isAllowedFeild(final String fieldName) {
        if ( fieldName.equals(IndexingConstant.FILE_CONTENT ) )
            return true;
        
        if ( fieldName.equals(IndexingConstant.CHAT_MESSAGE) )
            return true;
        
        if ( fieldName.equals(IndexingConstant.OFFLINE_EMAIL_HTML_CONTENT))
            return true;
        
        if ( fieldName.equals(IndexingConstant.ONLINE_EMAIL_BODY))
            return true;
        
        return false;
    }
    
    private void setTags(final Map<String, Integer> tagsMap) {

        int excludeNumber = Integer.parseInt(this.panel.getTagsExclude().getText().trim());
        int tagsNumber = Integer.parseInt(this.panel.getTagsNumber().getText().trim());

        if (excludeNumber < 0) {
            JOptionPane.showMessageDialog(this.panel, 
                    "number is not correct", "please enter valid integer", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tagsNumber < 0) {
            JOptionPane.showMessageDialog(this.panel, 
                    "number is not correct", "please enter valid integer", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tagsNumber > tagsMap.size()) {
            JOptionPane.showMessageDialog(this.panel, "number is greater than words in index",
                    "Too Much Input", JOptionPane.ERROR_MESSAGE);
            this.panel.getTagsNumber().setText((tagsMap.size() / 3) + "");
            return;
        }
        
        // create cloud
        Cloud cloud = new Cloud();
        cloud.setMaxWeight(50.0);
        cloud.setThreshold(excludeNumber); //show just tags with this number
        cloud.setMaxTagsToDisplay(tagsNumber);

        Set set = tagsMap.entrySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry) itr.next();

            String text = (String) me.getKey();
            int value = (Integer) me.getValue();

            Object[] data = new Object[]{text, value};
            JTableUtil.addRowToJTable(this.panel.getCloudTable(), data);
            Tag tag = new Tag(text, value);
            tag.setLink("Term: " + text + " Frequnecy: " + value);
            tag.setScore(value);

            cloud.addTag(tag);
        }

        List<Tag> tags = null;

        if (this.panel.getTagsDisplayMode().getSelectedIndex() == 0) {
            tags = cloud.tags(new Tag.NameComparatorAsc());
        } else if (this.panel.getTagsDisplayMode().getSelectedIndex() == 1) {
            tags = cloud.tags(new Tag.NameComparatorDesc());
        } else if (this.panel.getTagsDisplayMode().getSelectedIndex() == 2) {
            tags = cloud.tags(new Tag.ScoreComparatorAsc());
        } else if (this.panel.getTagsDisplayMode().getSelectedIndex() == 3) {
            tags = cloud.tags(new Tag.ScoreComparatorDesc());
        }
        
        this.panel.renderTags(tags);
    }
}
