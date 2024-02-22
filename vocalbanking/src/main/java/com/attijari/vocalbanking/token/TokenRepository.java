package com.attijari.vocalbanking.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>  {
    //@Query(value = """
      //select t from Token t inner join Profile u\s
     // on t.profile.id = u.id\s
    //  where u.id = :id and (t.expired = false or t.revoked = false)\s
      //""")
    @Query("""
    SELECT t FROM Token t 
    INNER JOIN t.profile u
    WHERE u.id = :id AND (t.expired = false OR t.revoked = false)
""")
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
