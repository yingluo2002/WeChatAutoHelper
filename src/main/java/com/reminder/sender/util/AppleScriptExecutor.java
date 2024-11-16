package com.reminder.sender.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AppleScriptExecutor {

    public static void execute(String script) {
        try {
            String[] lines = script.split("\n");
            List<String> cmd = new ArrayList<>();
            cmd.add("osascript");

            // 为每一行添加 -e 参数
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    cmd.add("-e");
                    cmd.add(line.trim());
                }
            }

            // 创建执行 AppleScript 的进程
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            // 读取标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 读取错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder error = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                error.append(line).append("\n");
            }

            // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("AppleScript execution failed with exit code: " + exitCode + "\nError: " + error.toString());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute AppleScript: " + e.getMessage(), e);
        }
    }
}