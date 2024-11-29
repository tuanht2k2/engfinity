package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Account;
import com.kma.engfinity.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Account> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM accounts_roles ar " +
            "JOIN roles r ON ar.role_id = r.name " +
            "WHERE ar.account_id = :accountId AND r.name = :roleName", nativeQuery = true)
    boolean hasRole(@Param("accountId") String accountId, @Param("roleName") ERole roleName);

    @Query(value = "SELECT *" +
            "FROM accounts " +
            "WHERE (display_name LIKE CONCAT('%', :keyword, '%') " +
            "   OR full_name LIKE CONCAT('%', :keyword, '%') " +
            "   OR email LIKE CONCAT('%', :keyword, '%') " +
            "   OR address LIKE CONCAT('%', :keyword, '%') " +
            "   OR phone_number LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY :sortBy :sortDir " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Account> search (
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword);

    @Query(value = "SELECT * FROM accounts WHERE phone_number IN :phoneNumbers", nativeQuery = true)
    List<Account> searchByPhoneNumbers(@Param("phoneNumbers") List<String> phoneNumbers);

}
