package currency.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import currency.domain.CurrencyRates;
import currency.repository.ConversionRepository;
import currency.domain.Conversion;

@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Service("currency.application.ConversionService")
public class ConversionService {
    @Autowired
    private ConversionRepository conversionRepository;
	
    /**
     * Called when the user want to convert a currency to another
     * 
     * @param startValue The start value in the start currency
     * @param startCurrency The start currency
     * @param endCurrency The currency to convert to
     * @return The start value in the specified end currency rounded to two decimals.
     */
	public BigDecimal conversion(BigDecimal startValue, String startCurrency, String endCurrency) {
		CurrencyRates currencyRates = conversionRepository.findCurrencyRatesByCurrencyName(startCurrency);
		return Conversion.convert(currencyRates, startValue, endCurrency);
	}
}
