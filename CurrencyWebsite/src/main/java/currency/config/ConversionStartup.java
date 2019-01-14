package currency.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import currency.domain.CurrencyRates;
import currency.repository.ConversionRepository;

/**
 * When the program runs this class is executed once to setup the database with static conversion rates.
 * 
 */
@Component
public class ConversionStartup implements ApplicationRunner  {
	
	private ConversionRepository conversionRepository;
	
	/**
	 * Set the reference to a conversion repository
	 * 
	 * @param conversionRepository The reference to a conversion repository
	 */
    @Autowired
    public ConversionStartup(ConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }
    
    @Transactional
	@Override
	public void run(ApplicationArguments args) throws Exception {
		runHelper("SEK", 1.0, 0.110, 0.097, 12.285);
		runHelper("USD", 9.052, 1.0, 0.879, 111.205);
		runHelper("Euro", 10.294, 1.137, 1.0, 126.453);
		runHelper("Yen", 0.081, 0.009, 0.008, 1.00);
	}
	
	private void runHelper(String currencyName, double conversionRateSEK, double conversionRateUSD, double conversionRateEuro, double conversionRateYen) {
		conversionRepository.save(new CurrencyRates(currencyName, conversionRateSEK, 
				conversionRateUSD, conversionRateEuro, conversionRateYen));
	}

}