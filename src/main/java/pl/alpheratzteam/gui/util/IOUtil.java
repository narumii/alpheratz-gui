package pl.alpheratzteam.gui.util;

import org.lwjgl.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import static org.lwjgl.BufferUtils.*;

/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */

public final class IOUtil {

    private IOUtil() {
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        var newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @param resource   the resource to read
     * @param bufferSize the initial buffer size
     * @return the resource data
     * @throws IOException if an IO error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        var path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (var seekableByteChannel = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) seekableByteChannel.size() + 1);
                while (seekableByteChannel.read(buffer) != -1);
            }
        } else {
            try (var inputStream = IOUtil.class.getClassLoader().getResourceAsStream(resource); var readableByteChannel = Channels.newChannel(inputStream)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = readableByteChannel.read(buffer);
                    if (bytes == -1) {
                        break;
                    }

                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

}