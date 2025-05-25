package com.yupi.yuojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    /**
     * outputList
     */
    private List<String> outputList;

    /**
     * 最后结果集合
     */
    private List<Boolean> isCorrect;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private AnalysisResult analysisResult;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
