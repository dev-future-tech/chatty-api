package com.example.restservice.tools;

import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

public class PoemTools {

    @Tool
    public List<String> authors() {
        return List.of("Allen Ricther", "Ncuti Gatwa", "Anson Mount", "Sonequa Martin-Green", "Stephen King", "Fred Astaire", "Marilyn Monroe");
    }

    @Tool
    public List<String> composers() {
        return List.of("Amadeus Wolfgang Mozart", "Beethoven", "Strauss", "Schumann", "Schubert", "Tchaikovsky");
    }
}
