/*
 * Decompiled with CFR 0_115.
 */
package com.webapp.utils.file;

import com.webapp.utils.file.EncodeUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

public final class FilesUtils {
    public static void replaceLine() {
    }

    public static void insertLines(Path path, String buffer, int preLines) throws Exception {
        FileChannel file = FileChannel.open(FilesUtils.notExistCreate(path), StandardOpenOption.READ, StandardOpenOption.WRITE);
        String temp = buffer + "\n";
        long fileSize = file.size();
        long totalSize = fileSize + (long)temp.length();
        MappedByteBuffer mbb = file.map(FileChannel.MapMode.READ_WRITE, 0, totalSize);
        int position = FilesUtils.insertBeforeSize(mbb, preLines);
        int afterSize = (int)(fileSize - (long)position);
        byte[] tempByte = new byte[afterSize];
        MappedByteBuffer insertAfter = file.map(FileChannel.MapMode.PRIVATE, position, afterSize);
        insertAfter.get(tempByte);
        mbb.position(position);
        mbb.put(temp.getBytes());
        mbb.put(tempByte);
        mbb.force();
        file.close();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int writeLines(Path path, ByteBuffer buffer) {
        AbstractInterruptibleChannel file = null;
        try {
            file = FileChannel.open(FilesUtils.notExistCreate(path), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            int n = ((FileChannel) file).write(buffer);
            return n;
        }
        catch (IOException e) {
            e.printStackTrace();
            int e1 = 0;
            return e1;
        }
        finally {
            try {
                if (file != null) {
                    file.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int writeLines(Path path, byte[] buffer) {
        return FilesUtils.writeLines(path, EncodeUtils.encode(buffer));
    }

    public static int writeLines(Path path, CharBuffer buffer) {
        return FilesUtils.writeLines(path, EncodeUtils.encode(buffer));
    }

    public static int writeLines(Path path, String str) {
        return FilesUtils.writeLines(path, EncodeUtils.encode(str));
    }

    public static int writeLines(Path path, List<String> lines, String split) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < lines.size(); ++i) {
            buffer.append(lines.get(i) + split);
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        return FilesUtils.writeLines(path, buffer.toString());
    }

    public static int writeLines(Path path, List<String> lines) {
        return FilesUtils.writeLines(path, lines, "\n");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int appendLine(Path path, ByteBuffer buffer, boolean checkLineMark) {
        buffer.rewind();
        AbstractInterruptibleChannel file = null;
        try {
            file = FileChannel.open(FilesUtils.notExistCreate(path), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            if (checkLineMark) {
                if (!FilesUtils.existLineMark(path)) {
                    ((FileChannel) file).write(EncodeUtils.encode("\n"));
                }
            } else {
                ((FileChannel) file).write(EncodeUtils.encode("\n"));
            }
            int n = ((FileChannel) file).write(buffer);
            return n;
        }
        catch (IOException e) {
            e.printStackTrace();
            int e1 = 0;
            return e1;
        }
        finally {
            try {
                if (file != null) {
                    file.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int appendLine(Path path, ByteBuffer buffer) {
        return FilesUtils.appendLine(path, buffer, false);
    }

    public static int appendLine(Path path, byte[] byteArray, boolean checkLineMark) {
        return FilesUtils.appendLine(path, EncodeUtils.encode(byteArray), checkLineMark);
    }

    public static int appendLine(Path path, byte[] byteArray) {
        return FilesUtils.appendLine(path, EncodeUtils.encode(byteArray));
    }

    public static int appendLine(Path path, CharBuffer buffer, boolean checkLineMark) {
        return FilesUtils.appendLine(path, EncodeUtils.encode(buffer), checkLineMark);
    }

    public static int appendLine(Path path, CharBuffer buffer) {
        return FilesUtils.appendLine(path, EncodeUtils.encode(buffer));
    }

    public static int appendLine(Path path, String str, boolean checkLineMark) {
        return FilesUtils.appendLine(path, EncodeUtils.encode(str), checkLineMark);
    }

    public static int appendLine(Path path, String str) {
        return FilesUtils.appendLine(path, EncodeUtils.encode(str));
    }

    public static int appendLine(Path path, List<String> lines, boolean checkLineMark) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < lines.size(); ++i) {
            buffer.append(lines.get(i) + "\n");
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        return FilesUtils.appendLine(path, buffer.toString(), checkLineMark);
    }

    public static int appendLine(Path path, List<String> lines) {
        return FilesUtils.appendLine(path, lines, false);
    }

    public static List<String> readAllLines(Path path) {
        try {
            return Files.readAllLines(path, EncodeUtils.getUTFCharset());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static /* varargs */ Path createDirAndFile(String file, String ... dir) {
        Path path = null;
        try {
            path = Paths.get(Files.createDirectories(Paths.get("", dir), new FileAttribute[0]).toString(), file);
            if (Files.notExists(path, new LinkOption[0])) {
                return Files.createFile(path, new FileAttribute[0]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    public static Path createDirAndFile(String file, Path path) {
        return FilesUtils.createDirAndFile(file, path.toString());
    }

    public static Path createDirAndFile(Path path) {
        return FilesUtils.createDirAndFile(path.getFileName().toString(), path.getParent());
    }

    public static Path notExistCreate(Path path) {
        return FilesUtils.createDirAndFile(path);
    }

    public static boolean existLineMark(Path path) {
        if (Files.notExists(path, new LinkOption[0])) {
            return false;
        }
        try {
            FileChannel file = FileChannel.open(path, StandardOpenOption.READ);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1);
            long fileSize = Files.size(path);
            if (fileSize > 0 && file.read(byteBuffer, fileSize - 1) > 0 && (char)byteBuffer.get(0) == '\n') {
                return true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int insertBeforeSize(ByteBuffer buffer, int lines) {
        int position = 0;
        if (lines == 0) {
            return 0;
        }
        buffer.rewind();
        for (int i = 0; i < buffer.limit(); ++i) {
            if ((char)buffer.get() != '\n' || ++position < lines) continue;
            return i + 1;
        }
        return buffer.limit();
    }

    public static int whichLineSize(ByteBuffer buffer, int line) {
        int position = 0;
        if (line == 0) {
            return 0;
        }
        int count = 0;
        boolean flag = false;
        buffer.rewind();
        for (int i = 0; i < buffer.limit(); ++i) {
            if (position + 1 == line) {
                ++count;
                flag = true;
            }
            if ((char)buffer.get() != '\n') continue;
            if (flag) break;
            ++position;
        }
        return count;
    }

    public static void cleaner(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction<Object>(){

            @Override
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

}
