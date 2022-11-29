package pe.edu.upc.banking.transactions.command.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.banking.transactions.command.application.dto.*;
import pe.edu.upc.banking.transactions.command.domain.*;
import pe.edu.upc.banking.accounts.contracts.commands.*;
import pe.edu.upc.banking.transactions.contracts.commands.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transactions")
public class TransactionCommandController {
	private final CommandGateway commandGateway;
	
	public TransactionCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

	@PostMapping("/deposit")
	public ResponseEntity<Object> deposit(@Validated @RequestBody DepositMoneyRequestDto depositMoneyRequestDto) {
		String transactionId = UUID.randomUUID().toString();
		CreditAccount command = new CreditAccount(
				depositMoneyRequestDto.getAccountId(),
				transactionId,
				depositMoneyRequestDto.getAmount()
		);
		CompletableFuture<Object> future = commandGateway.send(command);
		CompletableFuture<Object> futureResponse = future.handle((ok, ex) -> {
			if (ex != null) {
				return new DepositMoneyErrorResponseDto();
			}
			return new DepositMoneyOkResponseDto(transactionId);
		});
		try {
			Object response = (Object)futureResponse.get();
			if (response instanceof DepositMoneyOkResponseDto) {
				return new ResponseEntity(response, HttpStatus.OK);
			}
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		} catch( Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/withdraw")
	public ResponseEntity<Object> withdraw(@Validated @RequestBody WithdrawMoneyRequestDto withdrawMoneyRequestDto) {
		String transactionId = UUID.randomUUID().toString();
		DebitAccount command = new DebitAccount(
				withdrawMoneyRequestDto.getAccountId(),
				transactionId,
				withdrawMoneyRequestDto.getAmount()
		);
		CompletableFuture<Object> future = commandGateway.send(command);
		CompletableFuture<Object> futureResponse = future.handle((ok, ex) -> {
			if (ex != null) {
				if (ex instanceof OverdraftLimitExceededException) {
					return new WithdrawMoneyErrorResponseDto("The account exceeded the overdraft Limit");
				}
				return new WithdrawMoneyErrorResponseDto();
			}
			return new WithdrawMoneyOkResponseDto(transactionId);
		});
		try {
			Object response = (Object)futureResponse.get();
			if (response instanceof WithdrawMoneyOkResponseDto) {
				return new ResponseEntity(response, HttpStatus.OK);
			}
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		} catch( Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<Object> transfer(@Validated @RequestBody MoneyTransferRequestDto moneyTransferRequestDto) {
		String transactionId = UUID.randomUUID().toString();
		CreateMoneyTransfer command = new CreateMoneyTransfer(
			transactionId,
			moneyTransferRequestDto.getFromAccountId(),
			moneyTransferRequestDto.getToAccountId(),
			moneyTransferRequestDto.getAmount()
		);
		CompletableFuture<Object> future = commandGateway.send(command);
		CompletableFuture<Object> futureResponse = future.handle((ok, ex) -> {
		    if (ex != null) {
		        return new MoneyTransferErrorResponseDto();
		    }
		    return new MoneyTransferOkResponseDto(transactionId);
		});
		try {
			Object response = (Object)futureResponse.get();
			if (response instanceof MoneyTransferOkResponseDto) {
				return new ResponseEntity(response, HttpStatus.OK);
			}
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		} catch( Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}