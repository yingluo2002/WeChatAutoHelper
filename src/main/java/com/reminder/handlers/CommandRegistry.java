package com.reminder.handlers;

import com.reminder.annotation.Command;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandRegistry {
    private final Map<String, Class<? extends CommandHandler>> commands = new HashMap<>();

    public CommandRegistry() {
        registerCommands();
    }

    private void registerCommands() {
        Reflections reflections = new Reflections("com.reminder.handlers");
        Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(Command.class);

        for (Class<?> clazz : commandClasses) {
            if (CommandHandler.class.isAssignableFrom(clazz)) {
                Command command = clazz.getAnnotation(Command.class);
                commands.put(command.value().toLowerCase(), (Class<? extends CommandHandler>) clazz);
            }
        }
    }

    public CommandHandler getHandler(String command) throws Exception {
        Class<? extends CommandHandler> handlerClass = commands.get(command.toLowerCase());
        if (handlerClass == null) {
            throw new IllegalArgumentException("未知的命令: " + command);
        }
        return handlerClass.getDeclaredConstructor().newInstance();
    }

    public void printUsage() {
        final String VERSION = "v1.0";
        final String JAR_NAME = "SenderAndRemainder-1.0.jar";
        final String[] COMMANDS = {
                "send        发送单条消息给指定联系人",
                "batchsend   批量发送不同消息给不同联系人",
                "remainder   对比Excel表格发送提醒消息"
        };

        StringBuilder sb = new StringBuilder();

        // 标题部分
        sb.append(String.format("微信自动化消息发送工具 %s%n", VERSION))
                .append(String.format("%n用法：java -jar %s <命令> [参数...]%n", JAR_NAME));

        // 命令概述
        sb.append("\n支持的命令：\n");
        for (String cmd : COMMANDS) {
            sb.append(String.format("  %s%n", cmd));
        }

        // 详细说明
        sb.append("\n命令详细说明：\n")
                // 1. 发送单条消息
                .append("1. 发送单条消息：\n")
                .append(String.format("   java -jar %s send <联系人> <消息内容> <发送前确认(true/false)>%n", JAR_NAME))
                .append(String.format("   示例: java -jar %s send \"张三\" \"你好\" true%n", JAR_NAME))

                // 2. 批量发送消息
                .append("\n2. 批量发送消息：\n")
                .append(String.format("   java -jar %s batchsend <receiver.json路径> <发送前确认(true/false)>%n", JAR_NAME))
                .append(String.format("   示例: java -jar %s batchsend \"./receiver.json\" true%n", JAR_NAME))

                // 3. Excel对比提醒
                .append("\n3. Excel对比提醒：\n")
                .append(String.format("   java -jar %s remainder <提醒消息> <参考Excel路径> <对比Excel路径> <发送前确认(true/false)>%n", JAR_NAME))
                .append(String.format("   示例: java -jar %s remainder \"请及时提交\" \"./base.xlsx\" \"./compare.xlsx\" true%n", JAR_NAME))

                // 注意事项
                .append("\n注意事项：\n")
                .append("- 使用前请确保微信保持在线并置于前台\n")
                .append("- Excel文件必须包含\"姓名\"列\n")
                .append("- receiver.json格式参考：\n")
                .append("[\n")
                .append("    {\n")
                .append("        \"contact\": \"张三\",\n")
                .append("        \"message\": \"你好张三，这是测试消息1\"\n")
                .append("    },\n")
                .append("    {\n")
                .append("        \"contact\": \"李四\",\n")
                .append("        \"message\": \"你好李四，这是测试消息2\"\n")
                .append("    },\n")
                .append("    {\n")
                .append("        \"contact\": \"王五\",\n")
                .append("        \"message\": \"你好王五，这是测试消息3\"\n")
                .append("    }\n")
                .append("]\n")
                .append("- 联系人名称尽量与微信好友昵称完全匹配\n");

        System.out.print(sb);
    }
}
