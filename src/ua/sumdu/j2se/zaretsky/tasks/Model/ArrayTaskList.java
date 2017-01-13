package ua.sumdu.j2se.zaretsky.tasks.Model;

import java.io.Serializable;
import java.util.Iterator;
/**
 * Class for creating and editing ArrayTaskList
 *
 * @author Nikolay Zaretsky
 * @version 1.0 15 Oct 2016
 */
public class ArrayTaskList extends TaskList implements Cloneable, Serializable {

    private Task[] arrayTaskList = new Task[10];

    /**
     * Method to add task into ArrayTaskList
     *
     * @param task - current task for adding
     */
    public void add(Task task) throws IllegalArgumentException {

        if (task != null) {

            if (count < arrayTaskList.length) {
                arrayTaskList[count] = task;
            } else {
                arrayTaskList = increaseArray(arrayTaskList);
                arrayTaskList[count] = task;
            }
            count++;
        } else {
            throw new IllegalArgumentException("Incorrect task");
        }


    }

    /**
     * Method to remove task from ArrayTaskList
     *
     * @param task - current task for removing
     * @return true  if this task was in ArrayTaskList
     */
    public boolean remove(Task task) {
        if (task != null) {
            for (int i = 0; i < count; i++) {
                if (task.equals(arrayTaskList[i])) {
                    for (int a = i; a < count - 1; a++) {
                        arrayTaskList[a] = arrayTaskList[a + 1];
                    }
                    arrayTaskList[count-1]=null;
                    count--;
                    return true;

                }
            }
            return false;
        } else {
            throw new IllegalArgumentException("Incorrect task");
        }
    }

    /**
     * Method to getting task from ArrayTaskList
     *
     * @param index - getting task with current index from ArrayTaskList
     * @return Task
     */
    public Task getTask(int index) throws IllegalArgumentException {
        if (index >= 0 && index < count) {
            return arrayTaskList[index];
        } else {
            throw new IllegalArgumentException("Incorrect index for array");
        }

    }

    /**
     * Method to get size ArrayTaskList
     *
     * @return size of ArrayTaskList(with tasks)
     */
    public int size() {

        return count;
    }


    /**
     * Method to increase  ArrayTaskList
     *
     * @param arrayTaskList
     * @return newArrayTaskList - new biggest array
     */
    private Task[] increaseArray(Task[] arrayTaskList) {
        int newSize = arrayTaskList.length * 30 / 100;
        Task[] newArrayTaskList = new Task[newSize + arrayTaskList.length];
        System.arraycopy(arrayTaskList, 0, newArrayTaskList, 0, arrayTaskList.length);

        return newArrayTaskList;
    }

    @Override
    public Iterator iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Task> {
        private int index = 0;


        @Override
        public boolean hasNext() {

            return index < count;
        }

        @Override
        public Task next() throws IllegalStateException {
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            Task currentIteratorTask = arrayTaskList[index];
            index++;
            return currentIteratorTask;
        }

        @Override
        public void remove() throws IllegalStateException {

            if (index != 0) {
                ArrayTaskList.this.remove(arrayTaskList[index - 1]);
                index--;
            } else {
                throw new IllegalStateException();
            }
        }
    }


    @Override
    public String toString() {
        int active = 0;
        int nonActive = 0;
        Iterator<Task> iterator = this.iterator();


        while (iterator.hasNext()) {
            if (iterator.next().isActive()) {
                active++;
            } else {
                nonActive++;
            }
        }
        return "ArrayTaskList\nTotal number of tasks: " + count +
                "\nNot active tasks: " + nonActive +
                "\nActive tasks: " + active;

    }
    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList res = (ArrayTaskList)super.clone();
        res.arrayTaskList = this.arrayTaskList.clone();
        return res;
    }
}
