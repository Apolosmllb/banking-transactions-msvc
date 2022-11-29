package pe.edu.upc.banking.transactions.command.application.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MoneyTransferRequestDto {
	@NotNull
	private String fromAccountId;
	private String toAccountId;
	private BigDecimal amount;

	public String getFromAccountId() {
		return fromAccountId;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}