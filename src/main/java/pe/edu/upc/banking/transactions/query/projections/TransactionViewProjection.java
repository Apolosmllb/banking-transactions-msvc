package pe.edu.upc.banking.transactions.query.projections;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import pe.edu.upc.banking.accounts.contracts.events.*;
import pe.edu.upc.banking.transactions.command.domain.TransactionStatus;
import pe.edu.upc.banking.transactions.command.domain.TransactionType;
import pe.edu.upc.banking.transactions.contracts.events.MoneyTransferCompleted;
import pe.edu.upc.banking.transactions.contracts.events.MoneyTransferCreated;
import pe.edu.upc.banking.transactions.contracts.events.MoneyTransferFailed;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TransactionViewProjection {
	private final TransactionViewRepository transactionViewRepository;
	
	public TransactionViewProjection(TransactionViewRepository transactionViewRepository) {
        this.transactionViewRepository = transactionViewRepository;
    }
	
	@EventHandler
    public void on(AccountCredited event) {
		String transactionId = event.getTransactionId();
		String accountId = event.getAccountId();
		BigDecimal amount = event.getAmount();
	    String transactionType = TransactionType.DEPOSIT.toString();
		TransactionView transactionView = new TransactionView(transactionId, accountId, null, amount, transactionType, TransactionStatus.COMPLETED.toString(), event.getOccurredOn());
		transactionViewRepository.save(transactionView);
    }
	
	@EventHandler
    public void on(AccountDebited event) {
		String transactionId = event.getTransactionId();
		String accountId = event.getAccountId();
		BigDecimal amount = event.getAmount();
	    String transactionType = TransactionType.WITHDRAW.toString();
	    TransactionView transactionView = new TransactionView(transactionId, accountId, null, amount, transactionType, TransactionStatus.COMPLETED.toString(), event.getOccurredOn());
		transactionViewRepository.save(transactionView);
    }

	@EventHandler
	public void on(MoneyTransferCreated event) {
		String transactionId = event.getTransactionId();
		String fromAccountId = event.getFromAccountId();
		String toAccountId = event.getToAccountId();
		BigDecimal amount = event.getAmount();
		String transactionType = TransactionType.TRANSFER.toString();
		String transactionStatus = TransactionStatus.CREATED.toString();
		TransactionView transactionView = new TransactionView(transactionId, fromAccountId, toAccountId, amount, transactionType, transactionStatus, event.getOccurredOn());
		transactionViewRepository.save(transactionView);
	}

	@EventHandler
	public void on(FromAccountDebited event) {
		Optional<TransactionView> transactionViewOptional = transactionViewRepository.findById(event.getTransactionId());
		if (transactionViewOptional.isPresent()) {
			TransactionView transactionView = transactionViewOptional.get();
			String transactionStatus = TransactionStatus.IN_PROGRESS.toString();
			transactionView.setTransactionStatus(transactionStatus);
			transactionView.setCreatedAt(event.getOccurredOn());
			transactionViewRepository.save(transactionView);
		}
	}

	@EventHandler
	public void on(MoneyTransferFailed event) {
		Optional<TransactionView> transactionViewOptional = transactionViewRepository.findById(event.getTransactionId());
		if (transactionViewOptional.isPresent()) {
			TransactionView transactionView = transactionViewOptional.get();
			String transactionStatus = TransactionStatus.FAILED.toString();
			transactionView.setTransactionStatus(transactionStatus);
			transactionView.setCreatedAt(event.getOccurredOn());
			transactionViewRepository.save(transactionView);
		}
	}

	@EventHandler
	public void on(MoneyTransferCompleted event) {
		Optional<TransactionView> transactionViewOptional = transactionViewRepository.findById(event.getTransactionId());
		if (transactionViewOptional.isPresent()) {
			TransactionView transactionView = transactionViewOptional.get();
			String transactionStatus = TransactionStatus.COMPLETED.toString();
			transactionView.setTransactionStatus(transactionStatus);
			transactionView.setCreatedAt(event.getOccurredOn());
			transactionViewRepository.save(transactionView);
		}
	}
}