package ua.sumdu.j2se.zaretsky.tasks.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ua.sumdu.j2se.zaretsky.tasks.Model.Task;

/**
 * Created by Nikolion on 12.01.2017.
 */
public class MessageController {
    @FXML
    Label msgLabel;
    @FXML
    Button msgBtn;
    private Stage dialogStage;
    private Task task;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
