package com.engineerskasa.holywrit;

public class History {
    int id;
    String ref;
    String mode;
    String timestamp;

    public History() {
    }

    public History(int id, String ref, String mode, String timestamp) {
        this.id = id;
        this.ref = ref;
        this.mode = mode;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
