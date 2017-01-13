package ua.sumdu.j2se.zaretsky.tasks.Model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Nikolion on 27.11.2016.
 */
public class TaskIO {
    public static final String TIME_PATTERN = "[yyyy-MM-dd HH:mm:ss.sss]";
    public static final SimpleDateFormat DATE_F = new SimpleDateFormat(TIME_PATTERN);
    public static final String TO = " to ";
    public static final String FROM = " from ";
    public static final String AT = " at ";

    public static void write(TaskList tasks, OutputStream out) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeInt(tasks.size());
            for (Task task : (Iterable<Task>) tasks) {
                oos.writeInt(task.getTitle().length());
                oos.writeObject(task.getTitle());
                oos.writeInt(task.isActive() ? 1 : 0);
                oos.writeInt(task.getRepeatInterval());
                if (task.isRepeated()) {
                    oos.writeLong(task.getStartTime().getTime());
                    oos.writeLong(task.getEndTime().getTime());
                } else {
                    oos.writeLong(task.getTime().getTime());
                }
            }
        } finally {
            oos.flush();
            oos.close();
        }


    }

    public static void read(TaskList tasks, InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(in);
            int a = ois.readInt();
            for (int i = 0; i < a; i++) {
                int lengthTitle = ois.readInt();
                String title = (String) ois.readObject();
                boolean active = ois.readInt() == 1;
                int repeat = ois.readInt();
                if (repeat == 0) {
                    long startTime = ois.readLong();
                    Task task = new Task(title, new Date(startTime));
                    task.setActive(active);
                    tasks.add(task);
                } else {
                    long startTime = ois.readLong();
                    long endTime = ois.readLong();
                    Task task = new Task(title, new Date(startTime), new Date
                            (endTime), repeat);
                    task.setActive(active);
                    tasks.add(task);
                }
            }
        } finally {
            ois.close();
        }


    }

    public static void writeBinary(TaskList tasks, File file) throws
            IOException {
        FileOutputStream fileOutputStr = new FileOutputStream(file);
        try {
            write(tasks, fileOutputStr);
        } finally {
            fileOutputStr.flush();
            fileOutputStr.close();
        }
    }

    public static void readBinary(TaskList tasks, File file) throws IOException,
            ClassNotFoundException {

        FileInputStream fis = new FileInputStream(file);
        read(tasks, fis);
        fis.close();


    }

    public static void write(TaskList tasks, Writer out) throws IOException {
        try {

            int numLine = 1;

            for (Task t:tasks) {
                out.write(getTitleModif(t.getTitle()));
                if (!t.isActive() && !t.isRepeated()) {
                    out.write(" at " + DATE_F.format
                            (t.getStartTime()) + " is inactive");

                } else if (!t.isActive() && t.isRepeated()) {
                    out.write(" from " + DATE_F.format
                            (t.getStartTime())
                            + " to " + DATE_F.format(t.getEndTime())
                            + " every " + secondsToStringTime(t.getRepeatInterval
                            ()) + " is " +
                            "inactive");
                } else if (t.isActive() && t.isRepeated()) {
                    out.write(" from " + DATE_F.format
                            (t.getStartTime())
                            + " to " + DATE_F.format(t.getEndTime())
                            + " every " + secondsToStringTime(t.getRepeatInterval
                            ()));
                } else if (t.isActive() && !t.isRepeated()) {
                    out.write(" at " + DATE_F.format
                            (t.getStartTime()));
                }
                if (numLine < tasks.count) {
                    out.write(";");
                } else {
                    out.write(".");
                }
                out.write(System.getProperty("line.separator"));
                numLine++;
            }
        } finally {
            out.flush();
            out.close();
        }


    }

    public static void read(TaskList tasks, Reader in) throws IOException, ParseException {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(in);
            String s;

            while ((s = bufferedReader.readLine()) != null) {
                Task task = parseTask(s);
                tasks.add(task);
            }
        } finally {
            bufferedReader.close();
        }

    }


    public static void writeText(TaskList tasks, File file) throws
            IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            write(tasks, writer);
        } finally {
            //writer.flush();
            writer.close();
        }

    }

    public static void readText(TaskList tasks, File file) throws IOException, ParseException {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            read(tasks, fileReader);
        } finally {
            fileReader.close();
        }
    }


    public static String secondsToStringTime(int t) {
        String result = "";
        int days = t / (60 * 60 * 24);
        int hours = (t - days * 60 * 60 * 24) / (60 * 60);
        int minutes = (t - (days * 60 * 60 * 24) - (hours * 60 * 60)) / 60;
        int seconds = t - (days * 60 * 60 * 24) - (hours * 60 * 60) - (minutes * 60);

        if (days > 0) {
            result = result + days + ending(days, " day");
        }
        if (hours > 0) {
            result = result + " " + hours + ending(hours, " hour");
        }
        if (minutes > 0) {
            result = result + " " + minutes + ending(minutes, " minute");
        }
        if (seconds > 0) {
            result = result + " " + seconds + ending(seconds, " second");
        }
        result = result.trim();
        return "[" + result + "]";
    }

    private static String ending(int time, String s) {
        if (time < 2) {
            return s;
        } else {
            return s + "s";
        }
    }

    private static String getTitleModif(String s) {
        return "\"" + s.replaceAll("\"", "\"\"") + "\"";
    }

    private static Task parseTask(String s) throws ParseException {
        Task task;
        if (s.contains("] every [")) {
            task = parseRepeatedTask(s);
        } else {
            task = parseNotRepeatedTask(s);
        }
        return task;
    }


    private static Task parseNotRepeatedTask(String s) throws ParseException {
        Task task;
        String title;
        Date time;

        title = s.substring(1, s.lastIndexOf(AT) - 1);
        title = parseQuotes(title);

        int timePosition = s.lastIndexOf(AT) + AT.length();
        String timeString = s.substring(timePosition, timePosition + TIME_PATTERN.length());
        time = DATE_F.parse(timeString);

        task = new Task(title, time);

        if (!s.contains("inactive")) {
            task.setActive(true);
        }

        return task;
    }

    private static Task parseRepeatedTask(String s) throws ParseException {
        Task task;
        String title;
        Date startTime;
        Date endTime;
        int interval;

        title = s.substring(1, s.lastIndexOf(FROM) - 1);
        title = parseQuotes(title);

        int startTimePosition = s.lastIndexOf(FROM) + FROM.length();
        String startTimeString = s.substring(startTimePosition, startTimePosition + TIME_PATTERN.length());
        startTime = DATE_F.parse(startTimeString);

        int endTimePos = s.lastIndexOf(TO) + TO.length();
        String endTimeString = s.substring(endTimePos, endTimePos + TIME_PATTERN.length());
        endTime = DATE_F.parse(endTimeString);

        String intervalString = s.substring(s.lastIndexOf("[") + 1, s
                .lastIndexOf("]"));
        interval = parseInterval(intervalString);

        task = new Task(title, startTime, endTime, interval);

        if (!s.contains("inactive;")) {
            task.setActive(true);
        }

        return task;
    }

    private static String parseQuotes(String title) {
        return title.replaceAll("\"\"", "\"");
    }


    private static int parseInterval(String intervalString) throws
            ParseException, IllegalArgumentException {
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        String[] parts = intervalString.split(" ");
        for (int i = 0; i < parts.length; i = i + 2) {
            if (parts[i + 1].contains("day")) {
                day = Integer.parseInt(parts[i]);
                continue;
            }
            if (parts[i + 1].contains("hour")) {
                hour = Integer.parseInt(parts[i]);
                continue;
            }
            if (parts[i + 1].contains("minute")) {
                minute = Integer.parseInt(parts[i]);
                continue;
            }
            if (parts[i + 1].contains("second")) {
                second = Integer.parseInt(parts[i]);
            }
        }
        if (day < 0 || hour < 0 || minute < 0 || second < 0 || hour >= 24 ||
                minute > 59 || second > 59) {
            throw new IllegalArgumentException("Incorrect time");
        }
        return ((day * 60 * 60 * 24) + (hour * 60 * 60) + (minute * 60) + second);
    }

    public static String writeTask(Task t){
        String result = "";

        result= result.concat(getTitleModif(t.getTitle()));
        if (!t.isActive() && !t.isRepeated()) {
           result= result.concat(" at " + DATE_F.format
                    (t.getStartTime()) + " is inactive");

        } else if (!t.isActive() && t.isRepeated()) {
            result=result.concat(" from " + DATE_F.format
                    (t.getStartTime())
                    + " to " + DATE_F.format(t.getEndTime())
                    + " every " + secondsToStringTime(t.getRepeatInterval
                    ()) + " is " +
                    "inactive");
        } else if (t.isActive() && t.isRepeated()) {
            result=result.concat(" from " + DATE_F.format
                    (t.getStartTime())
                    + " to " + DATE_F.format(t.getEndTime())
                    + " every " + secondsToStringTime(t.getRepeatInterval
                    ()));
        } else if (t.isActive() && !t.isRepeated()) {
            result=result.concat(" at " + DATE_F.format
                    (t.getStartTime()));
        }

        return result;
    }


}
