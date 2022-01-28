package com.ipiecoles.audiotheque.controller;

import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;

/*@RestController
@RequestMapping("/albums")*/
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    /*
    @RequestMapping(
            method = RequestMethod.POST,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Album createAlbum(
            @RequestBody Album album
    ){
        //Artiste existe déjà (id, nom existant) => 409 CONFLICT
        if(album.getId() != null && albumRepository.existsById(album.getId()) ||
                albumRepository.existsByName(album.getTitle())){
            throw new EntityExistsException("Il existe déjà un artiste identique en base");
        }
        //        //valeurs incompatibles avec le type de l'attribut => 400 BAD REQUEST
        //        //valeurs incorrectes (fonctionnel) => 400 BAD REQUEST
        //Hibernate validator, mettre en place une méthode de validation manuelle
        //excède les limites de la base (ex : nom > 50 caractères) => 400 BAD REQUEST
        try {
            return albumRepository.save(album);
        }
        catch(Exception e){
            throw new IllegalArgumentException("Problème lors de la sauvegarde de l'artiste");
        }
    }*/
}



