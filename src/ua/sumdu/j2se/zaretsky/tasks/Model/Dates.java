package ua.sumdu.j2se.zaretsky.tasks.Model;

import java.util.Date;

/**
 * Created by Nikolion on 22.11.2016.
 */
public class Dates extends Date {

    public Dates(long time) {
        super(time);
    }
    public Dates() {
        super();
    }
    @Override
    public void setTime(long time) {
        throw new UnsupportedOperationException("Date should not be changed");
    }
    @Override
    public Date clone() {
        return new Date(getTime());
    }

}