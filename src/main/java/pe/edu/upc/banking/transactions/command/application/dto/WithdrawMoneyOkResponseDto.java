package pe.edu.upc.banking.transactions.command.application.dto;

public class WithdrawMoneyOkResponseDto {
	private String transactionId;
	
	public WithdrawMoneyOkResponseDto(String transactionId)
	{
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}
}