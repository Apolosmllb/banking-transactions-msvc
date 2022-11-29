package pe.edu.upc.banking.transactions.command.domain;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import pe.edu.upc.banking.accounts.contracts.commands.*;
import pe.edu.upc.banking.accounts.contracts.events.*;
import pe.edu.upc.banking.transactions.contracts.commands.*;
import pe.edu.upc.banking.transactions.contracts.events.*;
import java.math.BigDecimal;
import java.time.Instant;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class MoneyTransfer {
	@AggregateIdentifier
    private String transactionId;
	private String fromAccountId;
	private String toAccountId;
	private BigDecimal amount;
	private TransactionStatus status;
    
    public MoneyTransfer() {
    }
	
	@CommandHandler
    public MoneyTransfer(CreateMoneyTransfer command) {
        apply(
        	new MoneyTransferCreated(
        		command.getTransactionId(), 
        		command.getFromAccountId(),
        		command.getToAccountId(),
        		command.getAmount(), Instant.now()
        	)
        );
    }
	
	@CommandHandler
    public void handle(MarkTransferAsCompleted command) {
        Instant now = Instant.now();
        apply(new MoneyTransferCompleted(command.getTransactionId(), now));
    }
	
	@CommandHandler
    public void handle(MarkTransferAsFailed command) {
        Instant now = Instant.now();
        apply(new MoneyTransferFailed(command.getTransactionId(), now));
    }

    @CommandHandler
    public void returnMoney(CreditFromAccount command) {
        Instant now = Instant.now();
        apply(new FromAccountCredited(command.getAccountId(), command.getTransactionId(), command.getAmount(), now));
    }
	
	@EventSourcingHandler
    protected void on(MoneyTransferCreated event) {
        this.transactionId = event.getTransactionId();
        this.fromAccountId = event.getFromAccountId();
        this.toAccountId = event.getToAccountId();
        this.amount = event.getAmount();
        this.status = TransactionStatus.CREATED;
    }
	
	@EventSourcingHandler
    public void on(MoneyTransferFailed event) {
        this.status = TransactionStatus.FAILED;
    }

    @EventSourcingHandler
    public void on(MoneyTransferCompleted event) {
        this.status = TransactionStatus.COMPLETED;
    }
}
