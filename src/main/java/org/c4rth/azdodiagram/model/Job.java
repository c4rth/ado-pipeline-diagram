package org.c4rth.azdodiagram.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Job {
    private String job;
    private String displayName;
    private String[] dependsOn;
    private Task[] steps;

    public String getCellName() {
        return displayName == null ? job : displayName;
    }

    public boolean hasDependsOn() {
        return (dependsOn != null && dependsOn.length > 0);
    }
}
