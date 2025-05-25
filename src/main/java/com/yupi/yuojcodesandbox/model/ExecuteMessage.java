package com.yupi.yuojcodesandbox.model;

import lombok.Data;

/**
 * 进程执行信息
 */
@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;

    private Long memory;

    /**
     * 读取是否成功，用于判断是否需要继续读
     */
    private boolean isCorrect;
}
