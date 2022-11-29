package pe.edu.upc.banking.transactions.command.application.dto;

public class MoneyTransferOkResponseDto {
	private String transactionId;
	
	public MoneyTransferOkResponseDto(String transactionId)
	{
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}
}