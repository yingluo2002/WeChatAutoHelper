package com.reminder.sender;

import com.reminder.sender.util.AppleScriptExecutor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class MacWeChatAutomation {
    private static final Robot robot;
    private static final String SCRIPT = """
            tell application "WeChat" to activate
            tell application "System Events"
                set frontmost of process "WeChat" to true
                delay 0.3
                tell process "WeChat"
                    click text field 1 of splitter group 1 of window 1
                    delay 0.5
                    keystroke "v" using {command down}
                    delay 0.5
                    key code 36
                    delay 0.5
                    set value of text area 1 of scroll area 2 of splitter group 1 of splitter group 1 of window 1 to "%s"
                    delay 0.5
                    set value of attribute "AXFocused" of text area 1 of scroll area 2 of splitter group 1 of splitter group 1 of window 1 to true
                    key code 36
                end tell
            end tell
            """;

    static {
        try {
            robot = new Robot();
            robot.setAutoDelay(500);
        } catch (AWTException e) {
            throw new RuntimeException("Failed to initialize Robot", e);
        }
    }

    /**
     * 显示确认发送对话框
     * @param contactName 接收者姓名
     * @return 用户是否确认发送
     */
    private static int showConfirmDialog(String contactName, String message) {
        JOptionPane pane = new JOptionPane(
                "消息内容：【" + message + "】\n接收好友名称：【" + contactName + "】\n是否确认发送？",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        // 创建对话框
        JDialog dialog = pane.createDialog("发送确认");
        // 设置窗口总在最前
        dialog.setAlwaysOnTop(true);
        // 显示对话框
        dialog.setVisible(true);

        // 获取用户选择的结果
        Object selectedValue = pane.getValue();
        if(selectedValue == null)
            return JOptionPane.CLOSED_OPTION;
        return (Integer)selectedValue;
    }

    /**
     * 发送消息
     */
    public static void sendMessage(String contactName, String message, Boolean ifAsk) {
        setClipboardContent(contactName);

        AppleScriptExecutor.execute("tell application \"WeChat\" to activate");

        String script = SCRIPT.formatted(message);

        if (ifAsk) {
            int result = showConfirmDialog(contactName, message);
            if (result == JOptionPane.NO_OPTION) {
                System.out.printf("已取消给【%s】取消发送消息：【%s】%n", contactName, message);
                return;
            }
            if (result == JOptionPane.CANCEL_OPTION) {
                System.out.printf("程序结束。%n");
                System.exit(0);
            }
        }

        AppleScriptExecutor.execute(script);
        robot.delay(500);
    }

    /**
     * 设置剪贴板内容
     */
    private static void setClipboardContent(String content) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(content);
        clipboard.setContents(selection, null);
    }
}