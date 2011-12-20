/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.filesignature;

/**
 *
 * @author Ahmed
 */
public class FileSignature {

    private String Id;
    private String Signature;
    private String[] Extension;
    private String Type;
    public String KnownSignature;

    public FileSignature() {
    }

    public FileSignature(String Id, String Signature, String[] Extension, String Type) {

        this.Id = Id;
        this.Signature = Signature;
        this.Extension = Extension;
        this.Type = Type;
    }

    public String getID() {
        return this.Id;
    }

    public String getSignature() {
        return this.Signature;
    }

    public String[] getExtension() {
        return this.Extension;
    }

    public String getType() {
        return this.Type;
    }

    public String toString() {

        return Id + "\n" + Signature + "\n" + Extension + "\n" + Type + "\n";
    }
}
