package pe.edu.upc.banking.transactions.query.projections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class TransactionView {
	@Id
	@Column(length=36)
    private String transactionId;
	@Column(length=36)
    private String fromAccountId;
	@Column(length=36, nullable = true)
	private String toAccountId;
    private BigDecimal amount;
	@Column(length=15)
    private String transactionType;
	@Column(length=15)
	private String transactionStatus;
	private Instant createdAt;

    public TransactionView() {
    }
    
    public TransactionView(String transactionId, String fromAccountId, String toAccountId, BigDecimal amount, String transactionType, String transactionStatus, Instant createdAt) {
		this.transactionId = transactionId;
    	this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
        this.amount = amount;
        this.transactionType = transactionType;
		this.transactionStatus = transactionStatus;
		this.createdAt = createdAt;
    }

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getFromAccountId() {
		return fromAccountId;
	}

	public void setFromAccountId(String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionStatus() { return transactionStatus; }

	public void setTransactionStatus(String transactionStatus) { this.transactionStatus = transactionStatus; }

	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	public Instant getCreatedAt() { return createdAt; }
}