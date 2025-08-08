# Camel Forage Demo

A demonstration project showcasing the capabilities of [Camel Forage](https://github.com/orpiske/camel-forage), an opinionated library of beans for Apache Camel that provides pre-configured components for AI integrations.

## Overview

This demo demonstrates how to build an AI-powered Camel application using Camel Forage's plug-and-play components. The demo creates a simple chat agent with memory that can remember conversations across multiple interactions.

## What This Demo Shows

- **AI Agent Integration**: Using the `DefaultAgentFactory` to automatically configure AI agents
- **Chat Memory**: Persistent conversation memory using message window chat memory
- **Multiple AI Providers**: Support for both Google Gemini (default) and Ollama through Maven profiles
- **Simple Setup**: Minimal configuration required to get AI functionality working

## Demo Flow

The application runs two timer-triggered routes that simulate a conversation with memory:

1. **Introduction Route** (`timer:startup`): 
   - Introduces the user as "Alice" with memory ID 1
   - Demonstrates initial message storage in chat memory

2. **Memory Test Route** (`timer:check`):
   - Asks "What is my name?" using the same memory ID
   - Demonstrates that the AI agent remembers the previous conversation

## Prerequisites

- Java 17+
- Maven 3.6+
- Either:
  - **Google Gemini API key** (default profile)
  - **Ollama server** running locally (ollama profile)

## Setup

### Option 1: Using Google Gemini (Default)

1. Get a Google Gemini API key from [Google AI Studio](https://aistudio.google.com/)

2. Set environment variables:
```bash
export GOOGLE_API_KEY="your-google-api-key"
export GOOGLE_MODEL_NAME="gemini-2.5-flash"  # Optional, defaults to gemini-1.5-flash
```

3. Run the demo:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.example.MainApp"
```

Or using the Camel Maven plugin:
```bash
mvn camel:run
```

### Option 2: Using Ollama

1. Install and start Ollama:
```bash
# Install Ollama (see https://ollama.ai)
ollama pull llama3  # or your preferred model
ollama serve
```

2. Run with Ollama profile:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.example.MainApp" -Pollama
```

Or using the Camel Maven plugin:
```bash
mvn camel:run -Pollama
```

## Project Structure

```
camel-forage-demo/
├── src/
│   └── main/
│       ├── java/com/example/
│       │   ├── MainApp.java           # Application entry point
│       │   └── MyRouteBuilder.java    # Camel routes configuration
│       └── resources/
│           └── log4j2.properties      # Logging configuration
├── pom.xml                            # Maven configuration with profiles
└── forage-readme.md                   # Camel Forage documentation
```

## Key Components

### Dependencies Used

- **camel-forage simple-agent**: Provides the basic AI agent implementation
- **camel-forage message-window**: Provides chat memory with message window
- **camel-forage google**: Google Gemini model provider (google profile)
- **camel-forage ollama**: Ollama model provider (ollama profile)

### Route Configuration

The demo uses Apache Camel's Java DSL to define two simple routes:

```java
// Introduction route
from("timer:startup?delay=2000&repeatCount=1")
    .setBody(constant("My name is Alice"))
    .setHeader(LangChain4jAgent.Headers.MEMORY_ID, constant(1))
    .to("langchain4j-agent:test-memory-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("${body}");

// Memory test route  
from("timer:check?delay=5000&repeatCount=1")
    .setBody(constant("What is my name?"))
    .setHeader(LangChain4jAgent.Headers.MEMORY_ID, constant(1))
    .to("langchain4j-agent:test-memory-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("${body}");
```

## Expected Output

When running successfully, you should see output similar to:

```
[Timer-startup] route1 INFO  Hello Alice! It's nice to meet you.
[Timer-check] route2 INFO  Your name is Alice.
```

## Maven Profiles

The project includes two Maven profiles for different AI providers:

- **google** (default): Uses Google Gemini models
- **ollama**: Uses locally running Ollama models

## Customization

### Changing AI Models

For Google Gemini, set the environment variable:
```bash
export GOOGLE_MODEL_NAME="gemini-2.5-pro"  # Use a different model
```

For Ollama, the model is configured in the provider (currently uses default model).

### Adding More Routes

Add additional routes to `MyRouteBuilder.java` to expand the demo:

```java
from("timer:periodic?period=30000")
    .setBody(constant("Tell me a joke"))
    .setHeader(LangChain4jAgent.Headers.MEMORY_ID, constant(2))  // Different memory ID
    .to("langchain4j-agent:joke-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Joke: ${body}");
```

## Troubleshooting

### Common Issues

1. **API Key Issues**: Ensure `GOOGLE_API_KEY` is set correctly for Google Gemini
2. **Ollama Connection**: Verify Ollama is running on `http://localhost:11434`
3. **Java Version**: Ensure you're using Java 17 or higher
4. **Memory Issues**: Each conversation should use a unique memory ID

### Enabling Debug Logging

The project is configured to show TRACE-level logging for Camel Forage components. Check the logs for detailed information about component initialization and message processing.

## Learn More

- [Camel Forage Documentation](https://github.com/orpiske/camel-forage)
- [Apache Camel Documentation](https://camel.apache.org/)
- [LangChain4j Documentation](https://docs.langchain4j.dev/)

## License

This demo is licensed under the Apache License 2.0.