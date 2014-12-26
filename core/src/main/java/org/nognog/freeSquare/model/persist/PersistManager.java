package org.nognog.freeSquare.model.persist;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.nognog.freeSquare.model.Savable;
import org.nognog.freeSquare.model.player.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

/**
 * @author goshi 2014/10/28
 */
class PersistManager {
	private static final String charSet = "UTF-8"; //$NON-NLS-1$
	private static final Json json = new Json();

	private PersistManager() {
	}

	private static byte[] encryptionKey;

	static {
		try {
			Player player = loadPlayer();
			encryptionKey = toEncryptKey(player);
		} catch (Throwable t) {
			encryptionKey = null;
		}
	}

	private static String reverse(String target) {
		return new StringBuffer(target).reverse().toString();
	}

	private static byte[] toEncryptKey(Player player) {
		StringBuilder sb = new StringBuilder();
		sb.append(reverse(player.getName())).append(player.getStartDate().getTime());
		final int keySize = 16;
		return Arrays.copyOf(sb.toString().getBytes(), keySize);
	}

	private enum CipherMode {
		ENCRYPT_MODE(Cipher.ENCRYPT_MODE), DECRYPT_MODE(Cipher.DECRYPT_MODE);

		private int value;

		private CipherMode(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	private static byte[] encrypt(byte[] data) throws Exception {
		Cipher cipher = getCipher(CipherMode.ENCRYPT_MODE);
		byte[] encryptedData = cipher.doFinal(data);
		return encryptedData;
	}

	private static byte[] decrypt(byte[] data) throws Exception {
		Cipher cipher = getCipher(CipherMode.DECRYPT_MODE);
		byte[] decryptedData = cipher.doFinal(data);
		return decryptedData;
	}

	private static Cipher getCipher(CipherMode cipherMode) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey, "AES"); //$NON-NLS-1$
		Cipher cipher;
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //$NON-NLS-1$
		cipher.init(cipherMode.getValue(), secretKeySpec);
		return cipher;
	}

	/**
	 * @param saveItem
	 * @param saveObject
	 * @throws SaveFailureException
	 */
	public static <T extends Savable> void save(PersistItem<T> saveItem, T saveObject) throws SaveFailureException {
		if (saveItem.equals(PersistItem.PLAYER)) {
			savePlayer((Player) saveObject);
			return;
		}
		if (encryptionKey == null) {
			throw new SaveFailureException();
		}
		String saveJson = json.toJson(saveObject);
		try {
			byte[] encryptedSaveJson = encrypt(saveJson.getBytes(charSet));
			synchronized (saveItem) {
				FileHandle file = Gdx.files.local((saveItem.getFileName()));
				file.writeBytes(encryptedSaveJson, false);
			}
		} catch (Exception e) {
			throw new SaveFailureException(e);
		}

	}

	private static synchronized void savePlayer(Player saveObject) throws SaveFailureException {
		if (encryptionKey != null) {
			throw new SaveFailureException();
		}
		String playerJson = json.toJson(saveObject);
		String base64PlayerJson = Base64Coder.encodeString(playerJson);
		synchronized (PersistItem.PLAYER) {
			FileHandle saveFile = Gdx.files.local(PersistItem.PLAYER.getFileName());
			saveFile.writeString(base64PlayerJson, false);
			encryptionKey = toEncryptKey(saveObject);
		}

	}

	/**
	 * @param loadItem
	 * @return load object
	 * @throws LoadFailureException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Savable> T load(PersistItem<T> loadItem) throws LoadFailureException {
		if (encryptionKey == null) {
			throw new LoadFailureException();
		}
		if (loadItem == PersistItem.PLAYER) {
			return (T) loadPlayer();
		}

		try {
			byte[] enctyptedLoadJson = null;
			synchronized (loadItem) {
				enctyptedLoadJson = Gdx.files.local(loadItem.getFileName()).readBytes();
			}
			String loadJson = new String(decrypt(enctyptedLoadJson), charSet);
			T loadData = json.fromJson(loadItem.getSaveClass(), loadJson);
			if (!(loadData.isValid())) {
				throw new InvalidLoadDataException();
			}
			return loadData;

		} catch (Throwable t) {
			throw new LoadFailureException(t);
		}

	}

	private static synchronized Player loadPlayer() throws LoadFailureException {
		try {
			String base64PlayerJson = null;
			synchronized (PersistItem.PLAYER) {
				base64PlayerJson = Gdx.files.local(PersistItem.PLAYER.getFileName()).readString();
			}
			String playerJson = Base64Coder.decodeString(base64PlayerJson);
			Player player = json.fromJson(Player.class, playerJson);

			if (!player.isValid()) {
				throw new InvalidLoadDataException();
			}
			return player;

		} catch (Throwable t) {
			throw new LoadFailureException(t);
		}
	}

	/**
	 * @param saveItem
	 * @return file of saveItem exists
	 */
	public static boolean saveItemExists(PersistItem<?> saveItem) {
		try {
			FileHandle file = Gdx.files.local(saveItem.getFileName());
			return file.exists();
		} catch (Exception e) {
			return false;
		}
	}
}