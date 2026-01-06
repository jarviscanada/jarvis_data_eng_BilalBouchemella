package ca.jrvs.apps.practice;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexExcImp implements RegexExc{
    private static final Pattern JPEG_PATTERN = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*\\.(?i:jpg|jpeg)$");
    private static final Pattern IP_ADDRESS = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");
    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*$");

    @Override
    public boolean matchJpeg(String filename) {
        if (filename == null) return false;
        Matcher matcher = JPEG_PATTERN.matcher(filename);
        return matcher.matches();
    }

    @Override
    public boolean matchIp(String ip) {
        if (ip == null) return false;
        Matcher matcher = IP_ADDRESS.matcher(ip);
        return matcher.matches();
    }

    @Override
    public boolean isEmptyLine(String line) {
        if (line == null) return false;
        Matcher matcher = EMPTY_LINE_PATTERN.matcher(line);
        return matcher.matches();
    }
}

