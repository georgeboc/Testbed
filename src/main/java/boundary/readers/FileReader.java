package boundary.readers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader implements Reader {
    @Override
    public String read(String path) throws RuntimeException {
        try {
            return Files.readString(Paths.get(path));
        } catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
