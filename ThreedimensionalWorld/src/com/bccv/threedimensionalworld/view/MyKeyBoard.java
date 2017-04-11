package com.bccv.threedimensionalworld.view;

import java.util.ArrayList;
import java.util.List;

import com.bccv.threedimensionalworld.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class MyKeyBoard extends LinearLayout {
//	private String TAG = "MyKeyBoard";
	private Context context;

	private enum KeyType {
		LowerLetter, UpperLetter, Number, Symbol
	};

	private KeyType currKeyType = KeyType.LowerLetter;

	private KeyBoardListener keyBoardListener = new KeyBoardListener() {

		@Override
		public void onKey(String text) {
			// TODO Auto-generated method stub

		}

		@Override
		public void hideKeyBoard() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSure() {
			// TODO Auto-generated method stub
			
		}
	};
	private String text = "";
	private String[] nums = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"-", "/", ":", ";", "(", ")", "¥", "$", "&", "@", "\"", ".", ",",
			"?", "!", "'" };
	private String[] upperLetters = { "Q", "W", "E", "R", "T", "Y", "U", "I",
			"O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X",
			"C", "V", "B", "N", "M" };
	private String[] lowerLetters = { "q", "w", "e", "r", "t", "y", "u", "i",
			"o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x",
			"c", "v", "b", "n", "m" };
	private String[] symbols = { "[", "]", "{", "}", "#", "%", "^", "*", "+",
			"=", "_", "\\", "|", "~", "<", ">", "·", "€", "£", "@", "\"", ".",
			",", "?", "!", "'" };
	private int[] leftInputKeyIds = { R.id.left_inputkey_0_button,
			R.id.left_inputkey_1_button, R.id.left_inputkey_2_button,
			R.id.left_inputkey_3_button, R.id.left_inputkey_4_button,
			R.id.left_inputkey_5_button, R.id.left_inputkey_6_button,
			R.id.left_inputkey_7_button, R.id.left_inputkey_8_button,
			R.id.left_inputkey_9_button, R.id.left_inputkey_10_button,
			R.id.left_inputkey_11_button, R.id.left_inputkey_12_button,
			R.id.left_inputkey_13_button, R.id.left_inputkey_14_button,
			R.id.left_inputkey_15_button, R.id.left_inputkey_16_button,
			R.id.left_inputkey_17_button, R.id.left_inputkey_18_button,
			R.id.left_inputkey_20_button, R.id.left_inputkey_21_button,
			R.id.left_inputkey_22_button, R.id.left_inputkey_23_button,
			R.id.left_inputkey_24_button, R.id.left_inputkey_25_button,
			R.id.left_inputkey_26_button };
	private int[] leftFunctionKeyIds = { R.id.left_sizekey_button,
			R.id.left_deletekey_button, R.id.left_functionkey_button,
			R.id.left_blankkey_button, R.id.left_surekey_button,
			R.id.left_quitkey_button };

	private List<Button> leftInputKeyButtons, leftFunctionKeyButtons;

	private List<Button> rightInputKeyButtons, rightFunctionKeyButtons;

	public MyKeyBoard(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public MyKeyBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public MyKeyBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		View view = View.inflate(context, R.layout.view_mykeyboard, null);
		leftFunctionKeyButtons = new ArrayList<Button>();
		rightFunctionKeyButtons = new ArrayList<Button>();
		leftInputKeyButtons = new ArrayList<Button>();
		rightInputKeyButtons = new ArrayList<Button>();
		initFunctionKey(view);
		initInputKey(view);
//		setInputKeyText();
		addView(view);
	}

	private void initInputKey(View view) {
		for (int i = 0; i < 26; i++) {
			final Button leftButton = (Button) view
					.findViewById(leftInputKeyIds[i]);
			leftButton.setScaleX(0.5f);
			leftInputKeyButtons.add(leftButton);
		}
	}

	private void setInputKeyText() {
		String[] currStrings = null;
		if (currKeyType == KeyType.LowerLetter) {
			currStrings = lowerLetters;
		} else if (currKeyType == KeyType.UpperLetter) {
			currStrings = upperLetters;
		} else if (currKeyType == KeyType.Number) {
			currStrings = nums;
		} else {
			currStrings = symbols;
		}
		for (int i = 0; i < 26; i++) {
			Button leftButton = leftInputKeyButtons.get(i);
			Button rightButton = rightInputKeyButtons.get(i);
			leftButton.setText(currStrings[i]);
			rightButton.setText(currStrings[i]);
		}
	}

	private void initFunctionKey(View view) {
		for (int i = 0; i < 6; i++) {
			final Button leftButton = (Button) view
					.findViewById(leftFunctionKeyIds[i]);
			leftButton.setScaleX(0.5f);
			leftFunctionKeyButtons.add(leftButton);
		}
	}

	private void functionKeyClick(int tag) {
		Button leftButton = leftFunctionKeyButtons.get(tag);
		Button rightButton = rightFunctionKeyButtons.get(tag);
		if (tag == 0) {
			if (currKeyType == KeyType.LowerLetter) {
				leftButton.setSelected(true);
				rightButton.setSelected(true);
				currKeyType = KeyType.UpperLetter;
			} else if (currKeyType == KeyType.UpperLetter) {
				leftButton.setSelected(false);
				if (leftButton.isFocused()) {
					rightButton.setSelected(true);
				}else {
					rightButton.setSelected(false);
				}
				
				currKeyType = KeyType.LowerLetter;
			} else if (currKeyType == KeyType.Number) {
				leftButton.setText("123");
				rightButton.setText("123");
				currKeyType = KeyType.Symbol;
			} else if (currKeyType == KeyType.Symbol) {
				leftButton.setText("#+=");
				rightButton.setText("#+=");
				currKeyType = KeyType.Number;
			}
			setInputKeyText();
		} else if (tag == 1) {
			if (text.length() > 0) {
				text = text.substring(0, text.length() - 1);
			}
			
			keyBoardListener.onKey(text);
		} else if (tag == 2) {
			Button left_0Button = leftFunctionKeyButtons.get(0);
			Button right_0Button = rightFunctionKeyButtons.get(0);
			if (currKeyType == KeyType.Number) {
				leftButton.setText("123");
				rightButton.setText("123");
				left_0Button.setText("");
				right_0Button.setText("");
				left_0Button.setBackgroundResource(R.drawable.keyboard_size);
				right_0Button.setBackgroundResource(R.drawable.keyboard_size);
				currKeyType = KeyType.LowerLetter;
			} else if (currKeyType == KeyType.Symbol) {
				leftButton.setText("123");
				rightButton.setText("123");
				left_0Button.setText("");
				right_0Button.setText("");
				left_0Button.setBackgroundResource(R.drawable.keyboard_size);
				right_0Button.setBackgroundResource(R.drawable.keyboard_size);
				currKeyType = KeyType.LowerLetter;
			} else {
				leftButton.setText("ABC");
				rightButton.setText("ABC");
				left_0Button.setText("#+=");
				right_0Button.setText("#+=");
				left_0Button
						.setBackgroundResource(R.drawable.keyboard_function);
				right_0Button
						.setBackgroundResource(R.drawable.keyboard_function);
				left_0Button.setSelected(false);
				right_0Button.setSelected(false);
				currKeyType = KeyType.Number;
			}
			setInputKeyText();
		} else if (tag == 3) {
			text = text + " ";
			keyBoardListener.onKey(text);
		} else if (tag == 4) {
			keyBoardListener.onSure();
		} else if (tag == 5) {
			keyBoardListener.hideKeyBoard();
		}
	}

	public void showKeyBoard() {
		this.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0,
				0, 440, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		this.startAnimation(bottomtranslateAnimation);
	}

	public void hideKeyBoard() {

		this.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0,
				0, 0, 440);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);
		bottomtranslateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				MyKeyBoard.this.clearAnimation();
				MyKeyBoard.this.setVisibility(View.GONE);
			}
		});

		this.startAnimation(bottomtranslateAnimation);
	}

	public void setKeyBoardListener(KeyBoardListener keyBoardListener) {
		this.keyBoardListener = keyBoardListener;
	}

	public void setText(String text) {
		this.text = text;
	}

	public interface KeyBoardListener {
		public void onKey(String text);
		public void hideKeyBoard();
		public void onSure();
	}

	public List<Button> getRightInputKeyButtons() {
		return leftInputKeyButtons;
	}

	public List<Button> getRightFunctionKeyButtons() {
		return leftFunctionKeyButtons;
	}

	public void setRightButtons(List<Button> rightInputKeyButtons,
			List<Button> rightFunctionKeyButtons) {
		this.rightInputKeyButtons = rightInputKeyButtons;
		this.rightFunctionKeyButtons = rightFunctionKeyButtons;
	}

	public void setInputKeyListener() {
		for (int i = 0; i < 26; i++) {
			final Button leftButton = leftInputKeyButtons.get(i);
			final Button rightButton = rightInputKeyButtons.get(i);
			leftButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					text = text + leftButton.getText();
					if (keyBoardListener != null) {
						keyBoardListener.onKey(text);
					}
				}
			});

			leftButton.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						rightButton.onKeyDown(keyCode, event);
					} else if (event.getAction() == KeyEvent.ACTION_UP) {
						rightButton.onKeyUp(keyCode, event);
					}
					return false;
				}
			});

			leftButton.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					rightButton.setSelected(hasFocus);
				}
			});

			leftButton.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					rightButton.onTouchEvent(event);
					return false;
				}
			});

			rightButton.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						leftButton.onKeyDown(keyCode, event);
					} else if (event.getAction() == KeyEvent.ACTION_UP) {
						leftButton.onKeyUp(keyCode, event);
					}
					return false;
				}
			});

			rightButton.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					leftButton.onTouchEvent(event);
					return false;
				}
			});
		}
	}

	public void setFunctionKeyButtons() {
		for (int i = 0; i < 6; i++) {
			final int tag = i;
			final Button leftButton = leftFunctionKeyButtons.get(i);
			final Button rightButton = rightFunctionKeyButtons.get(i);
			leftButton.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (tag == 0) {
						if (currKeyType == KeyType.UpperLetter) {
							rightButton.setSelected(true);
							return;
						}
					}
					rightButton.setSelected(hasFocus);
				}
			});
			leftButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					functionKeyClick(tag);
				}
			});
			leftButton.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						rightButton.onKeyDown(keyCode, event);
					} else if (event.getAction() == KeyEvent.ACTION_UP) {
						rightButton.onKeyUp(keyCode, event);
					}
					return false;
				}
			});

			leftButton.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					rightButton.onTouchEvent(event);
					return false;
				}
			});
			rightButton.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						leftButton.onKeyDown(keyCode, event);
					} else if (event.getAction() == KeyEvent.ACTION_UP) {
						leftButton.onKeyUp(keyCode, event);
					}

					return false;
				}
			});
			rightButton.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					leftButton.onTouchEvent(event);
					return false;
				}
			});
		}
	}
}
