/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.util;

import edu.coeia.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

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
    private Task task;
    
    public ZipUtil (final Task task) {
        this.task = task;
    }
    
    public void compress(final String srcFolder, final String dest) throws Exception {
        this.compressFolder(srcFolder, dest);
    }
    
    private void compressFolder (final String srcFolder, final String destZipFile) throws Exception {
        File srcFile = new File(srcFolder);
        
        if ( srcFile.isDirectory() ) {
            FileOutputStream dest = new FileOutputStream(destZipFile);
            ZipOutputStream out = new ZipOutputStream(dest);
            this.compresFolderContent("", srcFolder, out);
            out.close();
        }
    }
    
    private void compresFolderContent(final String path, final String srcFile, final ZipOutputStream out) throws Exception {
        File dir = new File(srcFile);
        String[] files = dir.list();
        
        if (files.length > 0) {
            for (String file : files) {
                if ( task.isCancelledTask() ) {
                    break;
                }
                
                String newPath = "";

                if (path.isEmpty()) {
                    newPath += dir.getName();
                } else {
                    newPath += path + File.separator + dir.getName();
                }

                String newFilePath = srcFile + File.separator + file;
                this.compressObject(newPath, newFilePath, out);
            }
        } else { // empty folder, zip it
            ZipEntry entry = new ZipEntry(path + File.separator + dir.getName() + File.separator );
            out.putNextEntry(entry);
            out.closeEntry(); 
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
        if ( task.isCancelledTask() )
            return;
        
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
    
    public void decompress(final String srcZip, final String destDir) throws Exception {
        this.decompressFolder(srcZip, destDir);
    }
    
    private void decompressFolder(final String srcZip, final String destDir) throws Exception {
        ZipInputStream zInputStream = new ZipInputStream(new FileInputStream(
                srcZip));
        ZipFile zf = new ZipFile(srcZip);
        
        ZipEntry zipEntry;
        while( (zipEntry = zInputStream.getNextEntry())  != null ) {
            if ( task.isCancelledTask() )
                break;
            
            if ( zipEntry.isDirectory() ) {
                File newFolder = new File(destDir + File.separator + zipEntry.getName());
                newFolder.mkdirs();
            }
            else {
                if ( zipEntry.getName().endsWith(File.separator) ) { // empty dir
                    File emptyDirectory = new File(destDir + File.separator + zipEntry.getName());
                    emptyDirectory.mkdirs();
                    continue;
                }
                
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
}
