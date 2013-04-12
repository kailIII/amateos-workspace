/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hodroist.app;


import java.io.Serializable;

/**
 *
 * @author albertomateos
 */

//Represents and store info about an mp3 file
public class Track implements Serializable{

    public String artist;
    public String title;
    public String album;
    public String year;
    public byte[] artwork;
    public String filename;


    Track(String _artist, String _title, String _album, String _year, byte[] _artwork, String _filename){
        this.artist = _artist;
        this.title = _title;
        this.album = _album;
        this.year = _year;
        this.artwork = _artwork;
        this.filename = _filename;
    }

}
