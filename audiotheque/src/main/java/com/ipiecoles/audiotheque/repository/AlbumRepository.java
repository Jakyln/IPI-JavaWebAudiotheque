package com.ipiecoles.audiotheque.repository;

import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    boolean existsByTitle(String title);

    @Query(value = "INSERT INTO artist a (a.title,a.artist_id) VALUES (title,artist_id)", nativeQuery = true)
    Album addAlbumToArtist(String title,Long artist_id);

    @Query(value = "SELECT * FROM album WHERE artist_id = id;", nativeQuery = true)
    List<Album> findAlbumByArtist(Long id);

    @Modifying
    @Query(value = "DELETE FROM album WHERE artist_id = ?1 ;", nativeQuery = true)
    void deleteAlbumFromArtist(Long artiste_id);

}
