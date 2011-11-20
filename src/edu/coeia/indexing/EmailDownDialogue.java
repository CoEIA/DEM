/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * IndexingDialog.java
 *
 * Created on Sep 10, 2011, 9:01:08 AM
 */
package edu.coeia.indexing;

import edu.coeia.cases.Case;

import edu.coeia.onlinemail.OnlineEmailReader;
import edu.coeia.util.FilesPath;
import java.io.IOException;
import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.swing.JLabel;


/**
 *
 * @author wajdyessam
 */
public class EmailDownDialogue extends javax.swing.JDialog  
{
    private OnlineEmailReader reader;
    
    public EmailDownDialogue() throws NoSuchProviderException, MessagingException, IOException, SQLException
    {
     
        
    }
    public EmailDownDialogue(java.awt.Frame parent,boolean modal, Case aCase) throws SQLException, NoSuchProviderException, MessagingException, IOException, Exception
    {
        super(parent, modal);
        initComponents();
        
        String Username = aCase.GetEmailConfig().get(0).getUserName();
        String Password = aCase.GetEmailConfig().get(0).getPassword();
        
        reader = new OnlineEmailReader(this,Username,Password, 
                aCase.getIndexLocation()+"\\"+FilesPath.ATTACHMENTS, 
                aCase.getIndexLocation()+"\\"+FilesPath.EMAIL_DB);
    
        reader.execute();
         
    }
    
     public JLabel getFrom() {
        return this.from;
    }

    public JLabel getBCC() {
        return this.bcc;
    }
    public JLabel getCC() {
        return this.cc;
    }
   public JLabel getSentDate() {
        return this.sdate;
    }

    public JLabel getCreationDate() {
        return this.cdate;
    }

    public JLabel getAttachments() {
        return this.attachments;
    }
    public JLabel getSubject()
    {
       return this.subject; 
    }
    public JLabel getTo()
    {
        return this.to;
    }
        

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressEmail = new javax.swing.JPanel();
        progresLabelPanel = new javax.swing.JPanel();
        FromjLabel = new javax.swing.JLabel();
        SentDatejLabel = new javax.swing.JLabel();
        CreateDatejLabel = new javax.swing.JLabel();
        CCjLabel = new javax.swing.JLabel();
        BCCjLabel = new javax.swing.JLabel();
        AttachjLabel = new javax.swing.JLabel();
        TojLabel = new javax.swing.JLabel();
        SubjectjLabel = new javax.swing.JLabel();
        ProgBar = new javax.swing.JProgressBar();
        from = new javax.swing.JLabel();
        to = new javax.swing.JLabel();
        subject = new javax.swing.JLabel();
        cc = new javax.swing.JLabel();
        bcc = new javax.swing.JLabel();
        sdate = new javax.swing.JLabel();
        cdate = new javax.swing.JLabel();
        attachments = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Email Indexing Window");
        setResizable(false);

        progressEmail.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Email Indexing", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        progressEmail.setLayout(new java.awt.BorderLayout());

        FromjLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        FromjLabel.setText("From:");

        SentDatejLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SentDatejLabel.setText("Sent Date:");

        CreateDatejLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        CreateDatejLabel.setText("Creation Date:");

        CCjLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        CCjLabel.setText("CC:");

        BCCjLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        BCCjLabel.setText("BCC:");

        AttachjLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        AttachjLabel.setText("Attachment Files:");

        TojLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        TojLabel.setText("To:");

        SubjectjLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SubjectjLabel.setText("Subject:");

        javax.swing.GroupLayout progresLabelPanelLayout = new javax.swing.GroupLayout(progresLabelPanel);
        progresLabelPanel.setLayout(progresLabelPanelLayout);
        progresLabelPanelLayout.setHorizontalGroup(
            progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progresLabelPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progresLabelPanelLayout.createSequentialGroup()
                                .addComponent(SubjectjLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(subject, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE))
                            .addGroup(progresLabelPanelLayout.createSequentialGroup()
                                .addComponent(TojLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addComponent(FromjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(from, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(81, Short.MAX_VALUE))
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CCjLabel)
                            .addComponent(BCCjLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(progresLabelPanelLayout.createSequentialGroup()
                                .addComponent(bcc, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                                .addGap(74, 74, 74))
                            .addGroup(progresLabelPanelLayout.createSequentialGroup()
                                .addComponent(cc, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addComponent(SentDatejLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sdate, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                        .addGap(32, 32, 32))
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addComponent(CreateDatejLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cdate, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addComponent(AttachjLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(attachments, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addComponent(ProgBar, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
        );
        progresLabelPanelLayout.setVerticalGroup(
            progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progresLabelPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(FromjLabel)
                    .addComponent(from, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addComponent(TojLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)))
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(subject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SubjectjLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CCjLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BCCjLabel)
                    .addComponent(bcc, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SentDatejLabel)
                    .addComponent(sdate, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CreateDatejLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(progresLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(AttachjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(progresLabelPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(attachments, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(ProgBar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        progressEmail.add(progresLabelPanel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(progressEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(progressEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("Email Indexing");

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AttachjLabel;
    private javax.swing.JLabel BCCjLabel;
    private javax.swing.JLabel CCjLabel;
    private javax.swing.JLabel CreateDatejLabel;
    private javax.swing.JLabel FromjLabel;
    private javax.swing.JProgressBar ProgBar;
    private javax.swing.JLabel SentDatejLabel;
    private javax.swing.JLabel SubjectjLabel;
    private javax.swing.JLabel TojLabel;
    private javax.swing.JLabel attachments;
    private javax.swing.JLabel bcc;
    private javax.swing.JLabel cc;
    private javax.swing.JLabel cdate;
    private javax.swing.JLabel from;
    private javax.swing.JPanel progresLabelPanel;
    private javax.swing.JPanel progressEmail;
    private javax.swing.JLabel sdate;
    private javax.swing.JLabel subject;
    private javax.swing.JLabel to;
    // End of variables declaration//GEN-END:variables

   

        

     
}