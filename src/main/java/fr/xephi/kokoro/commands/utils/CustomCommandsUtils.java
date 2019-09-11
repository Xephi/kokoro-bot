package fr.xephi.kokoro.commands.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class CustomCommandsUtils {

    /**
     *
     * @param dataFolder path to dataFolder, can be relative or absolute
     * @return an association of Guild Id -> (String -> String)
     */
    public static Map<String, Map<String, String>> loadFolder(String dataFolder, String folderToLoad) {
        Map<String, Map<String, String>> commands = new HashMap<>();
        Path dir = Paths.get(dataFolder, folderToLoad);
        if (!dir.toFile().exists())
            try {
                dir.toFile().mkdirs();
            } catch (final Exception e) {
                e.printStackTrace();
                return commands;
            }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {
                final String guildId = file.toFile().getName();
                Map<String, String> guildCommands = new HashMap<>();
                try ( DirectoryStream<Path> subStream = Files.newDirectoryStream(file)) {
                        for (Path command: subStream) {
                            final File commandFile = command.toFile();
                            final String args = new String(Files.readAllBytes(command), StandardCharsets.UTF_8);
                            guildCommands.put( commandFile.getName(), args );
                        }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                commands.put( guildId, guildCommands );
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return commands;
    }

    /**
     *
     * @param dataFolder path to dataFolder, can be relative or absolute
     * @param contentFolder can be: commands, twitter, musics
     * @param guildId discord guild id
     * @param fileName file to create
     * @param content content of the file
     * @return true if the file was created
     */
    public static boolean saveTo( String dataFolder, String contentFolder, String guildId, String fileName, String content ) {
        final File file = new File(dataFolder + File.separator + contentFolder + File.separator + guildId + File.separator + fileName );
        if (file.exists())
            return false;
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
                return false;
            if (!file.createNewFile())
                return false;
            RandomAccessFile stream = new RandomAccessFile(file, "rw");
            FileChannel channel = stream.getChannel();
            try ( FileLock lock = channel.tryLock() ) {
                stream.write(content.getBytes(StandardCharsets.UTF_8));
            } catch (final OverlappingFileLockException e) {
                stream.close();
                channel.close();
                return false;
            }
            stream.close();
            channel.close();
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
