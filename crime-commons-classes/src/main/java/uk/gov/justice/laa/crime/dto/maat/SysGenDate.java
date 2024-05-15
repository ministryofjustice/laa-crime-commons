package uk.gov.justice.laa.crime.dto.maat;

import java.io.Serializable;
import java.util.Date;

public class SysGenDate implements Comparable<SysGenDate>, Serializable {

    private static final long serialVersionUID = -126667557340875129L;

    private Date value;

    public SysGenDate() {
    }

    public SysGenDate(Date value) {
        setValue(value);
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public int compareTo(SysGenDate o) {
        return value != null ? value.compareTo(o.getValue()) : 1;
    }

    @Override
    public boolean equals(Object o) {
        return value != null && value.equals(o);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}