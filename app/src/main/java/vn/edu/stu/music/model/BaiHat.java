package vn.edu.stu.music.model;

import java.io.Serializable;
import java.util.Arrays;

public class BaiHat implements Serializable {
    private int id_song;
    private String name;
    private String author;
    private Genres id_genres;
    private String time;
    private byte[] picture;

    public BaiHat(int id_song, String name, String author, Genres id_genres, String time, byte[] picture) {
        this.id_song = id_song;
        this.name = name;
        this.author = author;
        this.id_genres = id_genres;
        this.time = time;
        this.picture = picture;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Genres getId_genres() {
        return id_genres;
    }

    public void setId_genres(Genres id_genres) {
        this.id_genres = id_genres;
    }

    public int getId_song() {
        return id_song;
    }

    public void setId_song(int id_song) {
        this.id_song = id_song;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    @Override
//    public String toString() {
//        return "Mã: " + id_song + "\n" +
//                "Tên: " + name + "\n" +
//                "Tác giả: " + author + "\n" +
//                "Thể loại: " + id_genres + "\n" +
//                "Thời lượng: " + time + "\n" +
//                "Hình ảnh: " + Arrays.toString(picture)
//                ;
//    }
}
