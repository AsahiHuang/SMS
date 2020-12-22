package com.asahih.sms.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Asahi Huang on 2020/12/6.
 */

@Entity
@Table(name = "log")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })

public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    private String type;
    private String username;
    private String name;
    private String time;
    private String handle;
    private String executor;

    public Log(){

    }

    public Log(String type,String username,String name,String time,String handle,String executor){
        this.type = type;
        this.username = username;
        this.name = name;
        this.time = time;
        this.handle = handle;
        this.executor = executor;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }
}
