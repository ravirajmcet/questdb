/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2023 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.cairo.vm.api;

import io.questdb.std.BinarySequence;
import io.questdb.std.Long256;
import io.questdb.std.Long256Acceptor;
import io.questdb.std.Unsafe;
import io.questdb.std.str.CharSink;
import io.questdb.std.str.DirectCharSequence;
import io.questdb.std.str.Utf8Sequence;
import io.questdb.std.str.Utf8SplitString;

import java.io.Closeable;

// readable
public interface MemoryR extends Closeable {

    long addressOf(long offset);

    default Utf8SplitString borrowUtf8SplitStringA() {
        throw new UnsupportedOperationException();
    }

    default Utf8SplitString borrowUtf8SplitStringB() {
        throw new UnsupportedOperationException();
    }

    @Override
    void close();

    void extend(long size);

    BinarySequence getBin(long offset);

    long getBinLen(long offset);

    boolean getBool(long offset);

    byte getByte(long offset);

    char getChar(long offset);

    /**
     * Returns UTF-16 encoded off-heap string.
     * <p>
     * Must return off-heap strings with stable pointers, i.e. once a string is returned,
     * its pointer remains actual until the memory is closed.
     */
    DirectCharSequence getDirectStr(long offset);

    double getDouble(long offset);

    float getFloat(long offset);

    int getIPv4(long offset);

    int getInt(long offset);

    long getLong(long offset);

    void getLong256(long offset, CharSink<?> sink);

    default void getLong256(long offset, Long256Acceptor sink) {
        long addr = addressOf(offset + Long.BYTES * 4);
        sink.setAll(
                Unsafe.getUnsafe().getLong(addr - Long.BYTES * 4),
                Unsafe.getUnsafe().getLong(addr - Long.BYTES * 3),
                Unsafe.getUnsafe().getLong(addr - Long.BYTES * 2),
                Unsafe.getUnsafe().getLong(addr - Long.BYTES)
        );
    }

    Long256 getLong256A(long offset);

    Long256 getLong256B(long offset);

    long getPageAddress(int pageIndex);

    int getPageCount();

    long getPageSize();

    short getShort(long offset);

    CharSequence getStrA(long offset);

    CharSequence getStrB(long offset);

    int getStrLen(long offset);

    Utf8Sequence getVarcharA(long offset, int size, boolean ascii);

    Utf8Sequence getVarcharB(long offset, int size, boolean ascii);

    long offsetInPage(long offset);

    int pageIndex(long offset);

    long size();
}
