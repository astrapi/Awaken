package com.example.tylerbwong.awaken.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Connor Wong
 */
public class Wake {
   /**
    * The target device's host name or ip address.
    */
   private String mHost;

   /**
    * The target device's MAC address.
    */
   private String mMac;

   /**
    * This is the optional SecureOn password. User should make sure password is enabled
    */

   private String mPass;

   /**
    * The port that WOL is used on (default is 7).
    */
   private int mPort = 7;

   private byte[] mBytes;

   /**
    * The length of the MAC address.
    */
   private final static int MAC_LENGTH = 6;

   /**
    * Magic packets must be 102 bytes. Length
    * of the UDP packet.
    */
   private final static int UDP_MULTIPLIER = 16;

   private final static int HEXADECIMAL = 16;
   /**
    * A delimiter for the target device's MAC
    * address.
    */
   private final static String MAC_DELIMITER = "(\\:|\\-)";

   private final static String PASS_DELIMITER = "(\\:|\\-)";
   /**
    * Invalid MAC address status message.
    */
   private final static String INVALID_MAC = "Invalid MAC Address";

   private final static String INVALID_PASS = "Invalid SecureOn Password";
   /**
    * Creates a new wakeable device using the default port 7.
    *
    * @param host the host name/ip address of the device
    * @param mac the MAC address of the device
    */
   public Wake(String host, String mac) {
      this.mHost = host;
      this.mMac = mac;
   }

   public Wake(String host, String mac, String pass) {
      this.mHost = host;
      this.mMac = mac;
      this.mPass = pass;
   }

   public Wake(String host, String mac, String pass, int port) {
      this.mHost = host;
      this.mMac = mac;
      this.mPass = pass;
      this.mPort = port;
   }

   /**
    * Creates a new wakeable device with a specified port
    * (usually 7 or 9).
    *
    * @param host the host name/ip address of the device
    * @param mac the MAC address of the device
    * @param port the port that WOL is used on
    */
   public Wake(String host, String mac, int port) {
      this.mHost = host;
      this.mMac = mac;
      this.mPort = port;
   }

   /**
    * Sends a magic packet to the target device to be woken up.
    */
   public void sendPacket() {
      try {
         byte[] macBytes = getMacBytes(mMac);
         mBytes = new byte[MAC_LENGTH + UDP_MULTIPLIER * macBytes.length];

         for (int index = 0; index < MAC_LENGTH; index++) {
            mBytes[index] = (byte) 0xff;
         }

         for (int index = MAC_LENGTH; index < mBytes.length; index += macBytes.length) {
            System.arraycopy(macBytes, 0, mBytes, index, macBytes.length);
         }

         InetAddress address = InetAddress.getByName(mHost);
         DatagramPacket packet = new DatagramPacket(mBytes, mBytes.length, address, mPort);
         DatagramSocket socket = new DatagramSocket();

         socket.send(packet);
         socket.close();
      }
      catch (Exception e) {

      }
   }

   /**
    * Gets the host name/ip address of the target device.
    *
    * @return the host name/ip address as String
    */
   public String getHost() {
      return mHost;
   }

   /**
    * Gets the MAC address of the target device.
    *
    * @return the mac address as a String
    */
   public String getMac() {
      return mMac;
   }

   /**
    * Gets the port that WOL works on.
    *
    * @return the wol port as an int
    */
   public int getPort() {
      return mPort;
   }

   private byte[] getMacBytes(String macStr) {
      byte[] bytes = new byte[MAC_LENGTH];
      String[] mac = macStr.split(MAC_DELIMITER);

      if (mac.length != MAC_LENGTH) {
         throw new IllegalArgumentException(INVALID_MAC);
      }

      try {
         for (int index = 0; index < MAC_LENGTH; index++) {
            bytes[index] = (byte) Integer.parseInt(mac[index], HEXADECIMAL);
         }
      }
      catch (NumberFormatException e) {
         throw new IllegalArgumentException(INVALID_MAC);
      }
      return bytes;
   }

   private byte[] getPassBytes(String passStr) {
      byte[] bytes = new byte[passStr.length()];
      String[] pass = passStr.split(PASS_DELIMITER);

      if (pass.length != 4 | pass.length != 6) {
         throw new IllegalArgumentException(INVALID_PASS);
      }

      try {
         for (int index = 0; index < pass.length; index++) {
            bytes[index] = (byte) Integer.parseInt(pass[index], HEXADECIMAL);
         }
      }
      catch (NumberFormatException e) {
         throw new IllegalArgumentException(INVALID_PASS);
      }
      return bytes;
   }
}
