package ua.sumdu.j2se.zaretsky.tasks.Model;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ua.sumdu.j2se.zaretsky.tasks.MainApp;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikolion on 10.01.2017.
 */
public class Detector extends Thread {

    MainApp mainApp;
    private TaskList tasks;
    private long notifyPeriodInMillis; //период в течение которого ищем таски
    public static final int PAUSE = 10000;//10 seconds

    public Detector(TaskList tasks, long notifyPeriodInMillis, long intervalOfNotifying, MainApp mainApp) {
        this.tasks = tasks;
        this.notifyPeriodInMillis = notifyPeriodInMillis; //1 day

        this.mainApp = mainApp;
    }

    /**
     * The action to be performed by this timer task.
     * Displays all active tasks that are going to be executed during incoming period.
     */
    @Override
    public void run() {
        while (!mainApp.isExit()) {
            long currentTime = new Date().getTime();

            TaskList incomingTasks = (TaskList) Tasks.incoming(tasks, new Date(currentTime), new Date(currentTime + notifyPeriodInMillis));
            System.out.println(incomingTasks.toString());
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


    *//*@Override
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
    }*//*

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
