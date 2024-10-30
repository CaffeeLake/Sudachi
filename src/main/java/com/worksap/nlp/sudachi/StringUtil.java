/*
 * Copyright (c) 2017-2022 Works Applications Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.worksap.nlp.sudachi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class StringUtil {
    private StringUtil() {
    }

    public static String readFully(URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return readFully(inputStream);
        }
    }

    public static String readFully(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            return readFully(is);
        }
    }

    public static String readFully(InputStream stream) throws IOException {
        InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        CharBuffer cb = CharBuffer.allocate(1024);
        while (isr.read(cb) != -1) {
            cb.flip();
            sb.append(cb);
            cb.clear();
        }
        return sb.toString();
    }

    public static ByteBuffer readAllBytes(URL url) throws IOException {
        return readAllBytes(url, ByteOrder.LITTLE_ENDIAN);
    }

    public static ByteBuffer readAllBytes(URL url, ByteOrder order) throws IOException {
        try (InputStream is = url.openStream()) {
            return readAllBytes(is, order);
        }
    }

    public static ByteBuffer readAllBytes(InputStream inputStream) throws IOException {
        return readAllBytes(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    public static ByteBuffer readAllBytes(InputStream inputStream, ByteOrder order) throws IOException {
        byte[] buffer = new byte[inputStream.available() + 1024];
        int offset = 0;

        while (true) {
            int nread = inputStream.read(buffer, offset, buffer.length - offset);
            if (nread >= 0) {
                offset += nread;
                if (offset == buffer.length) {
                    buffer = Arrays.copyOf(buffer, buffer.length * 2);
                }
            } else {
                break;
            }
        }
        ByteBuffer bbuf = ByteBuffer.wrap(buffer);
        bbuf.limit(offset);
        bbuf.order(order);
        return bbuf;
    }
}
