package buildtall;

public class BrownNoise8Bit implements WavProvider {
  short bitRate;
  WeinerProcess weinerProcess;
  int initialValue;
  public BrownNoise8Bit() {
    this.weinerProcess = new WeinerProcess();
    this.initialValue = 128;
  }

  @Override
  public byte[] getSamples(int samples) {
    return weinerProcess.getWeinerBytes(samples, initialValue);
  }
}
