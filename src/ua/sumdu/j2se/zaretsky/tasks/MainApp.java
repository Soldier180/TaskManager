package ua.sumdu.j2se.zaretsky.tasks;/**
 * Created by Nikolion on 06.01.2017.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;

import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.sumdu.j2se.zaretsky.tasks.Controller.AllTasksInPeriodController;
import ua.sumdu.j2se.zaretsky.tasks.Controller.TaskEditDialogController;
import ua.sumdu.j2se.zaretsky.tasks.Controller.TasksOverviewController;
import ua.sumdu.j2se.zaretsky.tasks.Model.*;
import ua.sumdu.j2se.zaretsky.tasks.Model.Task;

import java.io.File;
import java.io.IOException;
import java.util.Date;


//import static ua.sumdu.j2se.zaretsky.tasks.Model.Detector.PAUSE;

public class MainApp extends Application {
    private final Logger log = LogManager.getLogger(MainApp.class.getSimpleName());
    private static TaskList tasks = new LinkedTaskList();
    public static final File FILE = new File("resources/tasks");
    Detector detector;
    private boolean exit = false;
    private ObservableList<Task> tasksData = FXCollections
            .observableArrayList();


    public boolean isExit() {
        return exit;
    }

    public static TaskList getTasks() {
        return tasks;
    }

    public ObservableList<Task> getTasksData() {
        return tasksData;
    }


    private Stage primaryStage;
    private VBox rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image
                ("file:resources/image/task_manager1.png"));
        this.primaryStage.setMinWidth(730);
        this.primaryStage.setMinHeight(500);
        this.primaryStage.setTitle("TASK MANAGER");
        log.info("Open program");

        initRootLayout();

        detector = new Detector(tasks,600000,this);
        detector.start();
        //при закрытии программы записиваем все задачи в файл
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                writeInFile();
                exit = true;
                // System.out.println("Stage is closing");
                log.info("Program close");
                detector.stop();//close thread with notify

            }
        });
    }

    public MainApp() {
        try {
            TaskIO.readBinary(tasks, FILE);

            tasksData.clear();
            //add tasks in ObservableList
            for (Task t : tasks) {
                tasksData.add(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.catching(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.catching(e);
        }


    }


    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TasksOverview" +
                    ".fxml"));
            rootLayout = loader.load();


            TasksOverviewController controller = loader.getController();
            controller.setMainApp(this);

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            log.catching(e);
        }

    }


    /**
     * Открывает диалоговое окно для изменения деталей задачи.
     * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
     * объекте и возвращается значение true.
     *
     * @param task - объект адресата, который надо изменить
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public boolean showTaskEditDialog(Task task, boolean newTask) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TaskEditDialog.fxml"));
            GridPane page = (GridPane) loader.load();

            Stage dialogStage = new Stage();
            if (newTask) {
                dialogStage.setTitle("Create Task");
            } else {
                dialogStage.setTitle("Edit Task");
            }

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);


            TaskEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            if (newTask) {
                controller.setNewTask(task);
            } else {
                controller.setTask(task);
            }

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            log.catching(e);
            return false;
        }
    }


    public void showAllTasksInPeriod(Date startPeriod, Date endPeriod) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AllTasksInPeriod.fxml"));
            HBox page = (HBox) loader.load();

            Stage dialogStage = new Stage();

            dialogStage.setTitle("All task in period");
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём задачу в контроллер.
            AllTasksInPeriodController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDates(startPeriod,endPeriod);
            //controller.setData(startPeriod, endPeriod);


            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Возвращает главную сцену.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


    private void writeInFile() {
        try {
            TaskIO.writeBinary(tasks, FILE);
        } catch (IOException e) {
            e.printStackTrace();
            log.catching(e);
        }
    }

    public static void main(String[] args) {

        launch(args);


    }
}



/*javafx.concurrent.Task task = new javafx.concurrent.Task<Void>() {
            @Override public Void call() {
                final int max = 1000000;
                for (int i=1; i<=max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, max);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        ProgressBar bar = new ProgressBar();
        bar.progressProperty().bind(task.progressProperty());
        Scene barr = new Scene(bar);
        primaryStage.setScene(barr);

        new Thread(task).start();*/

        /*final Task<Void> task = new Task<Void>() {
            ..
        };
        scene.cursorProperty().bind(Bindings.when(task.runningProperty())
                .then(CURSOR_WAIT).otherwise(CURSOR_DEFAULT));
        new Thread(task).start();

        final javafx.concurrent.Task task = new javafx.concurrent.Task<Alert>() {
            @Override protected Alert call() throws InterruptedException {
                //updateMessage("Finding friends . . .");
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(200);
                    updateProgress(i+1, 10);


                a.setTitle("gchjcg");}
                //updateMessage("Finished.");
                return a;
            }
        };

        new Thread(task).start();*/


        /*while (!isExit()) {
            long currentTime = new Date().getTime();
            TaskList schedule = (ArrayTaskList) Tasks.incoming(tasks, new Date(currentTime), new Date
                    (currentTime + 300000));
            showMessage(schedule);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No TASK Selected");
        alert.setContentText("Please select a TASK in the table.");

        alert.show();*/

// showTaskOverview();

//detector = new Detector(tasks,300000, 5000, this);

  /*  public void startTask() {
        Runnable task = new Runnable() {
            public void run() {
                runTask();
            }
        };


        // Run the task in a background thread

        Thread backgroundThread = new Thread(task);

        // Terminate the running thread if the application exits

        backgroundThread.setDaemon(true);

        // Start the thread

        backgroundThread.start();
    }
*/

   /* public void runTask() {

        for (int i = 1; i <= 10; i++)

        {
            try {
                String status = "Processing " + i + " of " + 10;

                System.out.println(status);

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(getPrimaryStage());
                alert.setTitle("Attention");
                alert.setHeaderText("Task will start in the short run");

                alert.showAndWait();
                //textArea.appendText(status + "\n");

                Thread.sleep(1000);

            } catch (InterruptedException e)

            {
                e.printStackTrace();

            }

        }

    }*/