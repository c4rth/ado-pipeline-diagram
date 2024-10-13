package org.c4rth.azdodiagram.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pipeline {

    private Stage[] stages;

    private String yamlFileName;

}
