# AgentJ Examples

AgentJ examples Test


### Add Dependencies

```
    <dependencies>
        <dependency>
            <groupId>ai.agentscentral</groupId>
            <artifactId>agentj-openai</artifactId>
            <version>0.0.4</version>
        </dependency>
        <dependency>
            <groupId>ai.agentscentral</groupId>
            <artifactId>agentj-jetty</artifactId>
            <version>0.0.4</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.21</version>
        </dependency>        
    </dependencies>
```

# Standalone Agent

## Setting up a weather agent

### Create a stub weatherAPI 
Weather API to be used by LLM agent to retrieve current weather information
```
public class WeatherAPI {

    private final Random temperatureGenerator = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    public String getWeatherInformation(String city){
        double temperature = temperatureGenerator.nextDouble(1D, 50);
        return asJson(new WeatherInfo(city, temperature + " Celsius"));
    }

    private String asJson(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
```
### Create Weather Agent

```
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

```
### Running the weather agent chat bot using HTTP

Setup the `WeatherChatBot` to run it using `JettyHttpRunner` . Default port is `8181`

```
public class WeatherChatBot {


    public static void main(String[] args) throws Exception {

        final HttpConfig weatherChatConfig = new HttpConfig("/chat/*", WeatherAgent.getAgent());
        final AgentJConfig agentJConfig = new AgentJConfig(List.of(weatherChatConfig));

        AgentJStarter.run(new JettyHttpRunner(defaultJettyConfig(), agentJConfig));
    }


}

```

### Health Checks

#### Health check

`curl --location 'http://localhost:8181/health'`

#### Liveness check

`curl --location 'http://localhost:8181/health/liveness'`

#### Readiness check

`curl --location 'http://localhost:8181/health/readiness'`

### Chatting with application

*Request* 

```
curl --location 'http://localhost:8181/chat/cv_f66cc4ab8c6346f6bfdd898c255dcd2c' \
--header 'Content-Type: application/json' \
--data '{
    "message": "How is the weather in paris"
}'
```

*Response*

```
{
    "sessionId": "cv_f66cc4ab8c6346f6bfdd898c255dcd2c",
    "messages": [
        "The current temperature in Paris is approximately 46.3 Â°C."
    ]
}
```













