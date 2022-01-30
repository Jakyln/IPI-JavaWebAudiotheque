package com.ipiecoles.audiotheque.controller;

import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/*@RestController
@RequestMapping("/albums")*/
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

//afficher infos
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}", //URL
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Album findAlbumById(@PathVariable Long id){
        Optional<Album> album = albumRepository.findById(id);
        if(album.isPresent()){
            return album.get();
        }
        throw new EntityNotFoundException("L'album d'identifiant " + id + " n'existe pas !");
    }


    /*@RequestMapping(
            method = RequestMethod.POST,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Album createAlbum(
            @RequestBody String title,
            @RequestBody @NotNull Artist artist

            ){
        Album album = albumRepository.addAlbumToArtist(title,artist.getId());
        return album;
        //return albumRepository.save(album);


    }*/



    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(
            @PathVariable Long id
    ){
        albumRepository.deleteById(id);
    }
}



