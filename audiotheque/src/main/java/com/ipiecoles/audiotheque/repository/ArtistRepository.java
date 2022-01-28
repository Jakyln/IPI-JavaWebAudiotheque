package com.ipiecoles.audiotheque.repository;

import com.ipiecoles.audiotheque.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    //Artist findById(int id);
    //@Query("select * from artist where id = " + unId);
    boolean existsByName(String name);

    @Query(value = "SELECT * FROM Artist a WHERE a.name LIKE :name% ", nativeQuery = true)
    List<Artist> findByName(@Param("name") String name); //On met dans le paramètre du nom raccourci dans la requète  ex :'aero%'

    //Page<Artist> findByNameIgnoreCase(String name, Pageable pageable);

}
    /*PreparedStatement pst = conn.prepareStatement("SELECT first_name FROM  person_profile WHERE first_name LIKE ?");
    pst.setString(1, StringUtils.defaultIfBlank(name, "") + "%");*/