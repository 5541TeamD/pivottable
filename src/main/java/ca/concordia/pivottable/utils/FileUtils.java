package ca.concordia.pivottable.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class FileUtils {

    /**
     * Quick way to read a UTF-8 file into a string
     * @param filePath the path of the file (it can be a file on disk or in resources)
     * @return The contents of the file
     * @throws IOException in case of error reading the file
     */
    public static String readFileContents(String filePath) throws IOException {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(filePath));
        } catch (NoSuchFileException nsfe) {
            URL url = FileUtils.class.getResource(filePath);
            try {
                encoded = Files.readAllBytes(Paths.get(url.toURI()));
            } catch (URISyntaxException uriSE) {
                throw new IOException(uriSE.getMessage());
            }
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

}
