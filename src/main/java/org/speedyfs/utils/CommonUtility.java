package org.speedyfs.utils;

public class CommonUtility {
	String encryptionKey = "ATTMNPSi";
	/**
	 * Returns album name without '~' string from end.
	 * 
	 * @param actualAlbumName
	 * @return
	 */
	public String getVirtualAlbumName(String actualAlbumName) {
		String albumName = actualAlbumName.substring(0, actualAlbumName.lastIndexOf("~"));
		return albumName;
	}
	
	
	/**
	 * This method decrypts the content
	 * @param encryptedContent
	 * @return
	 */
	public String decrypt(String encryptedContent) {
		StringBuffer decrypted = new StringBuffer();	
		for (int i = 0; i < encryptedContent.length(); i+=2) {
			int j = i/2 + 1;			
			int hexVal = Integer.parseInt(encryptedContent.substring(i, Math.min(i + 2, encryptedContent.length())), 16);
			int encryptionKeyIndex = (j % encryptionKey.length() - encryptionKey.length() * ((j % encryptionKey.length()) == 0 ? -1 : 0)) - 1;
			char decryptedChar = encryptionKey.substring(encryptionKeyIndex, encryptionKeyIndex + 1).charAt(0);
			int decryptedAsc = (int) decryptedChar;
			char decryptedXorChar = (char) (hexVal ^ decryptedAsc);			
			decrypted.append(decryptedXorChar);
		}		
		return decrypted.toString();
	}

}
