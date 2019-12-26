/**
 * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.mpen.core.kernel_core.config;

import cn.com.mpen.core.kernel_core.db.listener.InitTableListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库初始化默认配置
 *
 * @author fengshuonan
 * @Date 2018/7/30 下午12:22
 */
@Configuration
public class DbInitializerAutoConfiguration {

    @Bean
    public InitTableListener initTableListener() {
        return new InitTableListener();
    }
}


