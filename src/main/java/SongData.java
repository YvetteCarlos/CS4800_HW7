import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Song{
    private String title;
    private String artist;
    private String album;
    private int duration;

    private int songID;

    //constructor
     public Song(String title, String artist, String album,  int duration, int songID){
         this.title = title;
         this.artist = artist;
         this.album = album;
         this.duration = duration;
         this.songID = songID;
     }

     //setters & getters


     public String getTitle() {
         return title;
     }

     public void setTitle(String title) {
         this.title = title;
     }

     public String getArtist() {
         return artist;
     }

     public void setArtist(String artist) {
         this.artist = artist;
     }

     public String getAlbum() {
         return album;
     }

     public void setAlbum(String album) {
         this.album = album;
     }

     public int getDuration() {
         return duration;
     }

     public void setDuration(int duration) {
         this.duration = duration;
     }

     public int getSongID() {
         return songID;
     }

     public void setSongID(int songID) {
         this.songID = songID;
     }
 }


public class SongData implements SongService { //REAL OBJECT

    private final SongService info;  // instance of the interface
    private final Map<Integer, Song> songMap; //map with all the songs and integer =songID

    public SongData(SongService info) {
        this.info = info;
        this.songMap = new HashMap<>();

    }


    @Override
    public Song searchById(Integer songID) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        if (songMap.containsKey(songID)) {
            return songMap.get(songID);
        } else {
            Song song = info.searchById(songID);
            songMap.put(songID, song);
            return song;
        }
    }

    @Override
    public List<Song> searchByTitle(String title) { //we need to get the song by the title , if the song is a tile get it
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        List<Song> songList = new ArrayList<>();
        for(Song song: songMap.values()){ //go through all songs in songList
            if(song.getTitle().equals(title)){ //from song object of Song class return the title and check if it equals title
                songList.add(song); //add song object to songList
            }
        }
        return songList; //return songList
    }

    @Override
    public List<Song> searchByAlbum(String album) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        List<Song> songList = new ArrayList<>();
        for(Song song: songMap.values()){ //go through all songs in songList
            if(song.getAlbum().equals(album)){ //from song object of Song class return the title and check if it equals title
                songList.add(song); //add song object to songList
            }
        }
        return songList;
    }

    public class Proxy implements SongService{
        private final SongData realSongData; //reference to the SongData object

        private final Map<Integer, Song> cacheSongID;
        private final Map<String, List<Song>> cacheTitle;
        private final Map<String, List<Song>> cacheAlbum;

        public Proxy(Map<Integer, Song> cacheSongID, Map<String, List<Song>> cacheTitle, Map<String, List<Song>> cacheAlbum, SongData realSongData) {
            this.cacheSongID = cacheSongID;
            this.cacheTitle = cacheTitle;
            this.cacheAlbum = cacheAlbum;
            this.realSongData = realSongData;
        }


        @Override
        public Song searchById(Integer songID) {
            if(cacheSongID.containsKey(songID)){
                return cacheSongID.get(songID); //gives us the songID from the cache
        }else{
            Song song = realSongData.searchById(songID);
            cacheSongID.put(songID, song);
            return song; //add to the cache, if not in the cache
          }
        }

        @Override
        public List<Song> searchByTitle(String title) {
            if(cacheTitle.containsKey(title)){
                return cacheTitle.get(title);
            }else{
               List<Song> song2 = realSongData.searchByTitle(title);
                cacheTitle.put(title, song2);
                return song2;
            }
        }

        @Override
        public List<Song> searchByAlbum(String album) {
            if(cacheAlbum.containsKey(album)){
                return cacheTitle.get(album);
            }else{
                List<Song> song3 = realSongData.searchByAlbum(album);
                cacheTitle.put(album, song3);
                return song3;
            }
        }
    }

    public static class Client{
        public static void main(String[] args) {
            Song song1 = new Song("Title 1", "Artist 1", "Album 1", 309, 1);
            Song song2 = new Song("Title 2", "Artist 2", "Album 2", 236, 2);
            Song song3 = new Song("Title 3", "Artist 3", "Album 3", 232, 3);
            Song song4 = new Song("Title 4", "Artist 4", "Album 4", 248, 4);
            Song song5 = new Song("Title 5", "Artist 5", "Album 5", 325, 5);

            Song[] array = {song1, song2, song3, song4, song5};

            for(Song song : array) {
                System.out.println("Title: " + song.getTitle() + "\nArtist: " + song.getArtist() + "\nAlbum: " + song.getAlbum() + "\nDuration: " + song.getDuration());
            }
            }

        }
    }

