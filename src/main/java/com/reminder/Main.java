package com.reminder;

import com.reminder.handlers.CommandHandler;
import com.reminder.handlers.CommandRegistry;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            new CommandRegistry().printUsage();
            return;
        }

        try {
            CommandRegistry registry = new CommandRegistry();
            CommandHandler handler = registry.getHandler(args[0]);
            handler.setArgs(args);
            handler.execute();
        } catch (Exception e) {
            System.err.println("执行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}