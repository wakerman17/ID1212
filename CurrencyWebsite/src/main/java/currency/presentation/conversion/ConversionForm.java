package currency.presentation.conversion;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import currency.util.Util;

/**
 * Logic for the ConversionForm.
 *
 */
class ConversionForm {
    @NotBlank	(message = "Please specify amount")
    @Pattern 	(regexp="^$|^[0-9]*\\.?[0-9]+$", message = "Only letters allowed")
    private String startValue;
    
    @NotBlank	(message = "You have to specify the start currency")
    private String startCurrency;
    @NotBlank	(message = "You have to specify the end currency")
    private String endCurrency;
    
    /**
     * 
     * @return The start value
     */
    public String getStartValue() {
        return startValue;
    }
    
    /**
     * 
     * @param startValue The new start value
     */
    public void setStartValue(String startValue) {
    	this.startValue = startValue;
    }
    
    /**
     * 
     * @return The start currency
     */
    public String getStartCurrency() {
        return startCurrency;
    }
    
    /**
     * 
     * @param startCurrency The new start currency
     */
    public void setStartCurrency(String startCurrency) {
        this.startCurrency = startCurrency;
    }
    
    /**
     * 
     * @return The end currency
     */
    public String getEndCurrency() {
        return endCurrency;
    }
    
    /**
     * 
     * @param endCurrency The new end currency
     */
    public void setEndCurrency(String endCurrency) {
        this.endCurrency = endCurrency;
    }
    
    @Override
    public String toString() {
        return Util.toString(this);
    }
}
