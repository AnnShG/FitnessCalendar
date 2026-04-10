package com.example.fitnesscalendar.repository;

import android.annotation.SuppressLint;

import com.example.fitnesscalendar.entities.AiMessage;
import com.example.fitnesscalendar.relations.DateColourResult;
import com.example.fitnesscalendar.relations.UserWithGoals;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class AiRepository {
    private final GenerativeModelFutures model;

    public AiRepository() {
        // initialize Gemini 1.5 Flash
        GenerativeModel gm = FirebaseVertexAI.getInstance()
                .generativeModel("gemini-1.5-flash");
        this.model = GenerativeModelFutures.from(gm);
    }

    @SuppressLint("DefaultLocale")
    public String buildPrompt(UserWithGoals user, List<DateColourResult> history) {
        int age = Period.between(
                user.user.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()
        ).getYears();

        String goals = user.goals.stream()
                .map(g -> g.goalTitle)
                .collect(Collectors.joining(", "));

        long completed = history.stream().filter(h -> h.isCompleted).count();
        long total = history.size();

        return String.format(
                "I am a %d-year-old %s. My fitness goals are: %s. " +
                        "In the last period, I scheduled %d workouts and completed %d of them. " +
                        "Based on this, give me one short, motivating text advice for my fitness journey.",
                age, user.user.gender, goals, total, completed
        );
    }

    public ListenableFuture<GenerateContentResponse> getAdvice(String prompt, List<AiMessage> history) {
        // convert Room history to Gemini Content objects
        Content.Builder contentBuilder = new Content.Builder();

        contentBuilder.setRole("user");
        contentBuilder.addText(prompt);
        Content userContent = contentBuilder.build();

        return model.generateContent(userContent);
    }
}
