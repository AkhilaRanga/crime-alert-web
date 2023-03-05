package com.crimealert.enums;

public class CrimeLevelEnum {
	public enum crimeLevel {
		HIGH ("HIGH"),
		LOW ("LOW");

		public final String crimeLevelValue;
	
		crimeLevel(String crimeLevelValue) {
			this.crimeLevelValue = crimeLevelValue;
		}
	} // valueOfLabel
}
