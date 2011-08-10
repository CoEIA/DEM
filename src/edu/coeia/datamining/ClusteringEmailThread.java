/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.datamining;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.gui.component.InfiniteProgressPanel ;
import edu.coeia.utility.Tuple ;
import edu.coeia.utility.FilesPath ;
import edu.coeia.cases.Case ;
import edu.coeia.email.MessageHeader ;
import edu.coeia.email.EmailReader;

import java.io.File ;
import java.io.IOException ;

import java.util.ArrayList ;

import javax.swing.SwingWorker ;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel;

import com.pff.PSTFile ;
import com.pff.PSTMessage;
import com.pff.PSTException ;
import com.pff.PSTObject;

public class ClusteringEmailThread extends SwingWorker<Integer,Void>{

    private String keyword ;
    private InfiniteProgressPanel panel ;
    private JTree emailClusterTree ;
    private Case index ;
    private PSTFile pstFile ;
    private String path ;
    
    public ClusteringEmailThread (String key, InfiniteProgressPanel i, JTree emailClusterTree,
    Case index, PSTFile pstFile, String path) {
        
        this.keyword = key ;
        this.panel = i ;
        this.emailClusterTree = emailClusterTree ;
        this.index = index ;
        this.path = path ;
    }

    @Override
    protected Integer doInBackground()  {
        try {
            File indexLocation = new File (index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
            ArrayList<Tuple<String, ArrayList<String>>> result = ClusteringData.clustetringData(
                    indexLocation, keyword, "mailtitle", "mailcontent");

            System.out.println("result size: " + result.size());
            
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(new ClusterNode("Cluster Result: " + keyword));

            for (Tuple<String,ArrayList<String>> cluster: result) {
                ClusterNode name = new ClusterNode(cluster.getA());
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);

                for (String id: cluster.getB() ){
                    try {
                        System.out.println("Search: " + id);
                        PSTMessage msg = searchEmail(id);

                        if ( msg == null ) {
                            System.out.println("cannot find: " + id);
                            continue ;
                        }

                        DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(new ClusterNode(id,msg));
                        node.add(fileNode);
                    }
                    catch (NumberFormatException e){
                        System.out.println("excpetion: " + id);
                    }
                }

                root.add(node);
            }

            emailClusterTree.setModel(new DefaultTreeModel(root));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    private PSTMessage searchEmail (String keyword) {
        try {
            ArrayList<MessageHeader> map = EmailReader.getInstance(pstFile,path,null);

            int row = map.size();
            for (int i=0 ; i<row ; i++ ) {
                MessageHeader m = map.get(i);

                System.out.println("get: " + m.getID() + " sub:" + m.getSubject() +  " , " + keyword);
                
                
                if ( map.get(i).getSubject().equalsIgnoreCase(keyword) ) {
                    PSTMessage msg = getMessage(m.getID());

                    System.out.println("msg: " + msg.getSubject() + " , " + keyword);
                    return msg;
                }
            }

//            for(Map.Entry<String,Map<Integer,PSTMessage>> mMap: bigMap.entrySet() ){
//                Map<Integer,PSTMessage> aMap = mMap.getValue();
//                for(Map.Entry<Integer,PSTMessage> bMap: aMap.entrySet()){
//                    PSTMessage msg = bMap.getValue();
//                    if ( msg.getSubject().equalsIgnoreCase(keyword) )
//                        return msg;
//                }
//            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
        return null ;
    }

    private PSTMessage getMessage (long id)throws IOException, PSTException {
        return (PSTMessage) PSTObject.detectAndLoadPSTObject(pstFile, id);
    }

    @Override
    public void done() {
        panel.stop();
    }
}
