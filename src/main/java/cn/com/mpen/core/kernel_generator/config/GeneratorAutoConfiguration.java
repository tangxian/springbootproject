package cn.com.mpen.core.kernel_generator.config;

import cn.com.mpen.core.kernel_generator.db.DbInfoInitializer;
import cn.com.mpen.core.kernel_generator.modular.controller.CodeController;
import cn.com.mpen.core.kernel_generator.modular.service.TableService;
import cn.com.mpen.core.kernel_core.db.DbInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GeneratorAutoConfiguration {
    @Bean
    public CodeController codeController() {
        return new CodeController();
    }

    @Bean
    public TableService tableService() {
        return new TableService();
    }

    @Bean
    public DbInitializer dbInitializer() {
        return new DbInfoInitializer();
    }
}
