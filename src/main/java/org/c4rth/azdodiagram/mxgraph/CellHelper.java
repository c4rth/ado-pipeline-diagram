package org.c4rth.azdodiagram.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import org.apache.commons.lang3.StringUtils;
import org.c4rth.azdodiagram.model.Job;
import org.c4rth.azdodiagram.model.Stage;
import org.c4rth.azdodiagram.model.Task;

import static org.c4rth.azdodiagram.mxgraph.Diagram.*;

public class CellHelper {

    public static mxCell getCellStage(Stage stage, double x, double y) {
        mxCell cell = new mxCell();
        cell.setVertex(true);
        mxGeometry geometry = new mxGeometry(x, y, CELL_STAGE_WIDTH, 100);
        cell.setGeometry(geometry);
        cell.setValue(stage.getCellName());
        cell.setStyle("rounded=0;whiteSpace=wrap;html=1;verticalAlign=top;fillColor=#dae8fc;strokeColor=#6c8ebf;fontSize=17;");
        return cell;
    }

    public static mxCell getCellJob(Job job, double x, double y) {
        mxCell cell = new mxCell();
        cell.setVertex(true);
        mxGeometry geometry = new mxGeometry(x, y, CELL_JOB_WIDTH, 100);
        cell.setGeometry(geometry);
        cell.setValue(job.getCellName());
        cell.setStyle("rounded=0;whiteSpace=wrap;html=1;verticalAlign=top;fillColor=#ffe6cc;strokeColor=#d79b00;fontSize=15;");
        return cell;
    }

    public static mxCell getCellTask(Task task, double x, double y) {
        mxCell cell = new mxCell();
        cell.setVertex(true);
        mxGeometry geometry = new mxGeometry(x, y, CELL_TASK_WIDTH, CELL_TASK_HEIGHT);
        cell.setGeometry(geometry);
        cell.setValue(StringUtils.abbreviate(task.getCellName(), 60));
        cell.setStyle("rounded=0;whiteSpace=wrap;html=1;fillColor=#e1d5e7;strokeColor=#9673a6;fontSize=12;");
        return cell;
    }
}
