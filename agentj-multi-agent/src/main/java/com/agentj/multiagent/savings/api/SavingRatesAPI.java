package com.agentj.multiagent.savings.api;

import java.math.BigDecimal;
import java.util.Random;

public class SavingRatesAPI {

    private final Random rateGenerator = new Random();

    public String getSavingRate(BigDecimal savingAmount){
        final double savingRate =  rateGenerator.nextDouble(1D, 10);
        return "Saving rate on £" + savingAmount + " is " + savingRate + "%";
    }

}
