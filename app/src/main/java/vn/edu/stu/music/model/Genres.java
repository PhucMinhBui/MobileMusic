package vn.edu.stu.music.model;

import java.io.Serializable;

public class Genres implements Serializable {
    private int id_genres;
    private String name;

    public Genres() {
    }

    public Genres(String name) {
        this.name = name;
    }

    public Genres(int id_genres, String name) {
        this.id_genres = id_genres;
        this.name = name;
    }

    public int getId_genres() {
        return id_genres;
    }

    public void setId_genres(int id_genres) {
        this.id_genres = id_genres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id_genres + " - " + name;
    }
}
