package com.threatdetection.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

	public static final int NOT_MATCHED = 0;
	public static final int MATCHED = 1;
	public static final int MATCHING_CANDIDATE = 2;

	public static final int DEDUCT = 1;
	public static final int REVERSAL = -1;

	public static final String REVERSAL_STRING = "REVERSAL";

	public static final int CANDIDATE_INDEX_NOT_SET = -1;

	private String profileName;
	private LocalDateTime transactionDate;
	private BigDecimal transactionAmount;
	private String transactionNarrative;
	private String transactionDescription;
	private String transactionID;
	private int transactionType;
	private String walletReference;
	private int matching;
	private int lineNumber;
	private int candidateIndex;

	public Transaction(String profileName, LocalDateTime transactionDate, BigDecimal transactionAmount,
			String transactionNarrative, String transactionDescription, String transactionID, int transactionType,
			String walletReference, int lineNumber) {
		super();
		this.setProfileName(profileName);
		this.setTransactionDate(transactionDate);
		this.setTransactionAmount(transactionAmount);
		this.setTransactionNarrative(transactionNarrative);
		this.setTransactionDescription(transactionDescription);
		this.setTransactionID(transactionID);
		this.setTransactionType(transactionType);
		this.setWalletReference(walletReference);
		this.setMatching(NOT_MATCHED);
		this.setLineNumber(lineNumber);
		this.setCandidateIndex(CANDIDATE_INDEX_NOT_SET);
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionNarrative() {
		return transactionNarrative;
	}

	public void setTransactionNarrative(String transactionNarrative) {
		this.transactionNarrative = transactionNarrative;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getWalletReference() {
		return walletReference;
	}

	public void setWalletReference(String walletReference) {
		this.walletReference = walletReference;
	}

	public int getMatching() {
		return matching;
	}

	public void setMatching(int matching) {
		this.matching = matching;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getCandidateIndex() {
		return candidateIndex;
	}

	public void setCandidateIndex(int candidateIndex) {
		this.candidateIndex = candidateIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Transaction other = (Transaction) obj;
		return Objects.equals(profileName, other.profileName)
				&& Objects.equals(transactionAmount, other.transactionAmount)
				&& Objects.equals(transactionDate, other.transactionDate)
				&& Objects.equals(transactionDescription, other.transactionDescription)
				&& Objects.equals(transactionID, other.transactionID)
				&& Objects.equals(transactionNarrative, other.transactionNarrative)
				&& transactionType == other.transactionType && Objects.equals(walletReference, other.walletReference);
	}

	@Override
	public int hashCode() {
		return Objects.hash(profileName, transactionAmount, transactionDate, transactionDescription, transactionID,
				transactionNarrative, transactionType, walletReference);
	}

}
