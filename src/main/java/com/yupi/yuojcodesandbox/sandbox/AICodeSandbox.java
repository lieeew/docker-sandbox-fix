package com.yupi.yuojcodesandbox.sandbox;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yupi.yuojbackendmodel.model.entity.CodeQuestion;
import com.yupi.yuojcodesandbox.model.ExecuteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2025/5/20
 * @description
 */
@Slf4j
@Component
public class AICodeSandbox extends JavaCodeSandboxTemplate {

    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList, CodeQuestion codeQuestion) {
        List<ExecuteMessage> resultList = new ArrayList<>();
        File parentFile = userCodeFile.getParentFile();
        try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{parentFile.toURI().toURL()})) {
            Class<?> clazz = classLoader.loadClass(codeQuestion.getClassName());
            JSONArray paramTypeArray = JSONUtil.parseArray(codeQuestion.getParamTypes());
            String methodName = codeQuestion.getMethodName();
            Class<?>[] paramClasses = new Class<?>[paramTypeArray.size()];
            for (int i = 0; i < paramTypeArray.size(); i++) {
                paramClasses[i] = resolveClass(paramTypeArray.getStr(i));
            }

            Method method = clazz.getDeclaredMethod(methodName, paramClasses);
            method.setAccessible(true);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            List<CodeQuestion.TestCase> testCases = codeQuestion.getTestCases();
            for (CodeQuestion.TestCase testParam : testCases) {
                ExecuteMessage message = new ExecuteMessage();
                try {
                    // inputStr 示例: "[[1, 2, 3, 4], 3]"
                    //                "[["a", "b", "c"],"c"]"
                    List<Object> params = testParam.getParams();
                    Object[] actualParams = new Object[params.size()];
                    for (int i = 0; i < params.size(); i++) {
                        actualParams[i] = convertParam(params.get(i), paramTypeArray.getStr(i));
                    }
                    Object result = method.invoke(instance, actualParams);
                    message.setMessage(String.valueOf(result));
                    // 最后的结果进行校验
                    message.setCorrect(isCorrect(testParam, result));
                } catch (Exception e) {
                    message.setErrorMessage(e.getMessage());
                    message.setCorrect(false);
                    log.error("runFile failed", e);
                }
                resultList.add(message);
            }

        } catch (Exception e) {
            log.error("runFile failed", e);
        }
        return resultList;
    }

    private boolean isCorrect(CodeQuestion.TestCase testParam, Object result) {
        List<Object> expectedList = testParam.getExpected();

        if (expectedList == null || expectedList.isEmpty()) {
            return result == null;
        }

        Object expected = expectedList.size() == 1 ? expectedList.get(0) : expectedList;

        // 如果 result 是数组
        if (result != null && result.getClass().isArray()) {
            Object[] resultArray = convertToObjectArray(result);
            if (expected instanceof List) {
                List<?> expectedArray = (List<?>) expected;
                return Arrays.deepEquals(resultArray, expectedArray.toArray());
            }
        }

        // 如果是 List 类型
        if (result instanceof List && expected instanceof List) {
            return Objects.equals(result, expected);
        }

        // 基础类型比较
//        return Objects.equals(result, expected);
        return false;
    }

    private Object[] convertToObjectArray(Object array) {
        if (!array.getClass().isArray()) {
            return null;
        }
        int length = Array.getLength(array);
        Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            result[i] = Array.get(array, i);
        }
        return result;
    }



    public Class<?> resolveClass(String typeName) throws ClassNotFoundException {
        return switch (typeName) {
            case "int" -> int.class;
            case "int[]" -> int[].class;
            case "String" -> String.class;
            case "String[]" -> String[].class;
            default -> Class.forName(typeName);
        };
    }

    public Object convertParam(Object value, String typeName) {
        return switch (typeName) {
            case "int" -> Integer.parseInt(value.toString());
            case "int[]" -> {
                ArrayList<?> list = (ArrayList<?>) value;
                int[] intArray = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    intArray[i] = ((Number) list.get(i)).intValue();
                }
                yield intArray;
            }
            case "String" -> value.toString();
            case "String[]" -> {
                ArrayList<?> list = (ArrayList<?>) value;
                String[] stringArray = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    stringArray[i] = (String) list.get(i);
                }
                yield stringArray;
            }
            default -> throw new IllegalArgumentException("Unsupported type: " + typeName);
        };
    }
}
