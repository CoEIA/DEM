/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zipper Utility
 * to compress and decompress zip files
 * 
 * @author wajdyessam
 */

public class ZipUtil {
    public ZipUtil () {
        
    }
    
    public void compress(final String srcFolder, final String dest) throws Exception {
        this.compressFolder(srcFolder, dest);
    }
    
    public void decompress(final String srcZip, final String destDir) throws Exception {
        this.decompressFolder(srcZip, destDir);
    }
    
    private void decompressFolder(final String srcZip, final String destDir) throws Exception {
        ZipInputStream zInputStream = new ZipInputStream(new FileInputStream(
                srcZip));
        ZipFile zf = new ZipFile(srcZip);
        
        ZipEntry zipEntry;
        while( (zipEntry = zInputStream.getNextEntry())  != null ) {
            if ( zipEntry.isDirectory() ) {
                File newFolder = new File(destDir + File.separator + zipEntry.getName());
                newFolder.mkdir();
            }
            else {
                String newPath = destDir + File.separator + zipEntry.getName();
                File newFile = new File(newPath);
                
                if (!newFile.getParentFile().exists() ) {
                    newFile.getParentFile().mkdirs();
                }
                
                InputStream is = zf.getInputStream(zipEntry);
                FileUtil.saveObject(is, newPath);
            }
            
            zInputStream.closeEntry();
        }

        zInputStream.close();
    }
    
    private void compressFolder (final String srcFolder, final String destZipFile) throws Exception {
        File srcFile = new File(srcFolder);
        
        if ( srcFile.isDirectory() ) {
            FileOutputStream dest = new FileOutputStream(destZipFile);
            ZipOutputStream out = new ZipOutputStream(dest);
            this.compresFolderContent("DEM", srcFolder, out);
            out.close();
        }
    }
    
    private void compresFolderContent(final String path, final String srcFile, final ZipOutputStream out) throws Exception {
        File dir = new File(srcFile);
        String[] files = dir.list();
        
        for(String file: files) {
            String newPath = path + File.separator + dir.getName();
            String newFilePath = srcFile + File.separator + file;
            this.compressObject(newPath, newFilePath, out);
        }
    }
        
    private void compressObject(final String path, final String srcFile, final ZipOutputStream out) throws Exception {
        File file = new File(srcFile);
        
        if ( file.isDirectory() ) {
            this.compresFolderContent(path, srcFile, out);
        }
        else {
            this.compressFile(path, srcFile, out);
        }
    }
    
    private void compressFile(final String path, final String srcFile, final ZipOutputStream out) throws Exception {
        File file = new File(srcFile);
        if ( file.isDirectory() ) return;
        
        final int BUFFER = 2048;
        byte data[] = new byte[BUFFER];
        
        FileInputStream fileInputStream = new FileInputStream(srcFile);
        ZipEntry entry = new ZipEntry(path + File.separator + file.getName());
        out.putNextEntry(entry);

        int count;
        while ( (count = fileInputStream.read(data)) > 0 ) 
            out.write(data, 0, count);

        fileInputStream.close();
        out.closeEntry(); 
    }
}
