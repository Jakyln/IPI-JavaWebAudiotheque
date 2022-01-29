package com.ipiecoles.audiotheque.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;



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


    /*@RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"
    )
    public Artist<Artist> findArtistByName(
            @RequestParam String name
    ){

        //String nameShort = name.substring(0,4);

        List<Artist> artists = artistRepository.findByName(name);
            if(artists != null){
                return artists;
            }
            throw new EntityNotFoundException("L'artiste de nom " + name + " n'a pas été trouvé !");
    }*/

    //throw new IllegalArgumentException("Seul les lettres sont autorisées !");
    //Nom correct mais inexistant => 404 NOT FOUND


 //liste d'artistes avec noms
    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"
    )
    public List<Artist> findArtistByName(
            @RequestParam String name
    ){

        //String nameShort = name.substring(0,4);

        List<Artist> artists = artistRepository.findByName(name);
            if(artists != null){
                return artists;
            }
            throw new EntityNotFoundException("L'artiste de nom " + name + " n'a pas été trouvé !");
    }

        //throw new IllegalArgumentException("Seul les lettres sont autorisées !");
        //Nom correct mais inexistant => 404 NOT FOUND
        //
/* Methode pour page d'artistes avec noms commanceant par variable
    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"
    )
    public Page<Artist> findArtistByName(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam String name

    ){
        //Page ou size ne sont pas des entiers => 400 BAD REQUEST
        //sortDirection différent de ASC ou DESC => 400 BAD REQUEST
        //Valeurs négatives pour page et size => 400 BAD REQUEST
        if(page < 0 || size <= 0){
            throw new IllegalArgumentException("La page et la taille doivent être positifs !");
        }
        //sortProperty n'est pas un attribut d'Artiste => 400 BAD REQUEST
        List<String> properties = Arrays.asList("id", "name", "nom");
        if(!properties.contains(sortProperty)){
            throw new IllegalArgumentException("La propriété de tri " + sortProperty + " est incorrecte !");
        }
        //contraindre size <= 50 => 400 BAD REQUEST
        if(size > 50){
            throw new IllegalArgumentException("La taille doit être inférieure ou égale à 50 !");
        }
        //page et size cohérents par rapport au nombre de lignes de la table => 400 BAD REQUEST
        Long nbArtists = artistRepository.count();
        if((long) size * page > nbArtists){
            throw new IllegalArgumentException("Le couple numéro de page et taille de page est incorrect !");
        }


        return artistRepository.findByNameIgnoreCase(name,PageRequest.of(page, size, sortDirection, sortProperty));

        //throw new EntityNotFoundException("L'artiste de nom " + name + " n'a pas été trouvé !");

    }*/



    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Page<Artist> listArtist(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ){
        //Page ou size ne sont pas des entiers => 400 BAD REQUEST
        //sortDirection différent de ASC ou DESC => 400 BAD REQUEST
        //Valeurs négatives pour page et size => 400 BAD REQUEST
        if(page < 0 || size <= 0){
            throw new IllegalArgumentException("La page et la taille doivent être positifs !");
        }
        //sortProperty n'est pas un attribut d'Artiste => 400 BAD REQUEST
        List<String> properties = Arrays.asList("id", "name", "nom");
        if(!properties.contains(sortProperty)){
            throw new IllegalArgumentException("La propriété de tri " + sortProperty + " est incorrecte !");
        }
        //contraindre size <= 50 => 400 BAD REQUEST
        if(size > 50){
            throw new IllegalArgumentException("La taille doit être inférieure ou égale à 50 !");
        }
        //page et size cohérents par rapport au nombre de lignes de la table => 400 BAD REQUEST
            Long nbArtists = artistRepository.count();
        if((long) size * page > nbArtists){
            throw new IllegalArgumentException("Le couple numéro de page et taille de page est incorrect !");
        }
        return artistRepository.findAll(PageRequest.of(page, size, sortDirection, sortProperty));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Artist createArtist(
            @RequestBody Artist artist
    ){
        //Artiste existe déjà (id, nom existant) => 409 CONFLICT
        if(artist.getId() != null && artistRepository.existsById(artist.getId()) ||
                artistRepository.existsByName(artist.getName())){
            throw new EntityExistsException("Il existe déjà un artiste identique en base");
        }
        //        //valeurs incompatibles avec le type de l'attribut => 400 BAD REQUEST
        //        //valeurs incorrectes (fonctionnel) => 400 BAD REQUEST
        //Hibernate validator, mettre en place une méthode de validation manuelle
        //excède les limites de la base (ex : nom > 50 caractères) => 400 BAD REQUEST
        try {
            return artistRepository.save(artist);
        }
        catch(Exception e){
            throw new IllegalArgumentException("Problème lors de la sauvegarde de l'artiste");
        }
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public String updateArtistName(
            @PathVariable Long id,
            @RequestBody String infos // infos est en json {"name":"Azymuthtest4","id":26}

    ){


        Artist artist = artistRepository.findArtistById(id); //On recup l'id.

        String name="";

        Pattern p = Pattern.compile("\"([^\"]*)\""); //On récupère toutes les valeurs entre guillemets avec regex
        Matcher m = p.matcher(infos);
        while (m.find()) {
            //dans la boucle ,m.group(1) renvoit "name", le nom rentré et "id", donc on filtre les autres
            if(m.group(1).equals("name") || m.group(1).equals("id") ){
                continue;
            }
            else{
                name = m.group(1);
                //System.out.println(name);

            }
        }

        artist.setName(name);   //met à jour le nom
        artist = artistRepository.save(artist);
        return artist.getName(); //renvoie une erreur de 1er caractère JSON, mais la ligne est bien mise à jour dans la bdd


    }


/*  marche pas : Request method 'DELETE' not supported

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}/delete"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(
            @PathVariable Long id
    ){
        artistRepository.deleteById(id);
    }*/

}
