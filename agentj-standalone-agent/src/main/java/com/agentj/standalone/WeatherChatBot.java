package com.agentj.standalone;

import ai.agentscentral.http.config.AgentJConfig;
import ai.agentscentral.http.config.HttpConfig;
import ai.agentscentral.http.config.cors.CORSConfig;
import ai.agentscentral.http.runner.AgentJStarter;
import ai.agentscentral.jetty.config.JettyConfig;
import ai.agentscentral.jetty.runner.JettyHttpRunner;
import com.agentj.standalone.weather.WeatherAgent;

import java.util.List;
import java.util.Set;

public class WeatherChatBot {


    public static void main(String[] args) throws Exception {

        CORSConfig corsConfig = CORSConfig.builder()
                .allowedOrigins(Set.of("http://localhost:5173"))
                .allowedHeaders(Set.of("Content-Type", "Authorization"))
                .allowedMethods(Set.of("GET", "POST"))
                .allowCredentials(true)
                .build();
        final HttpConfig weatherChatConfig = new HttpConfig("/chat/*", WeatherAgent.getAgent(), List.of(), corsConfig);
        final AgentJConfig agentJConfig = new AgentJConfig(List.of(weatherChatConfig));

        JettyConfig jettyConfig = JettyConfig.defaultConfig();

        // Enable logging
        System.setProperty("org.eclipse.jetty.LEVEL", "DEBUG");
        
        System.out.println("Starting server with the following configuration: "+ jettyConfig.toString());

        AgentJStarter.run(new JettyHttpRunner(jettyConfig, agentJConfig));
    }


}