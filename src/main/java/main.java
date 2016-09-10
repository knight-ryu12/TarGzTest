import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import api.compressTarGz;
/**
 * Created by chroma on 16/09/11.
 */
public class main {
    public static void main(String[] args) throws Exception {
        String baseDir = "test";
        String outDir = "out";
        String spath = System.getProperty("user.dir");
        File file = new File(baseDir);
        Path path = file.toPath();
        List<Path> pathList = new ArrayList<>();
        FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                pathList.add(file);
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(path,visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            compressTarGz.compresstargz(pathList,spath + "/" + outDir + "/" + "test.tar.gz");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
