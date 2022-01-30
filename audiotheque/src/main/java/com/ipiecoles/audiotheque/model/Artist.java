package com.ipiecoles.audiotheque.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name="artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="artistleid")
    private Integer id;
    //@Column(name="artistlename")
    private String name;

    @OneToMany()
    private Set<Album> albums = new HashSet<>();


    public Artist() {}

    public Artist(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist(Integer id, String name, HashSet<Album> albums) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Artist{" + "id=" + id + ", name=" + name + '}';
    }








}
