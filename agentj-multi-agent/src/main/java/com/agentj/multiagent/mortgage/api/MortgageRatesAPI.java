package com.agentj.multiagent.mortgage.api;

import java.math.BigDecimal;
import java.util.Random;

import static java.math.RoundingMode.HALF_EVEN;

public class MortgageRatesAPI {

    private final Random rateGenerator = new Random();

    public String getMortgageRates(BigDecimal propertyPrice, BigDecimal loanAmount){
        final String ltv = ltv(propertyPrice, loanAmount);
        final double mortgageRate =  rateGenerator.nextDouble(1D, 6);
        return "Your loan to value (LTV) is " + ltv + " and your mortgage rate is going to be " + mortgageRate + "%";
    }

    private String ltv(BigDecimal propertyPrice, BigDecimal loanAmount){
        return loanAmount
                .divide(propertyPrice, HALF_EVEN)
                .multiply(BigDecimal.valueOf(100)).setScale(2, HALF_EVEN).toPlainString();
    }

}
