/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.FreeSquare;

/**
 * @author goshi 2015/05/05
 */
public abstract class FreeSquareSimpleYesNoDialog extends FreeSquareSimpleDialog {

	/**
	 * @param freeSquare
	 * @param text
	 */
	public FreeSquareSimpleYesNoDialog(FreeSquare freeSquare, String text) {
		super(freeSquare); 
		this.setText(text);
		this.setLeftButtonText("Yes"); //$NON-NLS-1$
		this.setRightButtonText("No"); //$NON-NLS-1$
		this.setListener(new SimpleDialogListener() {	
			@Override
			public void rightButtonClicked() {
				FreeSquareSimpleYesNoDialog.this.yes();
			}
			
			@Override
			public void leftButtonClicked() {
				FreeSquareSimpleYesNoDialog.this.yes();
			}
		});
	}

	protected abstract void yes();
	
	protected abstract void no();

}
