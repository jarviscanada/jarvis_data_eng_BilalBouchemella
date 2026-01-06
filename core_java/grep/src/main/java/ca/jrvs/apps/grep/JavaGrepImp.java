package ca.jrvs.apps.grep;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;
import java.util.regex.Pattern;

public class JavaGrepImp implements  JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String rootPath;
    private String regex;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootpath outfile");
        }

        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("ERROR: unable to process", ex);
        }
    }

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();

        for (File file : listFiles(rootPath)) {
            for (String line : readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }

        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        File root = new File(rootDir);

        if (!root.exists()){
            logger.warn("Root path does not exist: {}", rootDir);
        } else if (!root.isDirectory() || root.listFiles() == null) {
            return files;
        }

        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(listFiles(file.getAbsolutePath()));
            } else {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) {
        if (inputFile == null || !inputFile.isFile()) {
            throw new IllegalArgumentException("Input file is invalid");
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile)))
        {
            String line = br.readLine();
            while (line != null){
                lines.add(line);
                line = br.readLine();
            }

        } catch (IOException e) {logger.error("Failed to read file: {}", inputFile.getAbsolutePath(), e);}

        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        if (line == null) {
            return false;
        }

        return Pattern.compile(regex).matcher(line).find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        if (lines == null) {
            throw new IllegalArgumentException("Lines cannot be null");
        }

        try (
                FileOutputStream fos = new FileOutputStream(outFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(osw)
        ) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            logger.error("Failed to write output file: {}", outFile, e);
            throw e;
        }
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}