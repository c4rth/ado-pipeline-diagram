package org.c4rth.azdodiagram.mxgraph;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import org.c4rth.azdodiagram.model.Job;
import org.c4rth.azdodiagram.model.Pipeline;
import org.c4rth.azdodiagram.model.Stage;
import org.c4rth.azdodiagram.model.Task;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class Diagram {

    public static final double CELL_MARGIN = 10;
    public static final double CELL_TOP = 50;
    public static final double CELL_TASK_WIDTH = 200;
    public static final double CELL_TASK_HEIGHT = 50;
    public static final double CELL_JOB_WIDTH = CELL_TASK_WIDTH + 2 * CELL_MARGIN;
    public static final double CELL_STAGE_WIDTH = CELL_JOB_WIDTH + 2 * CELL_MARGIN;

    private final mxGraph drawIoDiagram;
    private final Pipeline pipeline;

    public Diagram(Pipeline pipeline) {
        this.pipeline = pipeline;
        drawIoDiagram = new mxGraph();
        drawIoDiagram.setExtendParents(false);
        drawIoDiagram.setExtendParentsOnAdd(false);
        drawIoDiagram.setConstrainChildren(false);
    }

    public void generate() {
        drawIoDiagram.getModel().beginUpdate();
        try {
            Object root = drawIoDiagram.getModel().getParent(null);
            double x = CELL_MARGIN;
            Map<String, mxCell> dependsStage = new HashMap<>();
            mxCell previousCellStage = null;
            for (Stage stage : pipeline.getStages()) {
                mxCell cellStage = CellHelper.getCellStage(stage, x, CELL_MARGIN);
                dependsStage.put(stage.getStage(), cellStage);
                double jobY = addJobs(stage, cellStage);
                cellStage.getGeometry().setHeight(jobY - CELL_MARGIN);
                drawIoDiagram.addCell(cellStage, root);
                // depends : arrows
                if (!stage.hasDependsOn()) {
                    if (previousCellStage != null) {
                        drawIoDiagram.insertEdge(root, null, null, previousCellStage, cellStage, "edgeStyle=orthogonalEdgeStyle;");
                    }
                } else {
                    for (String dependOn : stage.getDependsOn()) {
                        drawIoDiagram.insertEdge(root, null, null, dependsStage.get(dependOn), cellStage, "edgeStyle=orthogonalEdgeStyle;");
                    }
                }
                previousCellStage = cellStage;
                x += CELL_STAGE_WIDTH + CELL_MARGIN * 5;
            }
        } finally {
            drawIoDiagram.getModel().endUpdate();
        }
    }

    public void save() throws IOException {
        mxCodec codec = new mxCodec();
        Element node = (Element) codec.encode(drawIoDiagram.getModel());
        node.setAttribute("grid", "1");
        node.setAttribute("gridSize", "10");
        node.setAttribute("guides", "1");
        node.setAttribute("shadow", "0");
        node.setAttribute("pageScale", "1");
        node.setAttribute("pageSize", "1");
        String xml = mxUtils.getPrettyXml(node);
        Files.writeString(Path.of(pipeline.getYamlFileName() + ".drawio"), xml, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    private double addJobs(Stage stage, mxCell cellStage) {
        double jobY = CELL_TOP;
        mxCell previouseCellJob = null;
        Map<String, mxCell> dependsJob = new HashMap<>();
        for (Job job : stage.getJobs()) {
            mxCell cellJob = CellHelper.getCellJob(job, CELL_MARGIN, jobY);
            dependsJob.put(job.getJob(), cellJob);
            double heightTasks = addTasks(job, cellJob);
            cellJob.getGeometry().setHeight(heightTasks);
            drawIoDiagram.addCell(cellJob, cellStage);
            jobY += cellJob.getGeometry().getHeight() + CELL_MARGIN * 2;
            // depends : arrows
            if (!job.hasDependsOn()) {
                if (previouseCellJob != null) {
                    drawIoDiagram.insertEdge(cellStage, null, null, previouseCellJob, cellJob);
                }
            } else {
                for (String dependOn : job.getDependsOn()) {
                    drawIoDiagram.insertEdge(cellStage, null, null, dependsJob.get(dependOn), cellJob, "edgeStyle=orthogonalEdgeStyle;");
                }
            }
            previouseCellJob = cellJob;
        }
        return jobY;
    }

    private double addTasks(Job job, mxCell cellJob) {
        double taskY = CELL_TOP;
        mxCell previouseCellTask = null;
        for (Task task : job.getSteps()) {
            mxCell cellTask = CellHelper.getCellTask(task, CELL_MARGIN, taskY);
            drawIoDiagram.addCell(cellTask, cellJob);
            taskY += CELL_TASK_HEIGHT + CELL_MARGIN;
            // arrows
            if (previouseCellTask != null) {
                drawIoDiagram.insertEdge(cellJob, null, null, previouseCellTask, cellTask);
            }
            previouseCellTask = cellTask;
        }
        return taskY;
    }

}
