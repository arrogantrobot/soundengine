package buildtall;

import java.io.*;
import java.util.Random;


public class WeinerProcess {
  Random r = new Random();

  private double getRandom() {
    return r.nextDouble() * 254;
  }

  private double clamp(double d) {
    if (d > 254.0) return 254.0;
    if (d < 1.0) return 1.0;
    return d;
  }

  private double getWeinerDouble(double t) {
    return clamp((r.nextGaussian() + t));
  }

  public byte[] getWeinerBytes(int numBytes, int initialValue) {
    byte[] answer = new byte[numBytes];
    double value = (float)initialValue;
    for(int i = 0; i < numBytes; i++) {
      value = getWeinerDouble(value);
      answer[i] = (byte)(value);
    }
    return answer;
  }

  public byte[] getBytes(int numBytes, int initialValue) throws IOException {
    ByteArrayOutputStream boas = new ByteArrayOutputStream();
    OutputStream os = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        boas.write(b);
      }
    };
    writeWeinerBytesToStream(numBytes, os);
    return boas.toByteArray();
  }

  public void writeWeinerBytesToStream(int numBytes, OutputStream os) throws IOException{
    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
    double value = 8f;
    for(int i = 0; i < numBytes; i++) {
      value = getWeinerDouble(value);
      dos.writeByte((int)value);
    }
    dos.close();
  }
}

