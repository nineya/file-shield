package com.nineya.shield.config;

import com.nineya.shield.config.properties.ShieldProperties;
import com.nineya.shield.model.enums.ModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

/**
 * @author 殇雪话诀别
 * 2021/9/7
 * 命令行配置解析
 */
@Slf4j
public class CommandConfiguration {
    private CommandLine cmd;
    private final String shortHelp = "h";
    private final String shortConfig = "c";
    private final String shortAlgo = "a";
    private final String shortMode = "m";
    private final String shortDigest = "d";

    private CommandConfiguration(String[] args) {
        Options options = new Options();
        options.addOption(shortHelp, "help", false, "Print the help information");
        options.addOption(shortConfig, "config", true, "Specifying a configuration file");
        options.addOption(shortAlgo, "algo", true, "Specifies the digest algorithm to use");
        options.addOption(shortMode, "mode", true, "Specify mode Build or Check");
        options.addOption(shortDigest, "digest", true, "Specifies the digest file to use for check");

        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption(shortHelp)) {
                printHelp(options);
                System.exit(0);
            }
        } catch (ParseException e) {
            printHelp(options);
            log.error("command cli params error.", e);
            System.exit(1);
        }
    }

    private void printHelp(Options options) {
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(120);
        hf.printHelp("java -jar file-shield.jar",
                "\nwhere options include:",
                options,
                null,
                true);
    }

    public static CommandConfiguration instance(String[] args) {
        return new CommandConfiguration(args);
    }

    public String configPath() {
        return cmd.getOptionValue(shortConfig, "config.yaml");
    }

    public ShieldProperties config(ShieldProperties properties) {
        if (cmd.hasOption(shortAlgo)) {
            properties.setAlgo(cmd.getOptionValue(shortAlgo));
        }
        if (cmd.hasOption(shortMode)) {
            properties.setMode(ModeEnum.value(cmd.getOptionValue(shortMode)));
        }
        if (cmd.hasOption(shortDigest)) {
            properties.setDigestPath(cmd.getOptionValue(shortDigest));
        }
        return properties;
    }
}
