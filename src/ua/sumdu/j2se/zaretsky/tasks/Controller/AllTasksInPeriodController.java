package ua.sumdu.j2se.zaretsky.tasks.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import ua.sumdu.j2se.zaretsky.tasks.MainApp;
import ua.sumdu.j2se.zaretsky.tasks.Model.Task;
import ua.sumdu.j2se.zaretsky.tasks.Model.Tasks;

import java.util.*;


/**
 * Created by Nikolion on 12.01.2017.
 */
public class AllTasksInPeriodController {

    @FXML
    TableView<Map.Entry<Date, Set<Task>>> allTasksInPeriodTable;
    @FXML
    TableColumn<Map.Entry<Date, Set<Task>>, String> dateColumn;
    @FXML
    TableColumn<Map.Entry<Date, Set<Task>>, String> taskTitleColumn;

    private Stage dialogStage;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setDates(Date startPeriod, Date endPeriod) {
        SortedMap<Date, Set<Task>> tasks = Tasks.calendar(MainApp.getTasks(),
                startPeriod, endPeriod);

        dateColumn.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<Map.Entry<Date, Set<Task>>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Date, Set<Task>>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey().toString());
            }
        });


        taskTitleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Date, Set<Task>>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Date, Set<Task>>, String> p) {
                Iterator a = p.getValue().getValue().iterator();
                String result = "";
                while (a.hasNext()) {
                    result += ((Task) a.next()).getTitle() + "\n";
                }
                return new SimpleStringProperty(result);
            }
        });


        ObservableList<Map.Entry<Date, Set<Task>>> items = FXCollections
                .observableArrayList(tasks.entrySet());
        allTasksInPeriodTable.setItems(items);

        allTasksInPeriodTable.getColumns().setAll(dateColumn, taskTitleColumn);

    }


}

