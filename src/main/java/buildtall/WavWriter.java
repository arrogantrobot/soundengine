package buildtall;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WavWriter {
  short bitRate;

  List<WavProvider> wavProviders;

  public WavWriter(short bitRate) {
    this.bitRate = bitRate;
    this.wavProviders = new ArrayList<WavProvider>();
  }

  public void addWavProvider(WavProvider wavProvider) {
    this.wavProviders.add(wavProvider);
  }

  private byte clamp(byte b) {
    if (b > 120) return 120;
    if (b < -120) return -120;
    return b;
  }

  private ByteBuffer combineStreams(ByteBuffer a, ByteBuffer b){
    int size = a.capacity();
//    System.out.println(Arrays.toString(a.array()));
//    System.out.println(Arrays.toString(b.array()));
    ByteBuffer bb = ByteBuffer.allocate(size);
    for (int i=0; i < size; i++) {
      bb.put(clamp((byte)((a.get() + b.get()))));
    }
    bb.flip();
    return bb;
  }

  private byte[] getMergedSamples(int samples) {
    ByteBuffer bb = ByteBuffer.allocate(samples);
    bb.put(wavProviders.get(0).getSamples(samples));
    bb.flip();

    if (wavProviders.size() > 1) {
      for(int i=1; i < wavProviders.size(); i++){
        ByteBuffer cmb = combineStreams(bb.duplicate(), ByteBuffer.wrap(wavProviders.get(i).getSamples(samples)));
        bb.position(0);
        bb.put(cmb);
      }
    }
    return bb.array();
  }

  public void writeWav(int samples, OutputStream os) throws IOException {
    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
    dos.write(getMergedSamples(samples));
    dos.flush();
    dos.close();
  }
}
