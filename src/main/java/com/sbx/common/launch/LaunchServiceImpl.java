package com.sbx.common.launch;

import com.sbx.common.constant.LaunchConstant;
import com.sbx.core.auto.annotation.AutoService;
import com.sbx.core.launch.service.LauncherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

/**
 * <p>LaunchServiceImpl class:</p>
 *
 * @author zhaijianchao
 * @version 1.0.0
 * @date 2020/3/23
 */
@Slf4j
@AutoService(LauncherService.class)
public class LaunchServiceImpl implements LauncherService {

    @Override
    public void launcher(SpringApplicationBuilder builder, String appName, String profile, boolean isLocalDev) {
        log.info("加载中心服务地址配置");
        Properties props = System.getProperties();
        props.setProperty("spring.cloud.nacos.config.server-addr", LaunchConstant.nacosAddr(profile));
        props.setProperty("spring.cloud.nacos.config.username","nacos");
        props.setProperty("spring.cloud.nacos.config.password",LaunchConstant.nacosPassword(profile));
        props.setProperty("spring.cloud.nacos.config.namespace",LaunchConstant.namespace(profile));
        props.setProperty("spring.cloud.nacos.discovery.server-addr",LaunchConstant.nacosAddr(profile));
        props.setProperty("spring.cloud.nacos.discovery.username","nacos");
        props.setProperty("spring.cloud.nacos.discovery.password",LaunchConstant.nacosPassword(profile));
        props.setProperty("spring.cloud.nacos.discovery.namespace",LaunchConstant.namespace(profile));
        props.setProperty("dubbo.registry.address","spring-cloud://"+LaunchConstant.nacosAddr(profile));


//        if (profile.equals("dev")) {
//            props.setProperty("spring.cloud.nacos.discovery.namespace","56921f44-c024-476d-b959-0d8bbcebfe89");
//        }
    }
    @Override
    public int getOrder() {
        return 0;
    }
    @Override
    public int compareTo(LauncherService o) {
        return 0;
    }
}
