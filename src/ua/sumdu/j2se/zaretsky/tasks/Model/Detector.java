package ua.sumdu.j2se.zaretsky.tasks.Model;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ua.sumdu.j2se.zaretsky.tasks.MainApp;
import ua.sumdu.j2se.zaretsky.tasks.Util.DateUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikolion on 10.01.2017.
 */
public class Detector extends Thread {

    MainApp mainApp;
    private TaskList tasks;
    private long notifyPeriodInMillis; //period when find tasks 10 minutes
    public static final int PAUSE = 300000;//5 minutes

    public Detector(TaskList tasks, long notifyPeriodInMillis, MainApp mainApp) {
        this.tasks = tasks;
        this.notifyPeriodInMillis = notifyPeriodInMillis;
        this.mainApp = mainApp;
    }

    @Override
    public void run() {
        while (!mainApp.isExit()) {

            Date currentTime = new Date();
            TaskList incomingTasks = (TaskList) Tasks.incoming(tasks, currentTime, new
                    Date(currentTime.getTime() + notifyPeriodInMillis));
            if (incomingTasks.count != 0) {
                System.out.println("Nearest tasks");
                for (Task t : incomingTasks) {
                    System.out.print("Time: " + DateUtil.format(t.nextTimeAfter(currentTime)));
                    System.out.println(" " + t.getTitle());
                }
            }
            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /*private Iterable<Task> tasks;

    public static final int PAUSE = 10000;//10 seconds
    private long timeToTaskStart;
    private MainApp mainApp;

    public Detector(Iterable<Task> tasks, long timeToTaskStart, MainApp mainApp) {
        this.tasks = tasks;
        this.timeToTaskStart = timeToTaskStart;
        this.mainApp = mainApp;

    }

    @Override
    public void run() {
        while (!mainApp.isExit()) {
            long currentTime = new Date().getTime();
            TaskList schedule = (ArrayTaskList) Tasks.incoming(tasks, new Date(currentTime), new Date
                    (currentTime + timeToTaskStart));
            mainApp.showMessage(schedule);
            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
}
