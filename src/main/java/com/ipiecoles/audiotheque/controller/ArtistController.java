package com.ipiecoles.audiotheque.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.Console;
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

    @Autowired
    private AlbumRepository albumRepository;



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
            throw new EntityNotFoundException("L'artiste de nom " + name + " n'a pas ??t?? trouv?? !");
    }


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
        //sortDirection diff??rent de ASC ou DESC => 400 BAD REQUEST
        //Valeurs n??gatives pour page et size => 400 BAD REQUEST
        if(page < 0 || size <= 0){
            throw new IllegalArgumentException("La page et la taille doivent ??tre positifs !");
        }
        //sortProperty n'est pas un attribut d'Artiste => 400 BAD REQUEST
        List<String> properties = Arrays.asList("id", "name", "nom");
        if(!properties.contains(sortProperty)){
            throw new IllegalArgumentException("La propri??t?? de tri " + sortProperty + " est incorrecte !");
        }
        //contraindre size <= 50 => 400 BAD REQUEST
        if(size > 50){
            throw new IllegalArgumentException("La taille doit ??tre inf??rieure ou ??gale ?? 50 !");
        }
        //page et size coh??rents par rapport au nombre de lignes de la table => 400 BAD REQUEST
            Long nbArtists = artistRepository.count();
        if((long) size * page > nbArtists){
            throw new IllegalArgumentException("Le couple num??ro de page et taille de page est incorrect !");
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
        //Artiste existe d??j?? (id, nom existant) => 409 CONFLICT
        if(artist.getId() != null && artistRepository.existsById(artist.getId()) ||
                artistRepository.existsByName(artist.getName())){
            throw new EntityExistsException("Il existe d??j?? un artiste identique en base");
        }

        if(artist.getName().trim().length()>0){ //v??rifie si le user n'a pas mis que des espaces
            try {
                return artistRepository.save(artist);
            }
            catch(Exception e){
                throw new IllegalArgumentException("Probl??me lors de la sauvegarde de l'artiste");
            }
        }
        else{
            throw new IllegalArgumentException("Veuillez remplir le champ du nom de l'artiste");
        }


    }

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Artist updateArtistName(
            @PathVariable Long id,
            @RequestBody String infos // infos est en json {"name":"Azymuthtest4","id":26}

    ){


        Optional<Artist> artist = artistRepository.findById(id); //On recup l'id.

        String name="";

        Pattern p = Pattern.compile("\"([^\"]*)\""); //On r??cup??re toutes les valeurs entre guillemets avec regex
        Matcher m = p.matcher(infos);
        while (m.find()) {
            //dans la boucle ,m.group(1) renvoit "name", le nom rentr?? et "id", donc on filtre les autres
            if(m.group(1).equals("name") || m.group(1).equals("id") ){
                continue;
            }
            else{
                name = m.group(1);
                //System.out.println(name);

            }
        }

        artist.get().setName(name);   //met ?? jour le nom
        return artistRepository.save(artist.get());
        //return artist.getName(); //renvoie une erreur de 1er caract??re JSON, mais la ligne est bien mise ?? jour dans la bdd


    }



    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteArtist(
            @PathVariable Long id
    ){
        Optional<Artist> artist = artistRepository.findById(id);
        List<Album> albums = albumRepository.findAlbumByArtist(artist.get().getId());

        for (Album album : albums) {
            System.out.println("album : " +album.getTitle() );
            albumRepository.deleteAlbumFromArtist(artist.get().getId());
        }
        artistRepository.deleteArtistById(id);
    }





}
