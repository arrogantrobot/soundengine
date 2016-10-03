package buildtall;

import java.nio.ByteBuffer;

public class SquareWave implements WavProvider {
  int amplitude;
  int samplesPerCycle;
  byte cycleLow = 1;
  byte cycleHigh = (byte)(cycleLow + amplitude);

  ByteBuffer wave;

  public SquareWave(int amplitude, int samplesPerCycle) {
    this.amplitude = amplitude;
    this.samplesPerCycle = samplesPerCycle;
    this.wave = getWave();
  }

  private ByteBuffer getWave() {
    int length = samplesPerCycle / 2;
    ByteBuffer bb = ByteBuffer.allocate(samplesPerCycle);
    for (int i=0; i< length; i++) {
      bb.put(i, cycleHigh);
    }
    for (int i=0; i< length; i++) {
      bb.put(length, cycleLow);
    }
    return bb;
  }

  private ByteBuffer getBytes(int samples) {
    int wholeCopies = samples / samplesPerCycle;
    int fractions = samples % samplesPerCycle;
    ByteBuffer bb = ByteBuffer.allocate(samples);

    for (int i=0; i < wholeCopies; i++) {
      for(byte b : wave.array()) {
        bb.put(b);
      }
    }
    int fractionStart = wholeCopies * samplesPerCycle;
    for(int i=fractionStart; i < samples; i++) {
      bb.put(i, wave.get(i-fractionStart));
    }

    return bb;
  }

  @Override
  public byte[] getSamples(int samples) {
    return getBytes(samples).array();
  }
}
