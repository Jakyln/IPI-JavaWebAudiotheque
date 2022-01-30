package com.ipiecoles.audiotheque.controller;

import com.google.gson.*;
import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/albums")
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
    )*/

    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void createAlbum(
            //@RequestBody Map<String,String> infos
            /*HttpServletRequest request,
            HttpServletResponse response, Artist artist*/
            //@RequestParam("title") String title
            //@RequestParam Artist artist
            //@PathVariable Long artistId
            @RequestBody @NotNull String infos

            ){
        Artist artist = new Artist();



        /*Album album = albumRepository.addAlbumToArtist(title,artistId);
        return album;*/

        //String title = request.getParameter("title");

        /*for (Map info : infos) {
            System.out.println("clé: "+mapentry.getKey()
                    + " | valeur: " + mapentry.getValue());
        }*/

        String artistName="";
        String albumTitle="";
        String[]  arrayTitleAndArtistName = new String[2];
        String test2="";
        int i = 0;


        Map<String,String> test ;

        Pattern findQuotes = Pattern.compile("\"([^\"]*)\""); //On récupère toutes les valeurs entre guillemets avec regex
        Pattern findId = Pattern.compile("([1-9]|[1-9][0-9]|[1-9][0-9][0-9])"); //
        Matcher m = findQuotes.matcher(infos);
        Matcher matchId = findId.matcher(infos);
        while (m.find()) {
            //dans la boucle ,m.group(1) renvoit "name", le nom rentré et "id", donc on filtre les autres
            if(m.group(1).equals("title") || m.group(1).equals("id")  || m.group(1).equals("artist")  || m.group(1).equals("name") ){
                continue;

            }
            else{
                /*if(m.group(1).equals("artist")){
                    test2 = m.group(1).toString();
                    }*/

                arrayTitleAndArtistName[i] = (m.group(1));
                i++;
                System.out.println("les artistName : " + m.group(1));



            }

            //System.out.println(m.group(1));
        }

        albumTitle = arrayTitleAndArtistName[0];
        artistName = arrayTitleAndArtistName[1];

        artist = artistRepository.findByNameOnly(artistName);


        /*while (matchId.find()) {
            //dans la boucle ,m.group(1) renvoit "name", le nom rentré et "id", donc on filtre les autres


            if(m.group(1) instanceof String){
                continue;
            }
            else{
                System.out.println("les nombres : " + m.group(1));
            }
        }*/
        //System.out.println(artistName);


        //{"title":"gdgr","artist":{"id":154,"name":"Whitesnak"},"id":null}

    //System.out.println("title : " + titleu);


        //artist = infos.get("Artist");

        //System.out.println("test request :" + object);
        Album album = new Album();
        /*album.setTitle(title);
        album.setArtist(artist);*/
         albumRepository.addAlbumToArtist(albumTitle, artist.getId());
         //albumRepository.save(album);


        //return albumRepository.save(album);

        /*GsonBuilder gsonBuilder = new GsonBuilder();
        Artist artist2 = gsonBuilder.create().fromJson(infos, Artist.class);
        System.out.println("Json1" + artist2.getName() );*/

        //JsonElement elem = null;
        //JsonObject jsonObject = JsonParser.parseString(infos).getAsJsonObject();
        //JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        //String titre = jsonObject.getAsJsonPrimitive("title").getAsString();
        //String artiste = jsonObject.getAsJsonObject("artist").get("id").getAsString();
        //System.out.println("Json1" + titre + "Json2" + artiste);
        //System.out.println("Json1" + titre );

        /*JsonArray arr = jsonObject.getAsJsonArray("artist");
        for (int i = 0; i < arr.size(); i++) {
            String post_id = arr.get(i).getAsJsonObject().get("id").getAsString();
            System.out.println(post_id);
        }*/


        /*JsonElement elem = null;
        elem = jsonObject.get("r$contentRatings");

        if(elem != null) {
            JsonArray contentRatingsList = elem.getAsJsonArray();
            if(contentRatingsList != null) {
                for(int i=0; i< contentRatingsList.size(); i++) {
                    JsonObject scheme =contentRatingsList.get(i).getAsJsonObject().getAsJsonObject("r$scheme");
                    JsonObject rating =contentRatingsList.get(i).getAsJsonObject().getAsJsonObject("r$rating");
                    JsonArray subRatings = contentRatingsList.get(i).getAsJsonObject().getAsJsonObject("r$subRatings").getAsJsonArray()*/

    }



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



