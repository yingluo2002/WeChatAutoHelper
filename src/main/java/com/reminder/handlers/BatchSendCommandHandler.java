package com.reminder.handlers;

import com.reminder.annotation.Command;
import com.reminder.service.BatchMessageService;

@Command(value = "batchsend", description = "批量发送不同消息给不同微信好友")
public class BatchSendCommandHandler extends CommandHandler {
    
    @Override
    public void validateArgs() {
        if (args.length != 3) {
            throw new IllegalArgumentException("批量发送命令格式错误！正确格式: java -jar A.jar batchsend <好友消息json文件路径> <发送前确认 (true or false)>");
        }
    }

    @Override
    public void execute() {
        validateArgs();
        String jsonPath = args[1];
        Boolean ifAsk = args[2].equals("true");
        
        BatchMessageService batchService = new BatchMessageService(jsonPath);
        batchService.sendMessagesFromJson(ifAsk);
    }
} 