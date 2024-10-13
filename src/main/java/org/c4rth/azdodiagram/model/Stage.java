package org.c4rth.azdodiagram.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Stage {
    private String stage;
    private String displayName;
    private String[] dependsOn;
    private Job[] jobs;

    public String getCellName() {
        return displayName == null ? stage : displayName;
    }

    public boolean hasDependsOn() {
        return (dependsOn != null && dependsOn.length > 0);
    }
}
