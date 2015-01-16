package org.nognog.freeSquare.model.persist;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.nognog.freeSquare.model.Savable;
import org.nognog.freeSquare.model.player.PlayLog;

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
			PlayLog playlog = loadPlayLog();
			encryptionKey = toEncryptKey(playlog);
		} catch (Throwable t) {
			encryptionKey = null;
		}
	}

	private static String reverse(String target) {
		return new StringBuffer(target).reverse().toString();
	}

	private static byte[] toEncryptKey(PlayLog playlog) {
		final int keySize = 16;
		return Arrays.copyOf(reverse(playlog.getLog()).getBytes(), keySize);
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

	private static Cipher getCipher(CipherMode cipherMode) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey, "AES"); //$NON-NLS-1$
		Cipher cipher;
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //$NON-NLS-1$
		cipher.init(cipherMode.getValue(), secretKeySpec);
		return cipher;
	}

	public static <T extends Savable> void save(PersistItem<T> saveItem, T saveObject) throws SaveFailureException {
		save(saveItem, saveObject, encryptionKey);
	}

	/**
	 * @param saveItem
	 * @param saveObject
	 * @param key
	 * @throws SaveFailureException
	 */
	static <T extends Savable> void save(PersistItem<T> saveItem, T saveObject, byte[] key) throws SaveFailureException {
		if (saveItem.equals(PersistItem.PLAY_LOG)) {
			savePlayLog((PlayLog) saveObject);
			return;
		}
		if (key == null) {
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

	private static synchronized void savePlayLog(PlayLog saveObject) {
		String playlogJson = json.toJson(saveObject);
		String base64PlaylogJson = Base64Coder.encodeString(playlogJson);
		byte[] newEncryptionKey = toEncryptKey(saveObject);
		synchronized (PersistItem.PLAY_LOG) {
			FileHandle saveFile = Gdx.files.local(PersistItem.PLAY_LOG.getFileName());
			saveFile.writeString(base64PlaylogJson, false);
			if (encryptionKey != null) {
				resavePersistItems(encryptionKey, newEncryptionKey);
			}
			encryptionKey = toEncryptKey(saveObject);
		}

	}

	private static void resavePersistItems(byte[] oldEncryptionKey, byte[] newEncryptionKey) {
		for (PersistItem<?> item : PersistItem.values()) {
			if (item == PersistItem.PLAY_LOG) {
				continue;
			}
			try {
				item.changeSaveEncryptionKey(oldEncryptionKey, newEncryptionKey);
			} catch (SaveFailureException | LoadFailureException e) {
				// skip
			}

		}
	}

	public static <T extends Savable> T load(PersistItem<T> loadItem) throws LoadFailureException {
		return load(loadItem, encryptionKey);
	}

	/**
	 * @param loadItem
	 * @return load object
	 * @throws LoadFailureException
	 */
	@SuppressWarnings("unchecked")
	static <T extends Savable> T load(PersistItem<T> loadItem, byte[] key) throws LoadFailureException {
		if (key == null) {
			throw new LoadFailureException();
		}
		if (loadItem == PersistItem.PLAY_LOG) {
			return (T) loadPlayLog();
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

	private static synchronized PlayLog loadPlayLog() throws LoadFailureException {
		try {
			String base64PlaylogJson = null;
			synchronized (PersistItem.PLAY_LOG) {
				base64PlaylogJson = Gdx.files.local(PersistItem.PLAY_LOG.getFileName()).readString();
			}
			String playlogJson = Base64Coder.decodeString(base64PlaylogJson);
			PlayLog player = json.fromJson(PlayLog.class, playlogJson);

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