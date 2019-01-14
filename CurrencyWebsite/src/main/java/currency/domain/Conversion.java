package currency.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Conversion {
	
    /**
     * Called when the user want to convert a currency to another
     * 
     * @param currencyRates All the currency rates for the start currency
     * @param startValue The start value in the start currency
     * @param endCurrency The currency to convert to
     * @return The start value in the specified end currency rounded to two decimals.
     */
	public static BigDecimal convert(CurrencyRates currencyRates, BigDecimal startValue, String endCurrency) {
		BigDecimal convertedMoney = null;
		if(endCurrency.equals("SEK")) {
			convertedMoney = currencyRates.getConversionSEK().multiply(startValue);
		} else if (endCurrency.equals("USD")) {
			convertedMoney = currencyRates.getConversionUSD().multiply(startValue);
		} else if (endCurrency.equals("Euro")) {
			convertedMoney = currencyRates.getConversionEuro().multiply(startValue);
		} else if (endCurrency.equals("Yen")) {
			convertedMoney = currencyRates.getConversionYen().multiply(startValue);
		}
		
		if (convertedMoney != null) {
			return convertedMoney.setScale(2, RoundingMode.HALF_UP);
		}
		return null;
	}
}
