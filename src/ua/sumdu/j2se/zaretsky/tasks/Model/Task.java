package ua.sumdu.j2se.zaretsky.tasks.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.*;
import java.util.Locale;

/**
 * Class for creating and editing tasks
 *
 * @author Nikolay Zaretsky
 * @version 1.0 04 Oct 2016
 */
public class Task implements Cloneable, Serializable {

    private String title;
    private boolean active;
    private Dates start;
    private Dates end;
    private int repeat;
    public static final Date BEGIN = new Date(0);


    /**
     * Constructor for a one-time task
     *
     * @param title It takes the value of the task name
     * @param time  It takes the value of the task time
     */
    public Task(String title, Date time) throws IllegalArgumentException {
        if (title == null || title.isEmpty() || title.matches("\\s+")) {
            throw new IllegalArgumentException("Error. Incorrect title. Task not created");
        } else if (time.before(BEGIN)) {
            throw new IllegalArgumentException("Incorrect value time. Task not created");
        } else {
            setTitle(title);
            setTime(time);
            setActive(false);
        }
    }

    /**
     * Constructor for a repeated task
     *
     * @param title    It takes the value of the task name
     * @param start    It takes the value of the task start time
     * @param end      It takes the value of the task end time
     * @param interval It takes the value of the task repeat time
     */
    public Task(String title, Date start, Date end, int interval) throws
            IllegalArgumentException {
        if (title == null || title.isEmpty() || title.matches("\\s+")) {
            throw new IllegalArgumentException("Error. Incorrect title. Task not created");
        } else if (start.before(BEGIN) || end.before(BEGIN)
                || interval <= 0) {
            throw new IllegalArgumentException("Some argument of method setTime(int start, int end, int interval) is wrong. Time not change");
        } else if (start.after(end)) {
            throw new IllegalArgumentException("Start time task greater the end time. Task not created");
        } else {
            setTitle(title);
            setTime(start, end, interval);
            setActive(false);
        }
    }

    /**
     * Method for getting the title of the task
     *
     * @return title
     */
    public synchronized String getTitle() {
        return title;
    }

    /**
     * Method for setting the title of the task
     *
     * @param title Title of task
     */
    public void setTitle(String title) throws IllegalArgumentException {

        if (title == null || title.isEmpty() || title.matches("\\s+"))
            throw new IllegalArgumentException("Invalid value title");
        {
            this.title = title;
        }
    }

    /**
     * Method for getting task status
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method for setting task status
     *
     * @param active Set the task active or inactive
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Method to getting time not repeated task
     *
     * @return start
     */

    public Date getTime() {
        return start;
    }

    /**
     * Method to setting start time, end time and repeat time repeated task
     *
     * @param start    Start time task
     * @param end      End time task
     * @param interval Repeat time task
     */
    public void setTime(Date start, Date end, int interval) throws
            IllegalArgumentException {


        if (start.before(BEGIN) || end.before(BEGIN) ||
                interval < 0) {
            throw new IllegalArgumentException("Some argument of method setTime(int start, int end, int interval) is wrong. Time not change");
        } else if (start.after(end)) {
            throw new IllegalArgumentException("Start time task greater the end time. Time not change");
        } else {
            this.start = new Dates(start.getTime());
            this.end = new Dates(end.getTime());
            this.repeat = interval;
        }
    }

    /**
     * Method to setting time not repeated task
     *
     * @param time Task time
     */
    public void setTime(Date time) throws IllegalArgumentException {


        if (time.compareTo(BEGIN) != -1) {
            this.start = new Dates(time.getTime());
            this.end = new Dates(time.getTime());
            this.repeat = 0;

        } else {
            throw new IllegalArgumentException("Illegal argument time. Time not change");
        }

    }

    /**
     * Method to getting start time task
     *
     * @return start
     */
    public Dates getStartTime() {
        return start;
    }

   /*public String getFormatStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM 'at' HH:mm", Locale.ENGLISH);

        return dateFormat.format(start);
    }*/
    /**
     * Method to getting end time task
     *
     * @return end
     */
    public Dates getEndTime() {
        return end;
    }

    /**
     * Method to getting repeat time task
     *
     * @return repeat
     */
    public int getRepeatInterval() {
        return isRepeated() ? repeat : 0;
    }

    /**
     * Method to check the repeatability task
     *
     * @return boolean
     */
    public boolean isRepeated() {
        return repeat != 0;
    }

    /**
     * A method that returns the next time recurrence of task
     *
     * @param currentDate Time to check
     * @return nextTimeRepeat
     */

    public Date nextTimeAfter(Date currentDate) throws
            IllegalArgumentException {
        Dates current = new Dates(currentDate.getTime());
        if (current.before(BEGIN)) {
            throw new IllegalArgumentException("Incorrect param for time");
        }

        if (isActive()) {
            if (isRepeated()) {
                for (long tempTime = getStartTime().getTime(); tempTime <= getEndTime().getTime();
                     tempTime += getRepeatInterval() * 1000) {
                    if (tempTime > current.getTime()) {
                        return new Date(tempTime);
                    }
                }
            } else {
                if (getStartTime().after(current)) {
                    return getStartTime();
                }
            }
        }
        return null;
    }

    /**
     * A method  returns a description of the task
     *
     * @return "Task" + title + "is inactive", if the task is not active.
     * <p>"Task " + title + " at " + time, if the task is active and one-time</p>
     * <p>"Task \"" + getTitle() + "\" from " + getStartTime() + " to "
     * + getEndTime() + " every " + repeat + " seconds", if the task is active and repeated</p>
     */
    @Override
    public String toString() {

        if (!isActive() && !isRepeated()) {
            return ("Task \"" + getTitle() + "\" is inactive");
        } else if (!isActive() && isRepeated()) {
            return ("Task \"" + getTitle() + "\" from " + getStartTime() + " to " + getEndTime()
                    + " every " + repeat + " seconds" + " is inactive");
        } else if (isActive() && isRepeated()) {
            return ("Task \"" + getTitle() + "\" from " + getStartTime() + " to " + getEndTime()
                    + " every " + repeat + " seconds");
        } else {
            return ("Task \"" + getTitle() + "\" at " + getStartTime());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Task)) return false;

        Task task = (Task) o;

        if (active != task.active) return false;
        if (start.compareTo(task.start) != 0) return false;
        if (end.compareTo(task.end) != 0) return false;
        if (repeat != task.repeat) return false;
        return title != null ? title.equals(task.title) : task.title == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + repeat;
        return result;
    }

    @Override
    public final Task clone() throws CloneNotSupportedException {
        Task cloneOfTask = (Task) super.clone();
        cloneOfTask.setTime(new Date(this.getStartTime().getTime()), new Date(this
                .getEndTime().getTime()),this.repeat);

        return cloneOfTask;
    }


}
