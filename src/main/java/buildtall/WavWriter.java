package buildtall;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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

  private byte[] combineStreams(byte[] a, byte[] b){
    ByteBuffer bb = ByteBuffer.allocate(a.length);
    for (int i=0; i < a.length; i++) {
      bb.put(i, clamp((byte)((a[i] + b[i]))));
    }
    return bb.array();
  }

  private byte[] getMergedSamples(int samples) {
    ByteBuffer bb = ByteBuffer.allocate(samples);
    bb.put(wavProviders.get(0).getSamples(samples), 0, samples);

    if (wavProviders.size() > 1) {
      for(int i=1; i < wavProviders.size(); i++){
        byte[] cmb = combineStreams(bb.array(), wavProviders.get(i).getSamples(samples));
        for(int j=0;j<samples; j++) {
          bb.put(j, cmb[j]);
        }
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
