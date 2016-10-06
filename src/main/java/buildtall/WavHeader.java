package buildtall;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class WavHeader {
  // Offset 0
  // Contains the letters "RIFF" in ASCII form
  byte[] riffTag = new byte[4]; // ChunkID 4 bytes

  // Offset 4
  // This is the size of the entire file in bytes minus 8 bytes for the two fields not included in this count: ChunkID and ChunkSize.
  // Also = 4 + (8 + SubChunk1Size) + (8 + SubChunk2Size)
  byte[] riffLength = new byte[4]; // ChunkSize 4 bytes

  // Offset 8
  // Contains the letters "WAVE"
  byte[] waveTag = new byte[4];  // Format 4 bytes

  // Offset 12
  // Contains the letters "fmt "
  byte[] fmtTag = new byte[4]; // Subchunk1ID 4 bytes

  // Offset 16
  // 16 for PCM
  byte[] fmtLength; // Subchunk1Size 4 bytes

  // Offset 20
  // PCM = 1 (i.e. Linear quantization)
  // Values other than 1 indicate some form of compression.
  byte[] audioFormat; // AudioFormat 2 bytes

  // Offset 22
  // Mono = 1, Stereo = 2
  byte[] numChannels;  // NumChannels 2 bytes

  // Offset 24
  // 44100, 96000 etc
  byte[] sampleRate; // SampleRate 4 bytes

  // Offset 28
  // = SampleRate * NumChannels * BitsPerSample/8
  byte[] byteRate; // ByteRate 4 bytes

  // Offset 32
  // The number of bytes for one sample including all channels.
  // = NumChannels * BitsPerSample/8
  byte[] blockAlign; // BlockAlign 2 bytes

  // Offset 34
  // 8 bits = 8, 16 bits = 16
  byte[] bitsPerSample; // BitsPerSample 2 bytes

  // Offset 36
  // Contains the letters "data"
  byte[] dataTag = new byte[4]; // Subchunk2ID 4 bytes

  // Offset 40
  // This is the number of bytes in the data.
  // = NumSamples * NumChannels * BitsPerSample/8
  byte[] dataLength; // Subchunk2Size 4 bytes

  public WavHeader(int samplesPerSecond, int bitsPerSample, short channel, int numSamples) {
    this.riffTag = "RIFF".getBytes();
    this.riffLength = new byte[] { 0x1c, 0x30, 0x14, 0x00};
    this.waveTag = "WAVE".getBytes();
    this.fmtTag = "fmt ".getBytes();
    this.fmtLength = new byte[] {0x10, 0x00};
    this.audioFormat = new byte[]{0x00, 0x00, 0x01, 0x00};
    this.numChannels = new byte[]{0x01, 0x00};
    this.sampleRate = new byte[] {0x44, (byte)0xac, 0x00, 0x00};
    this.byteRate = new byte[] {0x44, (byte)0xac, 0x00, 0x00};
    this.blockAlign = new byte[] {0x01, 0x00};
    this.bitsPerSample = new byte[] {0x08, 0x00};
    this.dataTag = "data".getBytes();
    this.dataLength = ByteBuffer.allocate(4).putInt(numSamples).array();
  }

  public void writeToFile(File file) throws IOException {
    writeToOS(new FileOutputStream(file));
  }

  public void writeToOS(OutputStream os) throws IOException {
    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
    dos.write(riffTag);
    dos.write(riffLength);
    dos.write(waveTag);
    dos.write(fmtTag);
    dos.write(fmtLength);
    dos.write(audioFormat);
    dos.write(numChannels);
    dos.write(sampleRate);
    dos.write(byteRate);
    dos.write(blockAlign);
    dos.write(bitsPerSample);
    dos.write(dataTag);
    dos.write(dataLength);
    dos.flush();
  }

}
