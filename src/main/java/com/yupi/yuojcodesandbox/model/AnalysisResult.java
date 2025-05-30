package com.yupi.yuojcodesandbox.model;

import lombok.Data;

/**
 * @author leikooo
 */
@Data
public class AnalysisResult {
    /**
     * 是否安全（true 表示安全，false 表示存在风险）
     */
    private boolean secure;


    private Integer executeStatus;

    /**
     * 如果存在风险，返回修改建议
     */
    private String suggestion;

    public AnalysisResult() {
    }

    public AnalysisResult(boolean secure, String suggestion) {
        this.secure = secure;
        this.suggestion = suggestion;
    }

    @Override
    public String toString() {
        return "{" +
                "\"secure\":" + secure + "," +
                "\"suggestion\":\"" + suggestion + "\"" +
                '}';
    }
}
