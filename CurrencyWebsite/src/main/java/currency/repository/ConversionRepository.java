package currency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import currency.domain.CurrencyRates;

/**
 * Contains all database access concerning the conversions.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ConversionRepository extends JpaRepository<CurrencyRates, Long>{
	
	/**
	 * 
	 * @param currencyName The currency name
	 * @return All currency rates based on the currency name
	 */
	CurrencyRates findCurrencyRatesByCurrencyName(String currencyName);
}
