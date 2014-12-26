package org.nognog.freeSquare.util.font;

import org.nognog.freeSquare.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * @author goshi 2014/10/23
 */
public class FontUtil {

	private static final String HIRAGANA = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん"; //$NON-NLS-1$
	private static final String KATAKANA = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン"; //$NON-NLS-1$
	private static final String USE_KANJI = "風水火土雷氷光闇"; //$NON-NLS-1$
	private static final String ALL_CHARACTORS = createAllCharactors();

	private static String createAllCharactors() {
		StringBuilder builder = new StringBuilder();
		builder.append(FreeTypeFontGenerator.DEFAULT_CHARS).append(HIRAGANA).append(KATAKANA).append(USE_KANJI);
		return builder.toString();
	}

	/**
	 * ひらがな、カタカナ、一部漢字を利用できるようなFreeTypeFontParameterを生成します。
	 * 
	 * @param fontSize
	 *            フォントサイズ
	 * @return FreeTypeFont
	 */
	private static FreeTypeFontParameter createFontParameter(int fontSize) {
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = fontSize;
		parameter.characters = ALL_CHARACTORS;
		return parameter;
	}

	/**
	 * M_PLUS_FONTのフォントを描画するためのBitmapFontをリターンします。
	 * 
	 * @param fontSize
	 * @return M_PLUS_FONTのBitmapFont
	 */
	public static BitmapFont createMPlusFont(int fontSize) {
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Resources.mPlusFontPath));
		BitmapFont font = fontGenerator.generateFont(FontUtil.createFontParameter(fontSize));
		fontGenerator.dispose();
		return font;
	}

}
