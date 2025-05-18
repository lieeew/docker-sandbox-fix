package com.yupi.yuojcodesandbox.config;

import cn.hutool.core.util.ClassLoaderUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.time.Duration;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2025/5/18
 * @description
 */
@Configuration
public class DockerClientConfig {


    /**
     * 不使用 SSL
     */
    @Bean
    public DockerClient dockerClient() {
        com.github.dockerjava.core.DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://服务器IP:2376")
                .withDockerTlsVerify(false)
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        return DockerClientImpl.getInstance(config, httpClient);
    }

    /**
     * 使用 SSL
     * ca 文件位置具参考 https://ldv97g8qxo.feishu.cn/docx/NubgdM2DrobZ9Mx5NUAcxGXhnIc
     */
//    @Bean
//    public DockerClient dockerClient() {
//        URL url = ClassLoaderUtil.getClassLoader().getResource("ca");
//        com.github.dockerjava.core.DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//                .withDockerHost("tcp://服务器IP:2376")
//                .withDockerTlsVerify(true)
//                .withDockerCertPath(url.getPath().substring(1))
//                .build();
//        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
//                .dockerHost(config.getDockerHost())
//                .sslConfig(config.getSSLConfig())
//                .maxConnections(100)
//                .connectionTimeout(Duration.ofSeconds(30))
//                .responseTimeout(Duration.ofSeconds(45))
//                .build();
//        return DockerClientImpl.getInstance(config, httpClient);
//    }
}
