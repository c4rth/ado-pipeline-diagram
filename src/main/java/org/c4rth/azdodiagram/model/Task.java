package org.c4rth.azdodiagram.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Task {
    private String task;
    private String displayName;

    public String getCellName() {
        if (task.startsWith("6d15af64-176c-496d-b583-fd2ae21d4df4")) return "Checkout";
        return displayName == null ? task : displayName;
    }
}
