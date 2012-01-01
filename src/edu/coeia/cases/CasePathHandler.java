/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath;
import java.util.List;

/**
 * any file in the index consist of path fields point to evidence folder
 * to solve updating the index when evidence folder change problem
 * this path fields will be relative to configuration file
 * and the configuration file will point to the full evidence folder path
 * 
 * so when changing evidence path, then we will change the configuration file
 * without updating the index, or re-indexing it again.
 * 
 * also this class can detect if the evidence folder is not exists
 * and ask for the new one, then update the configuration file
 * 
 * @author wajdyessam
 */

import java.io.File ;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;

public class CasePathHandler {
    private List<PathEntry> entries;
    public static final String prefix = "@PATH_%d@";
    
    public CasePathHandler() {
        this.entries = new ArrayList<PathEntry>();
    }
    
    public void add(final File file ){
        String fileName = String.format(prefix, this.entries.size());
        PathEntry entry = new PathEntry(fileName, file.getAbsolutePath());
        this.entries.add(entry);
    }
    
    public void save(final String location) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(location), true));
        
        for(PathEntry entry: this.entries)
            writer.println(entry.name + " - " + entry.path);
        
        writer.close();
    }
    
    public void read(final String location) throws IOException {
        this.entries.clear();
        
        List<String> lines = FileUtil.getFileContentInArrayList(new File(location));
        
        for(String line: lines) {
            String name = line.split("-")[0].trim();
            String path = line.split("-")[1].trim();
            this.entries.add(new PathEntry(name, path));
        }
    }
    
    public List<PathEntry> getEntries() {
        return Collections.unmodifiableList(this.entries);
    }
    
    public void updateFullPath(final PathEntry entry) {
        
    }
    
    public String getFullPath(final String relativePath) {
       StringBuilder fullPath = new StringBuilder();
       
       for(int i=0; i<this.entries.size(); i++) {
           PathEntry entry = this.entries.get(i);
           
           if ( relativePath.startsWith(entry.name) ) {
               String filePath = relativePath.split(String.format(prefix + "\\\\", i))[1];
               fullPath.append(entry.path);
               fullPath.append(File.separator);
               fullPath.append(filePath);
               break;
           }
       }
       
       return fullPath.toString();
    }
    
    public String getRelativePath(final File file) {
        String fullPath = file.getAbsolutePath();
        StringBuilder relativePath = new StringBuilder();
        
        for(int i=0; i<this.entries.size(); i++) {
            PathEntry entry = this.entries.get(i);
            
            if ( fullPath.contains(entry.path)) {
                relativePath.append(String.format(prefix, i));
                relativePath.append(File.separator);
                relativePath.append(file.getName());
                break;
            }
        }
        
        return relativePath.toString();
    }
            
    public static class PathEntry {
        String name, path;
        
        public PathEntry(final String name, final String path) {
            this.name = name;
            this.path = path;
        }
    }
}
