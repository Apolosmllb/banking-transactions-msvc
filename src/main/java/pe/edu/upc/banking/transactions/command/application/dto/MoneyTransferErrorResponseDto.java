package pe.edu.upc.banking.transactions.command.application.dto;

public class MoneyTransferErrorResponseDto {
	private String message;
	
	public MoneyTransferErrorResponseDto()
	{
		this.message = "Error with the transfer";
	}
	
	public MoneyTransferErrorResponseDto(String message)
	{
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}