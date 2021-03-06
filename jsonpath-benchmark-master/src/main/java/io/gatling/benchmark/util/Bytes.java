package io.gatling.benchmark.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.IOUtils;
import org.asynchttpclient.netty.util.Utf8ByteBufCharsetDecoder;

import java.io.IOException;
import java.io.InputStream;

public final class Bytes {

  public static final int MTU = 1500;

  public static byte[] readBytes(String path) {
    try {
      return IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static byte[][] split(byte[] full, int chunkSize) {

    int chunkNumber = (int) Math.ceil(((double) full.length) / chunkSize);
    byte[][] chunks = new byte[chunkNumber][];

    int start = 0;
    int chunkCount = 0;

    while (start < full.length) {

      int length = Math.min(full.length - start, chunkSize);
      byte[] chunk = new byte[length];
      System.arraycopy(full, start, chunk, 0, length);
      chunks[chunkCount] = chunk;
      chunkCount++;
      start += length;
    }

    return chunks;
  }

  public static InputStream toInputStream(byte[][] chunks) {
    return new ByteBufInputStream(Unpooled.wrappedBuffer(chunks), true);
  }

  public static String toString(byte[][] chunks) {
      ByteBuf buf = Unpooled.wrappedBuffer(chunks);
      try {
        return Utf8ByteBufCharsetDecoder.decodeUtf8(buf);
      } finally {
        buf.release();
      }
  }

  public static byte[] toBytes(byte[][] chunks) {
    int len = 0;
    for (byte[] chunk : chunks) {
      len += chunk.length;
    }
    byte[] total = new byte[len];

    int offset = 0;
    for (byte[] chunk : chunks) {
      System.arraycopy(chunk, 0, total, offset, chunk.length);
      offset += chunk.length;
    }

    return total;
  }

  public static char[] toChars(byte[][] chunks) {
      ByteBuf buf = Unpooled.wrappedBuffer(chunks);
      try {
        return Utf8ByteBufCharsetDecoder.decodeUtf8Chars(buf);
      } finally {
        buf.release();
      }
  }
}
