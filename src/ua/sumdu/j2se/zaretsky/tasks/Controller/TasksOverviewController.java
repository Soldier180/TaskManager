package ua.sumdu.j2se.zaretsky.tasks.Controller;

/**
 * Created by Nikolion on 07.01.2017.
 */


import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.sumdu.j2se.zaretsky.tasks.MainApp;
import ua.sumdu.j2se.zaretsky.tasks.Model.*;
import ua.sumdu.j2se.zaretsky.tasks.Util.DateUtil;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;


public class TasksOverviewController {
    @FXML
    private TableView<Task> tasksTable;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, LocalDate> startTimeColumn;
    @FXML
    private TableColumn<Task, LocalDate> endTimeColumn;

    @FXML
    private Label titleLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label endTimeLabel;
    @FXML
    private Label repeatLabel;
    @FXML
    private Label activityLabel;
    @FXML
    private Label titleTask;

    @FXML
    Button showBtn;


    @FXML
    DatePicker allTaskStartDatePiker;

    @FXML
    DatePicker allTaskEndDatePiker;
    // Ссылка на главное приложение.
    private MainApp mainApp;
    //private Service<Void> backgroundThread;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public TasksOverviewController() {

    }

    private final Logger log = LogManager.getLogger(TasksOverviewController.class.getSimpleName());

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        Locale.setDefault(Locale.ENGLISH);
        allTaskStartDatePiker.setValue(LocalDate.now());
        allTaskEndDatePiker.setValue(allTaskStartDatePiker.getValue()
                .plusDays(7));
        // Инициализация таблицы адресатов с двумя столбцами.
        titleColumn.setCellValueFactory(new PropertyValueFactory<Task, String>
                ("title"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>
                ("StartTime"));

        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>
                ("EndTime"));
        showTaskDetails(null);

        tasksTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));
    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        tasksTable.setItems(mainApp.getTasksData());
    }

    /**
     * Заполняет все текстовые поля.
     * Если задача = null, то все текстовые поля очищаются.
     *
     * @param task — задача типа Task или null
     */
    private void showTaskDetails(Task task) {
        if (task != null) {
            // Заполняем метки информацией из объекта task.
            titleLabel.setText(task.getTitle());
            startTimeLabel.setText(DateUtil.format(task.getStartTime()));

            repeatLabel.setText(DateUtil.secondsToStringTime(task.getRepeatInterval
                    ()));
            if (task.getRepeatInterval() != 0) {
                endTimeLabel.setText(DateUtil.format(task.getEndTime()));
            } else {
                endTimeLabel.setText("");
            }


            activityLabel.setText(Boolean.toString(task.isActive()));


        } else {
            // Если task = null, то убираем весь текст.
            titleLabel.setText("");
            startTimeLabel.setText("");
            endTimeLabel.setText("");
            repeatLabel.setText("");
            activityLabel.setText("");
        }
    }

    @FXML
    public void deleteTask(ActionEvent actionEvent) {
        int selectedIndex = tasksTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Delete task");
            alert.setHeaderText("Are you sure you want delete the task?");
            alert.setContentText("Please confirm your choice");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Task tempTask = tasksTable.getItems().get(selectedIndex);
                String msg = TaskIO.writeTask(tempTask);
                MainApp.getTasks().remove(tempTask);
                tasksTable.getItems().remove(selectedIndex);
                log.info("DELETE task: " + msg);

            }

        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No TASK Selected");
            alert.setContentText("Please select a TASK in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке New...
     */
    @FXML
    private void handleNewTask() {
        Task tempTask = new Task("default", new Date());
        boolean okClicked = mainApp.showTaskEditDialog(tempTask, true);
        if (okClicked) {
            mainApp.getTasksData().add(tempTask);
            MainApp.getTasks().add(tempTask);
            showTaskDetails(tempTask);
            log.info("CREATE task: " + TaskIO.writeTask(tempTask));
        }
    }

    @FXML
    private void handleShowAllTasksInPeriod() {

        Date startPeriod = DateUtil.localDateToDate(allTaskStartDatePiker
                .getValue().atTime(0, 0, 0));
        Date endPeriod = DateUtil.localDateToDate(allTaskEndDatePiker
                .getValue().atTime(23, 59, 59));
        if (startPeriod != null && endPeriod != null && endPeriod.compareTo
                (startPeriod) > 0) {


           mainApp.showAllTasksInPeriod(startPeriod, endPeriod);
        }

    }

    /**
     * Вызывается, когда пользователь кликает по кнопка Edit...
     * Открывает диалоговое окно для изменения выбранной задачи.
     */
    @FXML
    private void handleEditTask()  {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            String oldTask = TaskIO.writeTask(selectedTask);
            boolean okClicked = mainApp.showTaskEditDialog(selectedTask, false);
            if (okClicked) {
                tasksTable.getColumns().get(0).setVisible(false);
                tasksTable.getColumns().get(0).setVisible(true);
                showTaskDetails(selectedTask);
                String newTask = TaskIO.writeTask(selectedTask);
                if (!oldTask.equals(newTask)) {
                    log.info("EDIT task: " + oldTask + " TO " + newTask);
                }
            }

        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Task Selected");
            alert.setContentText("Please select a task in the table.");

            alert.showAndWait();
        }
    }
}


 /*   Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <1 ; i++) {

                }{
                    long currentTime = new Date().getTime();
                    TaskList schedule = (ArrayTaskList) Tasks
                            .incoming(mainApp.tasks, new Date
                                    (currentTime), new Date
                                    (currentTime + 300000));

                    for (Task j:schedule) {
                        titleTask.setText(j.getTitle());
                        Alert t = new Alert(Alert.AlertType.INFORMATION);
                        t.initOwner(mainApp.getPrimaryStage());
                        t.setTitle(j.getTitle());
                        t.show();
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });*/