/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.inlong.sdk.transform.decode;

import org.apache.parquet.io.DelegatingSeekableInputStream;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.SeekableInputStream;

import java.io.ByteArrayInputStream;

public class ParquetInputByteArray implements InputFile {

    private final byte[] data;

    private class SeekableByteArrayInputStream extends ByteArrayInputStream {

        public SeekableByteArrayInputStream(byte[] buf) {
            super(buf);
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public int getPos() {
            return this.pos;
        }
    }

    public ParquetInputByteArray(byte[] data) {
        this.data = data;
    }

    @Override
    public long getLength() {
        return this.data.length;
    }

    @Override
    public SeekableInputStream newStream() {
        return new DelegatingSeekableInputStream(new SeekableByteArrayInputStream(this.data)) {

            @Override
            public void seek(long newPos) {
                ((SeekableByteArrayInputStream) this.getStream()).setPos((int) newPos);
            }

            @Override
            public long getPos() {
                return ((SeekableByteArrayInputStream) this.getStream()).getPos();
            }
        };
    }
}
