package com.classichabbo.goldfish.networking.wrappers;

import com.classichabbo.goldfish.networking.encoding.Base64Encoding;
import com.classichabbo.goldfish.networking.encoding.VL64Encoding;
import com.classichabbo.goldfish.networking.errors.MalformedPacketException;
import com.classichabbo.goldfish.util.StringUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class Request {
    private final ByteBuf buffer;
    private final String header;
    private final int headerId;

    public Request(ByteBuf buffer) {
        this.buffer = buffer;
        this.header = new String(new byte[]{buffer.readByte(), buffer.readByte()});
        ;
        this.headerId = Base64Encoding.decode(this.header.getBytes());
    }

    public int readInt() {
        byte[] remaining = this.remainingBytes();

        int length = remaining[0] >> 3 & 7;
        int value = VL64Encoding.decode(remaining);
        readBytes(length);

        return value;
    }

    public boolean readBool() {
        return readInt() == 1;
    }

    public String readClientString() {
        try {
            String val = "";
            byte[] data = remainingBytes();

            if (data[0] != (char) 2) {
                int position = 0;

                for (int i = 0; i < data.length; i++) {
                    if (data[i] == (char) 2) {
                        break;
                    }

                    position = i;
                }

                val = new String(this.readBytes(position + 1), StringUtil.getCharset());
            }

            this.readBytes(1);
            return val;
        } catch (Exception ex) {
            return null;
        }
    }

    public byte[] remainingBytes() {
        this.buffer.markReaderIndex();

        byte[] bytes = new byte[this.buffer.readableBytes()];
        buffer.readBytes(bytes);

        this.buffer.resetReaderIndex();
        return bytes;
    }

    public byte[] readBytes(int len) {
        byte[] payload = new byte[len];
        this.buffer.readBytes(payload);
        return payload;
    }

    public String contents() {
        byte[] remiainingBytes = this.remainingBytes();

        if (remiainingBytes != null) {
            return new String(remiainingBytes);
        }

        return null;
    }

    public String getMessageBody() {
        String consoleText = this.buffer.toString(Charset.defaultCharset());

        for (int i = 0; i < 14; i++) {
            consoleText = consoleText.replace(Character.toString((char)i), "{" + i + "}");
        }

        return consoleText;
    }

    public String getHeader() {
        return header;
    }

    public int getHeaderId() {
        return headerId;
    }
}