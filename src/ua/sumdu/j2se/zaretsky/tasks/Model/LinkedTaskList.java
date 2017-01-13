package ua.sumdu.j2se.zaretsky.tasks.Model;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Class for creating and editing LinkedTaskList
 *
 * @author Nikolay Zaretsky
 * @version 1.0 25 Oct 2016
 */
public class LinkedTaskList extends TaskList implements Serializable {

    private Node first = null;
    private Node last = null;

    /**
     * Class for work with Nodes of LinkedTaskList
     */
    private class Node  implements Serializable{
        Node next = null;
        Node previous = null;
        Task taskNode;

        private Node(Task task) {
            if (task == null) {
                throw new NullPointerException();
            }
            this.taskNode = task;
        }

        void setNext(Node next) {
            this.next = next;
        }

        void setPrevious(Node previous) {
            this.previous = previous;
        }
    }

    /**
     * Method to add task into TaskList
     *
     * @param task - current task for adding
     */
    public void add(Task task) {
        if (task != null) {
            Node node = new Node(task);
            if (count == 0) {
                first = node;
            } else {
                last.setNext(node);
                node.setPrevious(last);
            }

            last = node;
            count++;
        } else {
            throw new IllegalArgumentException("Incorrect task");
        }
    }

    /**
     * Method to remove task from TaskList
     *
     * @param task - current task for removing
     * @return true  if this task was in TaskList
     * @throws IllegalArgumentException if task = null
     */
    public boolean remove(Task task) throws IllegalArgumentException {
        if (task != null) {
            Node previous = null;
            Node current = first;

            while (current != null) {
                if (current.taskNode.equals(task)) {

                    if (previous != null) {                 // Node in the middle or at the end.
                        previous.setNext(current.next);

                        if (current.next == null) {         //If at the end, then we change the last.
                            last = previous;
                        } else {
                            current.next.setPrevious(previous);
                        }
                        count--;
                    } else {
                        if (count != 0) {                   // Node - at the beginning
                            first = first.next;
                            count--;

                            if (count == 0) {
                                last = null;
                            } else {
                                first.previous = null;
                            }
                        }
                    }

                    return true;
                }

                previous = current;
                current = current.next;
            }

            return false;
        } else {
            throw new IllegalArgumentException("Incorrect task");
        }
    }

    /**
     * Method to getting task from TaskList
     *
     * @param index - getting task with current index from TaskList
     * @return Task
     */
    public Task getTask(int index) throws IllegalArgumentException {

        if (index >= 0 && index < size()) {
            Node k = first;
            for (int i = 0; i < index; i++) {
                k = k.next;
            }
            return k.taskNode;
        } else {
            throw new IllegalArgumentException("Incorrect index for array");
        }
    }

    @Override
    public Iterator iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Task> {
        private int index = 0;
        private Node current = first;

        @Override
        public boolean hasNext() {
            return index < count;
        }

        @Override
        public Task next() {
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            Task currentIteratorTask = current.taskNode;
            current = current.next;
            index++;
            return currentIteratorTask;
        }

        @Override
        public void remove() throws IllegalStateException {

            if (index != 0) {
                LinkedTaskList.this.remove(current.previous.taskNode);
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

        for (Task task :  this) {
            if (task.isActive()) {
                active++;
            } else {
                nonActive++;
            }

        }

        return "LinkedTaskList\nTotal number of tasks: " + count +
                "\nNot active tasks: " + nonActive +
                "\nActive tasks: " + active;
    }

    @Override
    public LinkedTaskList clone() throws CloneNotSupportedException {
        return (LinkedTaskList) super.clone();
    }
}
