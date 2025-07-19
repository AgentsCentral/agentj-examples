package com.agentj.standalone.weather;

import ai.agentscentral.core.agent.Agent;
import ai.agentscentral.core.annotation.Tool;
import ai.agentscentral.core.annotation.ToolParam;
import ai.agentscentral.core.model.Model;
import ai.agentscentral.core.tool.ToolBag;
import ai.agentscentral.openai.config.OpenAIConfig;
import com.agentj.standalone.weather.api.WeatherAPI;

import java.util.List;

import static ai.agentscentral.core.agent.instructor.Instructors.stringInstructor;

public class WeatherAgent {

    private static final OpenAIConfig config = new OpenAIConfig(1D, System.getenv("OPEN_AI_KEY"));

    private static final ToolBag weatherTools = new ToolBag() {
        private final WeatherAPI api = new WeatherAPI();

        @Tool(name = "weather_tool", description = "Provides weather information about the city")
        public String weatherInformation(@ToolParam(name = "city", description = "City") String city) {
            System.out.println("calling weather info");

            return api.getWeatherInformation(city);
        }
    };

    private static final Agent weatherAgent = new Agent("weather_agent",
            new Model("o4-mini", config),
            List.of(stringInstructor("You are a weather assistant. You are responsible for telling current weather information about the city.")),
            List.of(weatherTools),
            List.of()
    );

    public static Agent getAgent(){
        return weatherAgent;
    }

}
