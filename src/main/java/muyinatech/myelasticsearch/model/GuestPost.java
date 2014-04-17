package muyinatech.myelasticsearch.model;

import java.util.Date;

/**
 * Created by Tosin on 17/04/14.
 */
public class GuestPost {

    private final String name;
    private final String country;
    private final String message;
    private final Date date;

    public GuestPost(String name, String country, String message, Date date) {
        this.name = name;
        this.country = country;
        this.message = message;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
