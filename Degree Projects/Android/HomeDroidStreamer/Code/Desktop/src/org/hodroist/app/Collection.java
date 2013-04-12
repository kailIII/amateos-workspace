package org.hodroist.app;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;


public class Collection {

    //Get Tag (JaudioTagger object) from mp3File
    public Tag getTags(File mp3File){

        //Open mp3 file
        AudioFile f=null;
        try {
            f = AudioFileIO.read(mp3File);
        } catch (Exception ex) {
            System.out.println("Excepcion while getting tags from Mp3: "+ex.toString());
        }

        //Get tags
        Tag tags = f.getTag();
        return tags;
    }


    //Get byte array representing artwork from a file
    public byte[] getArtwork(Tag tags){
        byte[] imageInByte=null;

        try {
            //Get artwork using JaudioTagger library
            Artwork artwork = tags.getFirstArtwork();
            BufferedImage art = null;
            art = artwork.getImage();
            //Convert BufferedImage to byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(art, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            System.out.println("Error while getting artwork from Mp3: " + ex.toString());
        }

        return imageInByte;
    }


    //Scannig files on ./Music directory
    public ArrayList<Track> scanFiles (){

        File folder = new File("./Music");
        File[] listOfFiles = folder.listFiles();
        ArrayList tracks = new ArrayList();

        for (int i = 0; i < listOfFiles.length; i++) {

          //If the file is an mp3 file
          if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".mp3")) {

            //Get tags
            Tag fileTags = getTags(listOfFiles[i]);
            String artist = fileTags.getFirst(FieldKey.ARTIST);
            String title = fileTags.getFirst(FieldKey.TITLE);
            String album = fileTags.getFirst(FieldKey.ALBUM);
            String year = fileTags.getFirst(FieldKey.YEAR);
            String path = listOfFiles[i].getPath().split("/")[2];

            //Create Track object
            Track track = new Track(artist,title,album,year,getArtwork(fileTags),path);

            //Add track to ArrayList
            tracks.add(track);

          } else{
            System.out.println("Error: Got not Mp3 file.");
          }
        }

        return tracks;
    }// End scanFiles


}
