package api;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by chroma on 16/09/11.
 */
public class compressTarGz {
    public static void compresstargz(List<Path> inputFiles, String outputFile) throws Exception {
        // tarファイル名を作成
        String outputTarFile = outputFile.replace(".tar.gz", ".tar");

        // tarアーカイブ作成
        try (FileOutputStream fos = new FileOutputStream(outputTarFile);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(fos)) {
            // 入力ファイルの数だけエントリーを追加
            for (Path fileName : inputFiles) {
                // フォルダの場合は次へ
                System.out.println(fileName.toString());
                if (fileName.endsWith("/")) {
                    File file = new File(fileName.toString());
                    TarArchiveEntry entry = new TarArchiveEntry(file, fileName.toString());
                    taos.putArchiveEntry(entry);
                    taos.closeArchiveEntry();
                    continue;
                }

                // 入力ファイル指定
                File file = new File(fileName.toString());
                TarArchiveEntry entry = new TarArchiveEntry(file, fileName.toString());
                taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
                taos.putArchiveEntry(entry);
                try (FileInputStream fis = new FileInputStream(fileName.toFile());
                     BufferedInputStream bis = new BufferedInputStream(fis)) {
                    // エントリーの中身を出力
                    int size = 0;
                    byte[] buf = new byte[1024];
                    while ((size = bis.read(buf)) > 0) {
                        taos.write(buf, 0, size);
                    }
                }

                // エントリー1つ文の出力を終了
                taos.closeArchiveEntry();
            }
        }

        // gzip圧縮
        try (FileInputStream fis = new FileInputStream(outputTarFile);
             BufferedInputStream bis = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream(outputFile);
             GzipCompressorOutputStream archive = new GzipCompressorOutputStream(fos)) {
            // エントリーの中身を出力
            int size = 0;
            byte[] buf = new byte[1024];
            while ((size = bis.read(buf)) > 0) {
                archive.write(buf, 0, size);
            }
        }
            File file = new File(outputTarFile);
            if(!file.delete()){ throw new AssertionError("Failed to delete File!"); }
    }
}
