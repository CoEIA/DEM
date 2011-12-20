/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hashanalysis;

import edu.coeia.util.FileUtil;
import edu.coeia.util.GUIFileFilter;
import edu.coeia.util.FilesPath;

import java.io.File;
import java.io.FileFilter;

import java.util.List ;
import java.util.ArrayList ;

/**
 * Hash Library Manager is collection of static methods
 * that work on specific task related to the hash library folder
 * 
 * @author wajdyessam
 */
final class HashLibraryManager {
   
    /**
     * adding the hash category if its new to hash library 
     * 
     * @param hashCategory the hash category to be written
     * @return false when there it existing ( same file name exist in the hash library)
     * @throws Exception 
     */
    public static boolean addHashCategory(final HashCategory hashCategory) 
    throws Exception {
        
        if (isHashLibraryContain(hashCategory) ) {
            return false;
        }
        
        writeHashCategory(hashCategory);
        return true;
    }
    
    /**
     * Remove the hash category from the hash library folder
     * @param hashCategory the hash category to be deleting
     * @return true if deleting done, false otherwise
     */
    public static boolean removeHashCategory(final HashCategory hashCategory) {
        File file = new File(getPathForHashCategory(hashCategory.getName()));
        
        boolean status = file.delete();
        return status;
    }
    
    /**
     * check if the hash library folder contain this hash category
     * @param hashCategory the hash category under test
     * @return true if existing, false otherwise
     */
    public static boolean isHashLibraryContain(final HashCategory hashCategory) {
        File file = new File(getPathForHashCategory(hashCategory.getName()));
        return file.exists();
    }
    
    /**
     * update the current hash category with new hash items collections
     * @param items the collections if items to update the file
     * @param categoryName the name of the target hash category
     * @throws Exception 
     */
    public static void update(final List<HashItem> items, final String categoryName)
    throws Exception {
        
        File file = new File(getPathForHashCategory(categoryName));
        HashCategory hashCategory = readHashCategory(file);
        
        for(HashItem item: items) {
            if ( ! hashCategory.isContain(item) ) {
                hashCategory.addItem(item);
            }
        }

        writeHashCategory(hashCategory);
    }
    
    /**
     * get the hash categories under hash library folder
     * the folder name is predefined 
     * @return the list of file in this folder 
     */
    public static List<HashCategory> getHashCategories() throws Exception {
        String hashLocation = FilesPath.HASH_LIBRARY_PATH ;
        
        List<File> files = FileUtil.getFilesInDirectory(hashLocation, hashExtensionFilter);
        List<HashCategory> categories = new ArrayList<HashCategory>();
        
        for(File file: files) {
            HashCategory hashCategory = HashLibraryManager.readHashCategory(file);
            categories.add(hashCategory);
        }
        
        return categories;
    }
    
    /**
     * get the path for this hash category in the hash library 
     * @param categoryName
     * @return full path of hash category location
     */
    public static String getPathForHashCategory(final String categoryName) {
        return FilesPath.HASH_LIBRARY_PATH + "\\" + categoryName + 
               FilesPath.HASH_SET_EXTENSION;
    }
    
    /**
     * write hash category into file 
     * @param hashCategory
     * @throws Exception 
     */
    private static void writeHashCategory(HashCategory hashCategory) throws Exception { 
       File file = new File(getPathForHashCategory(hashCategory.getName()));
       FileUtil.writeObject(hashCategory,file);
    }
    
    /**
     * read the file as hash category object
     * @param file the file path to be read
     * @return HashCategory for that file
     * @throws Exception 
     */
    public static HashCategory readHashCategory(final File file) throws Exception {
        HashCategory hashCategory = FileUtil.readObject(file);
        return hashCategory;
    }
    
    /**
     * this is filter to accept any files with extension .HASH_SET
     */
    private final static FileFilter hashExtensionFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.isFile() && file.getAbsolutePath().endsWith(FilesPath.HASH_SET_EXTENSION);
        }
    };
    
    /**
     * this is swing filter to accept files with extension .HASH_SET
     */
    public final static GUIFileFilter SWING_HASH_EXTENSION_FILTER = new GUIFileFilter("DEM HASH SET", 
            FilesPath.HASH_SET_EXTENSION);
}
