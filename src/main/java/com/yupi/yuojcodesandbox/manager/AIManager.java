package com.yupi.yuojcodesandbox.manager;

import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.request.SparkRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于对接 AI 平台
 */
@Service
@Slf4j
@AllArgsConstructor
public class AIManager {

    private SparkClient sparkClient;

    private final String promote = "你是一个经验丰富的 %s 安全专家，现在我会给你一段代码，请你分析是否存在安全风险。\n" +
            "\n" +
            "请根据以下规则进行回复：\n" +
            "1. 返回内容必须是 JSON 格式；\n" +
            "2. 字段包括：\n" +
            "   - result：布尔类型，true 表示有安全风险，false 表示无风险；\n" +
            "   - suggestions：字符串数组类型，如有安全隐患，请给出对应的修改建议；\n" +
            "   - riskPoints（可选）：指出风险代码行或关键点，帮助开发者更快定位；\n" +
            "3. 如果没有风险，suggestions 返回空数组；\n" +
            "4. 只分析代码的安全性，忽略业务逻辑或功能实现是否正确。\n" +
            "\n" +
            "下面是代码内容：\n";
    /**
     * 向 AI 发送请求
     */
    public String sendMsgToXingHuo(String language, String content) {
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.systemContent(String.format(promote, language)));
        messages.add(SparkMessage.userContent(content));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传,取值为[1,4096],默认为2048
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.6)
                // 指定请求版本
                .apiVersion(SparkApiVersion.V4_0)
                .build();
        // 同步调用
        String responseContent = sparkClient.chatSync(sparkRequest).getContent().trim();
        String subContent = responseContent.substring(7, responseContent.length() - 3);
        log.info("星火 AI 返回的结果 {}", subContent);
        return subContent;
    }
}