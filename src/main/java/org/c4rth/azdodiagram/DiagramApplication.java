package org.c4rth.azdodiagram;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.c4rth.azdodiagram.model.Pipeline;
import org.c4rth.azdodiagram.mxgraph.Diagram;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class DiagramApplication {

    public static void main(String[] args) {
        new DiagramApplication().run(args);
    }

    @SneakyThrows
    public void run(String... args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("You need to specify yaml file path");
        }
        String yamlFilename = args[0];
        readYaml(yamlFilename);
    }

    @SneakyThrows
    private void readYaml(String yamlFilename) {
        assert Files.exists(Path.of(yamlFilename));
        log.info("Load yaml from {}", yamlFilename);
        DumperOptions dumperOptions = new DumperOptions();
        Representer representer = new Representer(dumperOptions);
        representer.getPropertyUtils().setSkipMissingProperties(true);
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(true);
        Yaml yaml = new Yaml(new Constructor(Pipeline.class, options), representer, dumperOptions);
        try (InputStream inputStream = new FileInputStream(yamlFilename)) {
            log.info("Convert to AzDo pipeline");
            Pipeline pipeline = yaml.loadAs(inputStream, Pipeline.class);
            pipeline.setYamlFileName(yamlFilename);
            //log.info(pipeline.toString());
            generateDiagram(pipeline);
            log.info("done");
        }
    }

    @SneakyThrows
    private void generateDiagram(Pipeline pipeline) {
        Diagram diagram = new Diagram(pipeline);
        log.info("Generate diagram");
        diagram.generate();
        log.info("Save diagram");
        diagram.save();
    }

}