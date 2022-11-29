package pe.edu.upc.banking.transactions.query.projections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionViewRepository extends JpaRepository<TransactionView, String> {
}