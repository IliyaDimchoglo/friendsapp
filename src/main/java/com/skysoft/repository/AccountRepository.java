package com.skysoft.repository;

import com.skysoft.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findFirstByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    @Query(value = "select * from account_entity a where not exists " +
            "(select f.id from friend_entity f where (f.account_1_id = :account_id and f.account_2_id = a.id)" +
            "or (f.account_2_id = :account_id and f.account_1_id = a.id))" +
            "and a.id != :account_id", nativeQuery = true)
    List<AccountEntity> findAllNotFriendsByAccountId(@Param("account_id") String accountId);
}
