package com.ipiecoles.audiotheque.controller;

import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/count"
    )
    public Long countArtist(){
        return artistRepository.count();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}", //URL
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Artist findArtistById(@PathVariable Long id){
        Optional<Artist> artist = artistRepository.findById(id);
        if(artist.isPresent()){
            return artist.get();
        }
        throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'existe pas !");
        /*Artist artist = artistRepository.findById(id);
        return artist;*/
    }


    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"
    )
    public Artist findArtistByName(
            @RequestParam String name
    ){

            Artist artist = artistRepository.findByName(name);
            if(artist != null){
                return artist;
            }
            else{
                throw new EntityNotFoundException("L'artiste de nom " + name + " n'a pas été trouvé !");
            }

        //throw new IllegalArgumentException("Seul les lettres sont autorisées !");
        //Matricule correct mais inexistant => 404 NOT FOUND


    }




}
