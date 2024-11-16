package com.reminder.handlers;

import com.reminder.annotation.Command;
import com.reminder.sender.MacWeChatAutomation;
import com.reminder.service.ExcelCompareService;

import java.util.List;

@Command(value = "remainder", description = "批量提醒功能")
public class RemainderCommandHandler extends CommandHandler {

    @Override
    public void validateArgs() {
        if (args.length != 5) {
            throw new IllegalArgumentException("提醒命令格式错误！正确格式: java -jar A.jar remainder <提醒消息> <参考Excel文件路径> <Excel文件路径> <发送前确认 (true or false)>");
        }
    }

    @Override
    public void execute() {
        validateArgs();
        String message = args[1];
        String BASE_FILE = args[2];
        String compareFile = args[3];
        Boolean ifAsk = args[4].equals("true");

        ExcelCompareService comparison = new ExcelCompareService(BASE_FILE);
        List<String> missingNames = comparison.findMissingNames(compareFile);

        System.out.println("找到 " + missingNames.size() + " 个缺失的名单：" + missingNames);
        for (String name : missingNames) {
            MacWeChatAutomation.sendMessage(name, message, ifAsk);
        }
    }
}
