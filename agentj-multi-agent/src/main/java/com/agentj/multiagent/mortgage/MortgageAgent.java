package com.agentj.multiagent.mortgage;

import ai.agentscentral.core.agent.SimpleAgent;
import ai.agentscentral.core.annotation.Tool;
import ai.agentscentral.core.annotation.ToolParam;
import ai.agentscentral.core.handoff.Handoff;
import ai.agentscentral.core.model.Model;
import ai.agentscentral.core.tool.ToolBag;
import ai.agentscentral.openai.config.OpenAIConfig;
import com.agentj.multiagent.mortgage.api.MortgageRatesAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static ai.agentscentral.core.agent.instructions.Instructors.stringInstructor;

public class MortgageAgent {

    private static final Logger logger = LoggerFactory.getLogger(MortgageAgent.class);
    private static final OpenAIConfig config = new OpenAIConfig(1D, System.getenv("OPEN_AI_KEY"));

    private static final Handoff handoffToBankingAgent = new Handoff("handoffToBankingAgent",
            "banking_agent", "Transfer to banking agent for any other inquiries", null);
    private static final ToolBag mortgageToolBag = new ToolBag() {
        private final MortgageRatesAPI api = new MortgageRatesAPI();

        @Tool(name = "mortgage_rates_calculator", description = "Calculate mortgage rates based on load to value")
        public String mortgageRatesCalculator(@ToolParam(name = "propertyPrice", description = "Price of the property in GBP") String propertyPrice,
                                         @ToolParam(name = "loanAmount", description = "Loan amount in GBP") String loanAmount) {

            logger.info("Calculating mortgage rates bases on loan {} and property price {}", loanAmount, propertyPrice);

            return api.getMortgageRates(new BigDecimal(propertyPrice), new BigDecimal(loanAmount));
        }
    };

    private static final SimpleAgent mortgageAgent = new SimpleAgent("mortgage_agent",
            new Model("gpt-4o", config),
            List.of(stringInstructor("You are a mortgage rates assistant. You are responsible for assist users with mortgage rates based on loan to value (LTV). In case of inquiry, ask customer about the property value and loan amount to calculate rates. For inquires other than mortgages, transfer to banking agent.")),
            List.of(mortgageToolBag),
            List.of(handoffToBankingAgent)
    );

    public static SimpleAgent mortgageAgent(){
        return mortgageAgent;
    }

}
