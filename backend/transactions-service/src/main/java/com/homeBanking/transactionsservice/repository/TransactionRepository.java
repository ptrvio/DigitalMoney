package com.homeBanking.transactionsservice.repository;

import com.homeBanking.transactionsservice.entities.Transaction;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {



    @Query(value = """
    SELECT * FROM transactions 
    WHERE (origin = :cvu OR destination = :cvu OR account_id = :accountId)
    ORDER BY dated DESC
    """, nativeQuery = true)
    List<Transaction> getLastFiveTransactions(@Param("cvu") String cvu, @Param("accountId") Long accountId, Pageable pageable);


    @Query(value = """
    SELECT * FROM transactions 
    WHERE (origin = :cvu OR destination = :cvu OR account_id = :accountId)
    ORDER BY dated DESC
    """, nativeQuery = true)
    List<Transaction> getAllTransactions(@Param("cvu") String cvu, @Param("accountId") Long accountId);


    @Query(value = "SELECT * FROM transactions WHERE account_id= ?1 AND id=?2", nativeQuery = true)
    Optional<Transaction> findTransaction(Long accountId, Long id);

}
