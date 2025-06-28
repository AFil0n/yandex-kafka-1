package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private String title;
    private String text;

    @Override
    public String toString() {
        return String.format("id: %d :: title: %s :: text %s", id, title, text);
    }
}
