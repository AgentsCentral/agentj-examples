package com.agentj.multiagent.banking;

import ai.agentscentral.core.agent.AgentSystem;
import ai.agentscentral.core.agent.SimpleAgent;
import ai.agentscentral.core.handoff.Handoff;
import ai.agentscentral.core.model.Model;
import ai.agentscentral.openai.config.OpenAIConfig;

import java.util.List;

import static ai.agentscentral.core.agent.instructions.Instructors.stringInstructor;
import static com.agentj.multiagent.mortgage.MortgageAgent.mortgageAgent;
import static com.agentj.multiagent.savings.SavingsAgent.savingAgent;

public class BankingAgents {

    private static final OpenAIConfig config = new OpenAIConfig(1D, System.getenv("OPEN_AI_KEY"));

    private static final Handoff handoffToSavingsAgent = new Handoff("handoffToSavingsAgent",
            "savings_agent", "Transfer to savings agent related questions");

    private static final Handoff handoffToMortgageAgent = new Handoff("handoffToMortgageAgent",
            "mortgage_agent", "Transfer to mortgage agent for mortgage related inquiries");

    private static final SimpleAgent bankingAgent = new SimpleAgent("banking_agent",
            new Model("gpt-4o", config),
            List.of(stringInstructor("You are an assistant at a bank. For savings inquires transfer to transfer to savings agent, " +
                    "for mortgage rates related questions transfer to mortgage agent")),
            List.of(),
            List.of(handoffToMortgageAgent, handoffToSavingsAgent)
    );

    public static AgentSystem bankingSystem() {
        return new AgentSystem("Banking system", bankingAgent, List.of(mortgageAgent(), savingAgent()));

    }

}
