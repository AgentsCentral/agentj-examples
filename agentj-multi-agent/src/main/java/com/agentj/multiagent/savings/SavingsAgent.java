package com.agentj.multiagent.savings;

import ai.agentscentral.core.agent.Agent;
import ai.agentscentral.core.annotation.Tool;
import ai.agentscentral.core.annotation.ToolParam;
import ai.agentscentral.core.handoff.Handoff;
import ai.agentscentral.core.model.Model;
import ai.agentscentral.core.tool.ToolBag;
import ai.agentscentral.openai.config.OpenAIConfig;
import com.agentj.multiagent.savings.api.SavingRatesAPI;

import java.math.BigDecimal;
import java.util.List;

import static ai.agentscentral.core.agent.instructor.Instructors.stringInstructor;


public class SavingsAgent {

    private static final OpenAIConfig config = new OpenAIConfig(1D, System.getenv("OPEN_AI_KEY"));

    private static final Handoff handoffToBankingAgent = new Handoff("handoffToBankingAgent",
            "banking_agent", "Transfer to banking agent for any other inquiries");

    private static final ToolBag savingsToolBag = new ToolBag() {
        private final SavingRatesAPI api = new SavingRatesAPI();

        @Tool(name = "saving_rates_calculator", description = "Calculate saving rates based on savingAmount")
        public String savingRatesCalculator(@ToolParam(name = "savingAmount", description = "Saving amount in GBP") String savingAmount) {
            return api.getSavingRate(new BigDecimal(savingAmount));
        }
    };

    private static final String SAVING_AGENT_INSTRUCTIONS = """ 
                You are a saving accounts assistant at a bank.
                - You are responsible for answer users questions about the saving rates based on the amount of savings they have got.
                - Ask the customer about the amount of savings they have got. The users may provide the amount in short form like 5K which means 5000.
                - The customer can also provide information in following way, "I have 50K", which means 50000 in savings. Or just "5000" which means 5000 in savings.
                - For any inquires that are not related to saving rates transfer to banking agent."
            """;
    private static final Agent savingsAgent = new Agent("savings_agent",
            new Model("gpt-4o", config),
            List.of(stringInstructor(SAVING_AGENT_INSTRUCTIONS)),
            List.of(savingsToolBag),
            List.of(handoffToBankingAgent)
    );

    public static Agent savingAgent() {
        return savingsAgent;

    }

}
