package com.agentj.standalone;

import ai.agentscentral.http.config.AgentJConfig;
import ai.agentscentral.http.config.HttpConfig;
import ai.agentscentral.http.runner.AgentJStarter;
import ai.agentscentral.jetty.runner.JettyHttpRunner;
import com.agentj.standalone.weather.WeatherAgent;

import java.util.List;

import static ai.agentscentral.jetty.config.JettyConfig.defaultJettyConfig;

public class WeatherChatBot {


    public static void main(String[] args) throws Exception {

        final HttpConfig weatherChatConfig = new HttpConfig("/chat/*", WeatherAgent.getAgent());
        final AgentJConfig agentJConfig = new AgentJConfig(List.of(weatherChatConfig));

        AgentJStarter.run(new JettyHttpRunner(defaultJettyConfig(), agentJConfig));
    }


}