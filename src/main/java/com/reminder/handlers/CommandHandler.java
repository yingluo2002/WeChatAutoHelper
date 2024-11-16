package com.reminder.handlers;

public abstract class CommandHandler {
    protected String[] args;

    public void setArgs(String[] args) {
        this.args = args;
    }

    public abstract void execute();
    public abstract void validateArgs();
}
