package com.yupi.yuojcodesandbox.controller;

import cn.hutool.json.JSONUtil;
import com.yupi.yuojcodesandbox.manager.AIManager;
import com.yupi.yuojcodesandbox.model.AnalysisResult;
import com.yupi.yuojcodesandbox.sandbox.AICodeSandbox;
import com.yupi.yuojcodesandbox.sandbox.JavaDockerCodeSandbox;
import com.yupi.yuojcodesandbox.model.ExecuteCodeRequest;
import com.yupi.yuojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
public class MainController {

    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Resource
    private JavaDockerCodeSandbox javaDockerCodeSandbox;

    @Resource
    private AICodeSandbox aiCodeSandbox;

    @Resource
    private AIManager aiManager;

    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
    }

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request,
                                    HttpServletResponse response) {
        // 基本的认证 为了方便单独测试而注销了
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
//        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
//            response.setStatus(403);
//            return null;
//        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaDockerCodeSandbox.executeCode(executeCodeRequest);
    }

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @PostMapping("/ai")
    ExecuteCodeResponse executeCodeAI(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request,
                                    HttpServletResponse response) {
        // 基本的认证 为了方便单独测试而注销了
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
//        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
//            response.setStatus(403);
//            return null;
//        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
//        AnalysisResult securityAnalysisResult = checkUserCode(executeCodeRequest.getCode(), executeCodeRequest.getLanguage());
//        if (securityAnalysisResult.isSecure()) {
//            return ExecuteCodeResponse.builder().analysisResult(securityAnalysisResult).message("代码安全性有问题不允许执行").build();
//        }
        return aiCodeSandbox.executeCode(executeCodeRequest);
    }

    private AnalysisResult checkUserCode(String code, String language) {
        String userContent = aiManager.sendMsgToXingHuo(language, code);
        return JSONUtil.toBean(userContent, AnalysisResult.class);
    }

}
