package com.classichabbo.goldfish.networking.wrappers;

import com.classichabbo.goldfish.networking.encoding.Base64Encoding;
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
        this.header = new String(new byte[] { buffer.readByte(), buffer.readByte() });;
        this.headerId = Base64Encoding.decode(this.header.getBytes());
    }

    public String readClientString() {
        try {
            byte[] data = remainingBytes();

            int position = 0;

            for (int i = 0; i < data.length; i++) {
                if (data[i] == (byte) 2) {
                    break;
                }

                position = i;
            }

            String readData = new String(this.readBytes(position + 1), StringUtil.getCharset());
            this.readBytes(1);
            return readData;

        } catch (Exception ex) {
            return null;
        }
    }

    public byte[] remainingBytes() throws MalformedPacketException {
        try {
            this.buffer.markReaderIndex();

            byte[] bytes = new byte[this.buffer.readableBytes()];
            buffer.readBytes(bytes);

            this.buffer.resetReaderIndex();
            return bytes;
        } catch (Exception ex) {
            throw new MalformedPacketException("The packet sent to the server was malformed.");
        }
    }

    public byte[] readBytes(int len) throws MalformedPacketException {
        try {
            byte[] payload = new byte[len];
            this.buffer.readBytes(payload);
            return payload;
        } catch (Exception ex) {
            throw new MalformedPacketException("The packet sent to the server was malformed.");
        }
    }

    public String contents() throws MalformedPacketException {
        try {
            byte[] remiainingBytes = this.remainingBytes();

            if (remiainingBytes != null) {
                return new String(remiainingBytes);
            }

            return null;
        } catch (Exception ex) {
            throw new MalformedPacketException("The packet sent to the server was malformed.");
        }
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