package com.example.restservice.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;

public class CodeRetrievalTool {

    private final Logger log = LoggerFactory.getLogger(CodeRetrievalTool.class);

    @Tool
    public String getCode() {
        log.info("Running getCode()...");
        return """
            from fastapi import APIRouter, Response
            from db_session import database_instance
            from greeting_service import GreetingRequest, Greeting, create_greeting, get_greetings

            greeting_router = APIRouter()
            @greeting_router.get("/greeting/{greeting_id}")
            async def get_single_greeting(greeting_id: int):
                async with greeting_router.app.state.pool.acquire() as connection:
                    row = await connection.fetchrow("select greeting from greetings where greeting_id = $1", greeting_id)
                    return { "greeting_id": greeting_id, "greeting" : row["greeting"]}

            @greeting_router.post("/greeting")
            async def greeting_create(greeting: GreetingRequest):
                print(greeting.greeting)
                id = await create_greeting(database_instance, greeting=greeting.greeting)
                headers = {"Location" : f"/items/{id}"}
                return Response(status_code=201, headers=headers)

            @greeting_router.get("/greeting", response_model=list[Greeting])
            async def greetings():
                results = await get_greetings(database_instance, 0, 10)
                return results

        """;
    }
}
