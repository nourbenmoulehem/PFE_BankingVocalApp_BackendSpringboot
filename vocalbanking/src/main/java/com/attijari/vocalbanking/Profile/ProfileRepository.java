package com.attijari.vocalbanking.Profile;

import com.attijari.vocalbanking.Profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByEmail(String email);

    @Query("SELECT p FROM Profile p WHERE p.client IS NULL")
    List<Profile> findProfilesWhereClientIsNull();
  
    @Query("SELECT p FROM Profile p WHERE p.client.clientId = ?1")
    Profile findByClientId(Long client_id);
}
