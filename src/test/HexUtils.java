package test;

import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

public class HexUtils {

	private static char[] hexChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	public static byte[] toHex(byte[] digestByte) {
		byte[] rtChar = new byte[digestByte.length * 2];
		for (int i = 0; i < digestByte.length; i++) {
			byte b1 = (byte) (digestByte[i] >> 4 & 0x0f);
			byte b2 = (byte) (digestByte[i] & 0x0f);
			rtChar[i * 2] = (byte) (b1 < 10 ? b1 + 48 : b1 + 55);
			rtChar[i * 2 + 1] = (byte) (b2 < 10 ? b2 + 48 : b2 + 55);
		}
		return rtChar;
	}

	public static String toHexString(byte[] digestByte) {
		return new String(toHex(digestByte));
	}

	public static byte[] fromHex(byte[] sc) {
		byte[] res = new byte[sc.length / 2];
		for (int i = 0; i < sc.length; i++) {
			byte c1 = (byte) (sc[i] - 48 < 17 ? sc[i] - 48 : sc[i] - 55);
			i++;
			byte c2 = (byte) (sc[i] - 48 < 17 ? sc[i] - 48 : sc[i] - 55);
			res[i / 2] = (byte) (c1 * 16 + c2);
		}
		return res;
	}

	public static byte[] fromHexString(String hex) {
		return fromHex(hex.getBytes());
	}

	public static String encode(String in) {
		return new String(toHex(in.getBytes()));
	}

	public static byte[] strToByte(String inStr) {
		if (!StringUtils.isNumeric(inStr)) {
			return null;
		}
		ArrayList byteList = new ArrayList();
		for (int i = 0; i < inStr.length(); i++) {

			byteList.add(new Byte(Byte.parseByte(String.valueOf(inStr.charAt(i)))));
			// byteArray.add(new Byte(inStr.charAt(i)-47));
		}
		byte[] byteArray = new byte[byteList.size()];
		for (int j = 0; j < byteArray.length; j++) {
			byteArray[j] = ((Byte) byteList.get(j)).byteValue();

		}
		return byteArray;
	}
	public static String decode(String in) {
		return new String(fromHex(in.getBytes()));
	}

}
