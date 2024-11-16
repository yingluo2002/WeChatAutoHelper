package com.reminder.handlers;

import com.reminder.annotation.Command;
import com.reminder.sender.MacWeChatAutomation;

@Command(value = "send", description = "发送消息给指定联系人")
public class SendCommandHandler extends CommandHandler {
    @Override
    public void validateArgs() {
        if (args.length != 4) {
            throw new IllegalArgumentException("发送消息格式错误！正确格式: java -jar A.jar send <联系人> <消息内容> <发送前确认 (true or false)>");
        }
    }

    @Override
    public void execute() {
        validateArgs();
        String contactName = args[1];
        String message = args[2];
        Boolean ifAsk = args[3].equals("true");
        MacWeChatAutomation.sendMessage(contactName, message, ifAsk);
    }
}
