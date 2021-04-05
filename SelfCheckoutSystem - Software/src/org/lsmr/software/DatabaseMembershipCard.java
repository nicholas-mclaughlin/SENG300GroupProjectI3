package org.lsmr.software;

import java.util.*;



public class DatabaseMembershipCard {
	
	
	private static class MemberCardDetails {
		String cardHolder;
		Date cardExpiryDate;
	}
	
	private static HashMap<String, MemberCardDetails> membershipDatabase = new HashMap<>(); 
	
	public static void newMember(String cardNumber, String cardHolder, Date cardExpiryDate) {
		
		MemberCardDetails newCardDetails = new MemberCardDetails();

		newCardDetails.cardHolder = cardHolder;
		newCardDetails.cardExpiryDate = cardExpiryDate;
		
		membershipDatabase.put(cardNumber, newCardDetails); 
	}
	
	public static String getCardHolder(String cardNumber) {
		return membershipDatabase.get(cardNumber).cardHolder;
	}
	
	public static Date getCardExpiryDate(String cardNumber) {
		return membershipDatabase.get(cardNumber).cardExpiryDate;
	}
	
	public static boolean isCardInDatabase(String cardNumber) {
		if(membershipDatabase.containsKey(cardNumber)) {
			return true;
		}
		else return false;
	}
	
	
	
	private static boolean isCardNumberValid(String cardNumber){
		if(cardNumber != null) {
			try {
				Integer.parseInt(cardNumber);
			}
			catch(NumberFormatException e){
				return false;
			}
		}
		
		if(cardNumber == null) {
				return false;
		}
		
		return true;
	}
	
	private static boolean isCardHolderValid(String cardHolder) {
		if(cardHolder != null) {
			if(cardHolder == "") {
				return false;
			}
		}
		
		if(cardHolder == null) {
			return false;
		}
		
		return true;
	}
		
	private static boolean isCardExpiryDateValid(Date cardExpiryDate) {
		if(cardExpiryDate != null) {
			Date aDate = new Date();
			if(cardExpiryDate.before(aDate)) {
				return false;
			}
		}
		
		if(cardExpiryDate == null) {
			return false;
		}
	
		return true;
	}
}
