package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, String> {

    @Query(value = "SELECT * FROM relationships r " +
            "WHERE r.created_by = :accountA AND r.receiver_id = :accountB " +
            "OR r.created_by = :accountB AND r.receiver_id = :accountA", nativeQuery = true)
    Optional<Relationship> findRelationshipByAccounts(
            @Param("accountA") String accountA,
            @Param("accountB") String accountB
    );

}
