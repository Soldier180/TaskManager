package ua.sumdu.j2se.zaretsky.tasks.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ua.sumdu.j2se.zaretsky.tasks.MainApp;
import ua.sumdu.j2se.zaretsky.tasks.Model.Task;
import ua.sumdu.j2se.zaretsky.tasks.Util.DateUtil;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nikolion on 07.01.2017.
 */
public class TaskEditDialogController {

    @FXML
    TextField textFieldTitle;
    @FXML
    DatePicker datePikedDateStart;
    @FXML
    ChoiceBox<String> choiseBoxHoursStart;
    @FXML
    ChoiceBox<String> choiseBoxMinutesStart;
    @FXML
    TextField repeatTime;
    @FXML
    DatePicker datePikedDateEnd;
    @FXML
    ChoiceBox<String> choiseBoxHoursEnd;
    @FXML
    ChoiceBox<String> choiseBoxMinutesEnd;
    @FXML
    CheckBox chkBoxActive;

    @FXML
    Button btnNewTaskOk;

    private Stage dialogStage;
    private Task task;
    private boolean okClicked = false;


    public Task getTask() {
        return task;
    }

    static final List<String> hours = new ArrayList<>();
    static final List<String> minutes = new ArrayList<>();


    @FXML
    private void initialize() {
        Locale.setDefault(Locale.ENGLISH);
        if (hours.isEmpty()) {
            for (int i = 0; i < 24; i++) {
                if (i < 10) {
                    hours.add("0" + i);
                } else
                    hours.add("" + i);
            }
        }

        if (minutes.isEmpty()) {
            for (int i = 0; i < 60; i++) {

                if (i < 10) {
                    minutes.add("0" + i);
                } else
                    minutes.add("" + i);
            }
        }

        choiseBoxHoursStart.setItems(FXCollections.observableArrayList(hours));
        choiseBoxMinutesStart.setItems(FXCollections.observableArrayList(minutes));

        choiseBoxHoursEnd.setItems(FXCollections.observableArrayList(hours));
        choiseBoxMinutesEnd.setItems(FXCollections.observableArrayList(minutes));
    }

    public void setTask(Task task) {
        this.task = task;

        textFieldTitle.setText(task.getTitle());
        chkBoxActive.setSelected(task.isActive());

        datePikedDateStart.setValue(task.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        choiseBoxHoursStart.setValue(DateUtil.choiceBoxTime(task.getStartTime().getHours()));
        choiseBoxMinutesStart.setValue(DateUtil.choiceBoxTime(task.getStartTime().getMinutes()));

        if (task.isRepeated()) {
            datePikedDateEnd.setValue(task.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            choiseBoxHoursEnd.setValue(DateUtil.choiceBoxTime(task.getEndTime().getHours()));
            choiseBoxMinutesEnd.setValue(DateUtil.choiceBoxTime(task.getEndTime().getMinutes()));

            repeatTime.setText(DateUtil.secondsToStringTime(task.getRepeatInterval()));
        } else {
            choiseBoxHoursEnd.setValue("00");
            choiseBoxMinutesEnd.setValue("00");
        }

    }


    public void setNewTask(Task task) {
        this.task = task;

        textFieldTitle.setText("");
        chkBoxActive.setSelected(false);

        datePikedDateStart.setValue(null);
        choiseBoxHoursStart.setValue("00");
        choiseBoxMinutesStart.setValue("00");

        datePikedDateEnd.setValue(null);
        choiseBoxHoursEnd.setValue("00");
        choiseBoxMinutesEnd.setValue("00");

        repeatTime.setText("");


    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk() throws ParseException {
        if (isInputValid()) {
            int repeatInterval;
            task.setTitle(textFieldTitle.getText());
            task.setActive(chkBoxActive.isSelected());

            //----------------------------------
            LocalDate localDate1 = datePikedDateStart.getValue();
            Instant instant1 = Instant.from(localDate1.atStartOfDay(ZoneId
                    .systemDefault()));
            Long d1 = Date.from(instant1).getTime();

            Integer h1 = Integer.parseInt(choiseBoxHoursStart.getValue()) *
                    60 * 60 * 1000;
            Integer m1 = Integer.parseInt(choiseBoxMinutesStart.getValue()) *
                    60 * 1000;

            Date dateStart = new Date(d1 + h1 + m1);//Get start time
            //--------------------------------


            repeatInterval = DateUtil.parseInterval(repeatTime.getText());
            if (repeatInterval > 0) {

                LocalDate localDate2 = datePikedDateEnd.getValue();
                Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId
                        .systemDefault()));
                Long d2 = Date.from(instant2).getTime();

                Integer h2 = Integer.parseInt(choiseBoxHoursEnd.getValue()) *
                        60 * 60 * 1000;
                Integer m2 = Integer.parseInt(choiseBoxMinutesEnd.getValue()) *
                        60 * 1000;

                Date dateEnd = new Date(d2 + h2 + m2);//Get  end time
                //-------------------------------------

                task.setTime(dateStart, dateEnd, repeatInterval);
            } else {
                task.setTime(dateStart);
            }


            okClicked = true;
            dialogStage.close();
        }

    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Проверяет пользовательский ввод
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";
        int rTime = 0;
        LocalDate start = null;

        try {
            if (textFieldTitle.getText() == null || textFieldTitle.getText().isEmpty() ||
                    textFieldTitle.getText().matches("\\s+")) {
                throw new IllegalArgumentException("No valid title!");
            }
            if (datePikedDateStart.getValue() == null || datePikedDateStart
                    .getValue().isBefore(DateUtil.dateToLaocalDate(Task.BEGIN))) {

                throw new IllegalArgumentException("Incorrect start date! Maybe " +
                        "you not choice date or date is less 1970.01.01");
            } else {
                start = datePikedDateStart.getValue();
            }
            try {
                rTime = DateUtil.parseInterval(repeatTime.getText());

            } catch (ParseException e) {
                throw new IllegalArgumentException("Incorrect repeat time!");
            } catch (IllegalArgumentException a) {
                throw new IllegalArgumentException(a.getMessage());
            }
            if (rTime > 0) {
                if (datePikedDateEnd.getValue() == null || datePikedDateEnd
                        .getValue().isBefore(DateUtil.dateToLaocalDate(Task.BEGIN))) {

                    throw new IllegalArgumentException("Incorrect end date! " +
                            "Maybe " +
                            "you not choice date or date is less 1970.01.01");
                }
                if (datePikedDateEnd.getValue().isBefore(start)) {
                    throw new IllegalArgumentException("Incorrect end date! End date is less " +
                            "then start date!");
                }
            }

            return true;
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(e.getMessage());

            alert.showAndWait();

            return false;
        }

    }

}
