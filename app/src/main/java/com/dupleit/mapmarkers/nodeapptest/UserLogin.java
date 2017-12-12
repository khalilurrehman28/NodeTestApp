
package com.dupleit.mapmarkers.nodeapptest;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLogin implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    private final static long serialVersionUID = 4596898507111567568L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserLogin() {
    }

    /**
     * 
     * @param message
     * @param data
     */
    public UserLogin(String message, List<Datum> data) {
        super();
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
