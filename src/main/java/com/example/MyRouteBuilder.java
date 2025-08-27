package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.langchain4j.agent.Headers;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {


    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("timer:startup?delay=2000&repeatCount=1")
                .setBody(constant("My name is Alice"))
                .to("langchain4j-agent:test-memory-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
                .log("${body}");

        from("timer:check?delay=5000&repeatCount=1")
                .setBody(constant("What is my name?"))
                .to("langchain4j-agent:test-memory-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
                .log("${body}");
    }

}
