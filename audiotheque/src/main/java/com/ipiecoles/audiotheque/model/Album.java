package com.ipiecoles.audiotheque.model;

import org.springframework.data.jpa.repository.query.PartTreeJpaQuery;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Album {
    @Id
    private long id;
    private String title;

    @ManyToOne
    private Artist artist;
    //private int artist_id = artist.getId();

    public Album() {}

    public Album(long id, String title) {
        this.id = id;
        this.title = title;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }



}
