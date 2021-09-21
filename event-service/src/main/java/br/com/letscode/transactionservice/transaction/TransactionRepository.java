package br.com.letscode.transactionservice.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
