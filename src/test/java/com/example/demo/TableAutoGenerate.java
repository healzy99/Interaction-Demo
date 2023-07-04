package com.example.demo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableAutoGenerate {
    // 数据源
    private static final String DATA_SOURCE_URL = "jdbc:mysql://gz-cynosdbmysql-grp-11bgkkut.sql.tencentcdb.com:24437/gt4_dh?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&serverTimezone=GMT%2B8";
    // 填写代码生成的目录(不在需要更改)
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + File.separator + "src/main/java";

    public static void main(String[] args) {

        FastAutoGenerator.create(DATA_SOURCE_URL, "root", "R@ymon123")
                // 全局配置
                .globalConfig((scanner, builder) -> builder.author(scanner.apply("请输入作者名称？"))
                        .disableOpenDir()
                        .outputDir(OUTPUT_DIR))
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent("com.example.demo"))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名,多个英文逗号分隔,所有则输入 all")))
                        .addTablePrefix("t_")
                        .controllerBuilder()
                        .enableRestStyle()
                        .enableHyphenStyle()
                        .entityBuilder()
                        .enableLombok()
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                        ).build())
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
