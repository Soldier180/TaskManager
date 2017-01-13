package ua.sumdu.j2se.zaretsky.tasks.Model;

import java.util.*;


/**
 * Created by Nikolion on 24.11.2016.
 */
public class CompareByDate implements Comparator<Date> {
    @Override
    public int compare(Date e1, Date e2) {
        if (e1.compareTo(e2) == 1) {
            return -1;
        }
        if (e1.compareTo(e2) == -1) {
            return 1;
        } else {
            return 0;
        }
//        return  e1.compareTo(e2);
    }
}
