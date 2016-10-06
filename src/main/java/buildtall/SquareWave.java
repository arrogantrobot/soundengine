package buildtall;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SquareWave implements WavProvider {
  int amplitude;
  int samplesPerCycle;
  byte cycleLow;
  byte cycleHigh;

  ByteBuffer wave;

  public SquareWave(int amplitude, int samplesPerCycle) {
    this.amplitude = amplitude;
    this.samplesPerCycle = samplesPerCycle;
    this.cycleLow = (byte)(-(amplitude/2));
    this.cycleHigh = (byte)(cycleLow + amplitude);
    this.wave = getWave();
  }

  private ByteBuffer getWave() {
    int length = samplesPerCycle / 2;
    ByteBuffer bb = ByteBuffer.allocate(samplesPerCycle);
    for (int i=0; i< length; i++) {
      bb.put(cycleHigh);
    }
    for (int i=0; i< length; i++) {
      bb.put(cycleLow);
    }
    bb.flip();
    return bb;
  }

  private ByteBuffer getBytes(int samples) {
    int wholeCopies = samples / samplesPerCycle;
    int fractions = samples % samplesPerCycle;
    ByteBuffer bb = ByteBuffer.allocate(samples);
    for (int i=0; i < wholeCopies; i++) {
      bb.put(wave.duplicate());
    }

    bb.put(wave.array(), 0, fractions);
    bb.flip();
    return bb;
  }

  @Override
  public byte[] getSamples(int samples) {
    return getBytes(samples).array();
  }
}
