package ua.sumdu.j2se.zaretsky.tasks.Model;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.*;

import static ua.sumdu.j2se.zaretsky.tasks.Model.Task.BEGIN;


/**
 * Created by Nikolion on 22.11.2016.
 */
public class Tasks {


    /**
     * Method to receive all the tasks what will be executed at least once in some period
     *
     * @param from
     * @param to
     * @return ArrayTaskList with tasks
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date from,
                                          Date to) throws IllegalArgumentException {

        if (from.before(BEGIN) || to.before(BEGIN) || from.after(to)) {
            throw new IllegalArgumentException("Incorrect param from:" + from +
                    " or to:" + to);
        } else {
            TaskList list = new ArrayTaskList();
            for (Task task : tasks) {

                if (task.nextTimeAfter(from) != null && task.nextTimeAfter(from).compareTo(to) != 1) {
                    list.add(task);
                }
            }
            return list;
        }
    }


    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date
            start, Date end) {
        SortedMap<Date, Set<Task>> dateSetSortedMap = new TreeMap<>(new
                CompareByDate().reversed());
        TreeMap<Date, Task> tempResult;
        for (Task task : tasks) {
            tempResult = allTasksInInterval(task, start, end);
            for (Map.Entry<Date, Task> taskIter : tempResult.entrySet()) {
                if (dateSetSortedMap.containsKey(taskIter.getKey())) {
                    dateSetSortedMap.get(taskIter.getKey()).add(taskIter.getValue());
                } else {
                    HashSet<Task> taskSet = new HashSet<Task>();
                    taskSet.add(taskIter.getValue());
                    dateSetSortedMap.put(taskIter.getKey(), taskSet);
                }
            }
        }
        return dateSetSortedMap;
    }

    private static TreeMap<Date, Task> allTasksInInterval(Task task, Date
            start, Date end) {
        TreeMap<Date, Task> result = new TreeMap<Date, Task>();
        Date nextStartTime = task.getStartTime();
        if (task.isRepeated()) {
            while (nextStartTime.compareTo(end) != 1 && nextStartTime
                    .compareTo(task.getEndTime())!=1) {
                if (nextStartTime.compareTo(start) != -1) {
                    result.put(nextStartTime, task);
                }
                nextStartTime = new Date((nextStartTime.getTime() + (task
                        .getRepeatInterval() * 1000)));
            }
        } else if ((task.getStartTime().compareTo(start) != -1 && task.getStartTime().compareTo(end) != 1))
            result.put(nextStartTime, task);
        return result;
    }
}