package currency.presentation.conversion;

import currency.application.ConversionService;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
@Scope("session")
public class ConversionController {
	static final String DEFAULT_PAGE_URL = "/";
	static final String INDEX_PAGE = "conversion";
	static final String RESULT_PAGE = "conversionResult";
	static final String ABOUT_PAGE = "aboutCurrencyConverters";
	@Autowired
	ConversionService conversionService;
	
	@GetMapping(DEFAULT_PAGE_URL)
    public String showDefaultView() {
        return "redirect:" + INDEX_PAGE;
    }
	
	@GetMapping("/" + INDEX_PAGE)
    public String showConversionView(ConversionForm conversionForm) {
        return INDEX_PAGE;
    }
	
    @PostMapping("/" + INDEX_PAGE)
    public String getResult(@Valid ConversionForm conversionForm, BindingResult bindingResult, Model model){
		
        if (!bindingResult.hasErrors()) {
        	BigDecimal conversion = conversionService.conversion(new BigDecimal(conversionForm.getStartValue()), conversionForm.getStartCurrency(), conversionForm.getEndCurrency());
            if (conversion != null) {
            	model.addAttribute("conversion", conversion.toString());
            }
        }
        
        return INDEX_PAGE;
    }
    
    @GetMapping("/" + ABOUT_PAGE)
    public String showAboutView() {
        return ABOUT_PAGE;
    }
}
