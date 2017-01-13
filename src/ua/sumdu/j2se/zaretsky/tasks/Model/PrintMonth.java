package ua.sumdu.j2se.zaretsky.tasks.Model;

/**
 * Created by Nikolion on 31.12.2016.
 */
import java.util.*;
import java.text.*;
import static java.util.Calendar.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class PrintMonth {
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.set(DAY_OF_MONTH, 1);
        c.getTime();
        if (args.length == 1) {
            c.set(MONTH, parseInt(args[0]) - 1);
        }
        int month = c.get(MONTH);
        System.out.println(new SimpleDateFormat("MMMM yyyy:").format(c.getTime()));

        c.set(DAY_OF_WEEK, c.getFirstDayOfWeek());

        c.add(DATE, -7);
        DateFormat wd = new SimpleDateFormat("EE");
        for (int i = 0; i < 7; i++) {
            System.out.print(" ");
            System.out.print(wd.format(c.getTime()));
            c.add(DATE, 1);
        }
        System.out.println();

        do {
            System.out.print(c.get(MONTH) != month ?
                    "   " :
                    format("%3d", c.get(DAY_OF_MONTH)));
            c.add(DATE, 1);
            if (c.get(DAY_OF_WEEK) == c.getFirstDayOfWeek())
                System.out.println();
        }
        while (c.get(MONTH) == month || c.get(DAY_OF_WEEK) != c.getFirstDayOfWeek());
    }
}
