package currency.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CONVERSION_RATES")
public class CurrencyRates {
	private static final String SEQUENCE_NAME_KEY = "SEQ_NAME";
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME_KEY)
    @SequenceGenerator(name = SEQUENCE_NAME_KEY, sequenceName = "CONVERSION_SEQUENCE")
    @Column(name = "CONVERSION_ID")
    private long id;
    
    @NotNull(message = "{holder.name.missing}")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "{holder.name.invalid-char}")
    @Size(min = 2, max = 30, message = "{holder.name.length}")
    @Column(name = "CURRENCY_NAME")
    private String currencyName;
    
    @PositiveOrZero(message = "{acct.rate.negative}")
    @Column(name = "CONVERSION_RATE_SEK")
    private double toSEK;
    
    @PositiveOrZero(message = "{acct.rate.negative}")
    @Column(name = "CONVERSION_RATE_USD")
    private double toUSD;
    
    @PositiveOrZero(message = "{acct.rate.negative}")
    @Column(name = "CONVERSION_RATE_EURO")
    private double toEuro;
    
    @PositiveOrZero(message = "{acct.rate.negative}")
    @Column(name = "CONVERSION_RATE_YEN")
    private double toYen;
    
    public CurrencyRates(String currencyName, double toSEK, double toUSD, double toEuro, double toYen) {
    	this.currencyName = currencyName;
    	this.toSEK 	= 	toSEK;
    	this.toUSD	= 	toUSD;
    	this.toEuro = 	toEuro;
    	this.toYen 	= 	toYen;
    }
    
    protected CurrencyRates() {
    }

	public String getCurrencyName() {
		return currencyName;
	}

	public BigDecimal getConversionSEK() {
		return new BigDecimal(toSEK);
	}

	public BigDecimal getConversionUSD() {
		return new BigDecimal(toUSD);
	}

	public BigDecimal getConversionEuro() {
		return new BigDecimal(toEuro);
	}

	public BigDecimal getConversionYen() {
		return new BigDecimal(toYen);
	}
}
