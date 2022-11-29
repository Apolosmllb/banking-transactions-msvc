package pe.edu.upc.banking.transactions.command.sagas;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import pe.edu.upc.banking.accounts.contracts.commands.*;
import pe.edu.upc.banking.accounts.contracts.events.*;
import pe.edu.upc.banking.transactions.contracts.commands.*;
import pe.edu.upc.banking.transactions.contracts.events.*;
import javax.inject.Inject;
import java.math.BigDecimal;

@Saga
public class MoneyTransferSaga {
	private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    
	@Inject
    private transient CommandGateway commandGateway;
	
	@StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyTransferCreated event) {
		this.fromAccountId = event.getFromAccountId();
        this.toAccountId = event.getToAccountId();
        this.amount = event.getAmount();
        DebitFromAccount debitFromAccount = new DebitFromAccount(
        		event.getFromAccountId(),
                event.getTransactionId(),
                event.getAmount());
        commandGateway.send(debitFromAccount);
	}
	
	@SagaEventHandler(associationProperty = "transactionId")
    @EndSaga
    public void on(FromAccountNotFound event) {
        MarkTransferAsFailed markTransferAsFailed = new MarkTransferAsFailed(event.getTransactionId());
        commandGateway.send(markTransferAsFailed);
    }
	
	@SagaEventHandler(associationProperty = "transactionId")
    @EndSaga
    public void on(ToAccountNotFound event) {
        CreditAccount creditAccount = new CreditAccount(this.fromAccountId, event.getTransactionId(), this.amount);
        commandGateway.send(creditAccount);

        MarkTransferAsFailed markTransferAsFailed = new MarkTransferAsFailed(event.getTransactionId());
        commandGateway.send(markTransferAsFailed);
    }
	
	@SagaEventHandler(associationProperty = "transactionId")
    @EndSaga
    public void on(FromAccountDebitFailedDueNoFunds event) {
		MarkTransferAsFailed command = new MarkTransferAsFailed(event.getTransactionId());
		commandGateway.send(command);
    }
	
	@SagaEventHandler(associationProperty = "transactionId")
    public void on(FromAccountDebited event) {
        CreditToAccount command = new CreditToAccount(toAccountId, event.getTransactionId(), event.getAmount());
        commandGateway.send(command);
    }
	
	@EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void on(ToAccountCredited event) {
        MarkTransferAsCompleted command = new MarkTransferAsCompleted(event.getTransactionId());
        commandGateway.send(command);
    }
}