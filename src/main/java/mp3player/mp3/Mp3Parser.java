/*
    It allows for convenient processing of File objects loaded by the user into Mp3Song objects, which can be displayed in the table
 */
package mp3player.mp3;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mp3Parser {

    //is used to process a single File representing an MP3 file into an Mp3Song object
    public static Mp3Song createMp3Song(File file) throws IOException, TagException {
        MP3File mp3File = new MP3File(file);
        String absolutePath = file.getAbsolutePath();
        String title = mp3File.getID3v2Tag().getSongTitle();
        String author = mp3File.getID3v2Tag().getLeadArtist();
        String album = mp3File.getID3v2Tag().getAlbumTitle();
        return new Mp3Song(title, author, album, absolutePath);
    }

    //allows for loading all MP3 files from a given folder and transforming them into a list of Mp3Song objects
    public static List<Mp3Song> createMp3List(File dir) throws IOException, TagException {
        if(!dir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }
        List<Mp3Song> songList = new ArrayList<>();
        File[] files = dir.listFiles();
        for(File f: files) {
            String fileExtension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
            if(fileExtension.equals("mp3"))
                songList.add(createMp3Song(f));
        }

        return songList;
    }
}

