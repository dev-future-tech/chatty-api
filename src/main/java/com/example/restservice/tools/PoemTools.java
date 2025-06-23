package com.example.restservice.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

public class PoemTools {

    @Tool
    public List<String> authors() {
        return List.of("Allen Ricther", "Ncuti Gatwa", "Anson Mount", "Sonequa Martin-Green", "Stephen King", "Fred Astaire", "Marilyn Monroe");
    }

    @Tool
    public List<String> composers(@ToolParam int count) {
        return List.of("Amadeus Wolfgang Mozart", "Beethoven", "Strauss", "Schumann", "Schubert", "Tchaikovsky").subList(0, count);
    }
}
