package com.reminder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminder.model.MessageEntry;
import com.reminder.sender.MacWeChatAutomation;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BatchMessageService {
    private final String JSON_FILE_PATH;
    private final ObjectMapper objectMapper;

    public BatchMessageService(String jsonFilePath) {
        this.objectMapper = new ObjectMapper();
        this.JSON_FILE_PATH = jsonFilePath;
    }

    public void sendMessagesFromJson(Boolean ifAsk) {
        try {
            // 读取 JSON 文件
            List<MessageEntry> messages = readMessagesFromJson();
            
            System.out.println("找到 " + messages.size() + " 条消息需要发送");
            
            // 遍历并发送消息
            for (MessageEntry entry : messages) {
                String contactName = entry.getContact();
                String message = entry.getMessage();
                
                System.out.println("正在发送消息给: " + contactName + ", 消息内容: " + message);
                MacWeChatAutomation.sendMessage(contactName, message, ifAsk);
                
                // 添加适当的延迟，避免发送太快
                Thread.sleep(1000);
            }
            
            System.out.println("批量发送完成！");
            
        } catch (IOException e) {
            System.err.println("读取接收者配置文件失败: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("发送消息被中断: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<MessageEntry> readMessagesFromJson() throws IOException {
        File jsonFile = new File(this.JSON_FILE_PATH);
        if (!jsonFile.exists()) {
            throw new IOException("receiver.json 文件不存在！");
        }
        
        return objectMapper.readValue(jsonFile, new TypeReference<>() {
        });
    }
} 