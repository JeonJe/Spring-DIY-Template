package com.diy.app.domain;

public class Lecture {
    Long id;
    String name;
    Long price;
    private boolean visible;


    public Lecture() {

    }


    public Lecture(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.visible = false;
    }

    public Lecture(Long id, String name, Long price, boolean visible) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.visible = visible;
    }

    @ChangeVisible
    private void changeVisible() {
        this.visible = true;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @MethodOrder(1)
    public String getName() {
        return this.name;
    }


    @MethodOrder(2)
    public Long getPrice() {
        return this.price;
    }

    public Long getId() {
        return id;
    }

}
