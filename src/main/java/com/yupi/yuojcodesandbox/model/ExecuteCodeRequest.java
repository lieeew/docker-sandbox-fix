package com.yupi.yuojcodesandbox.model;

import com.yupi.yuojbackendmodel.model.entity.CodeQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {

    private List<String> inputList;

    private String code;

    private String language;

    private CodeQuestion codeQuestion;
}
