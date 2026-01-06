package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Paths;


public class JavaGrepLambdaImp extends JavaGrepImp{

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootpath outfile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception e) {
            javaGrepLambdaImp.logger.error("ERROR: unable to process", e);
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        if (inputFile == null || !inputFile.isFile()) {
            throw new IllegalArgumentException("Input file is invalid");
        }

        try (Stream<String> lines = Files.lines(inputFile.toPath())){
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Failed to read file: {}", inputFile.getAbsolutePath(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<File> listFiles(String rootDir) {
        Path root = Paths.get(rootDir);

        if (!Files.exists(root)) {
            logger.warn("Root path does not exist: {}", rootDir);
            return Collections.emptyList();
        }

        if (!Files.isDirectory(root)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.walk(root)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Failed to traverse directory: {}", rootDir, e);
            return Collections.emptyList();
        }
    }
}
