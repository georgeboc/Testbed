package boundary.readers;

public interface Reader {
    String read(String path) throws RuntimeException;
}
