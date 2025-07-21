package com.agentj.multiagent;

import ai.agentscentral.http.config.AgentJConfig;
import ai.agentscentral.http.config.HttpConfig;
import ai.agentscentral.http.runner.AgentJStarter;
import ai.agentscentral.jetty.runner.JettyHttpRunner;

import java.util.List;

import static ai.agentscentral.jetty.config.JettyConfig.defaultJettyConfig;
import static com.agentj.multiagent.banking.BankingTeam.bankingTeam;

public class BankingChatBot {

    public static void main(String[] args) throws Exception {

        final HttpConfig bankingChatConfig = new HttpConfig("/chat/*", bankingTeam());
        final AgentJConfig agentJConfig = new AgentJConfig(List.of(bankingChatConfig));

        AgentJStarter.run(new JettyHttpRunner(defaultJettyConfig(), agentJConfig));
    }
}