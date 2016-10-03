package buildtall;


import java.io.*;

class SoundTest {
  public static void main(String [] args) throws IOException {
    WavHeader wavHeader = new WavHeader(
        Configure.SAMPLES_PER_SECOND,
        Configure.BITS_PER_SAMPLE,
        Configure.CHANNELS,
        Configure.NUM_SAMPLES
    );
    FileOutputStream output = new FileOutputStream(new File("noise-out.wav"));
    try {
      wavHeader.writeToOS(output);
    } catch(IOException ioe) {
      System.out.println(ioe.getMessage());
      System.exit(1);
    }

    WavWriter wavWriter = new WavWriter(Configure.BITS_PER_SAMPLE);
    wavWriter.addWavProvider(new SquareWave(200, 400));
    //wavWriter.addWavProvider(new SquareWave(200, 800));
    wavWriter.writeWav(Configure.NUM_SAMPLES, output);

  }
}
