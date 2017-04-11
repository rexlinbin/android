package com.bccv.tianji.activity;

import java.math.BigInteger;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.tianji.R;
import com.bccv.tianji.model.JoystickButtons;
import com.utils.tools.AnimationManager;
import com.utils.tools.BaseActivity;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;

@SuppressLint("ClickableViewAccessibility")
public class ControllerActivity extends BaseActivity {
	private RelativeLayout titleLayout;
	private LinearLayout directionLayout;
	private ImageButton rockerImageButton, directionImageView;
	private ImageButton powerImageView, voldownImageView, volupImageView,
			homeImageView, rcbackImageView, menuImageView;
	private ImageButton leftImageButton, rightImageButton, sensorImageButton,
			selectImageButton, startImageButton, xImageButton, yImageButton,
			aImageButton, bImageButton;
	private ImageView keycontrollerImageView, gamerockerImageView,
			rockleftImageView, rockupImageView, rockrightImageView,
			rockdownImageView;
	private TextView titleTextView;
	private boolean isLandscape = false;
	private boolean isShowDireMenu = false;

	private long order;

	private enum Direction {
		Left, Up, Right, Down, Unknow
	}

	private Direction direction = Direction.Unknow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		if (savedInstanceState != null) {
			isLandscape = savedInstanceState.getBoolean("isLandscape", false);
		}
		order = JoystickButtons.None;
		setBack();

		if (isLandscape) {
			setLanView();
		} else {
			setPorView();
		}

	}

	private ImageView backTextView;

	private void setBack() {
		backTextView = (ImageView) findViewById(R.id.back_textView);
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();		
			}
		});
	}

	private void setPorView() {
		directionLayout = (LinearLayout) findViewById(R.id.direction_layout);
		keycontrollerImageView = (ImageView) findViewById(R.id.keycontroller_imageView);
		gamerockerImageView = (ImageView) findViewById(R.id.gamerocker_imageView);
		keycontrollerImageView.setSelected(true);

		gamerockerImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLandscape = true;
				setDirection();
			}
		});

		titleTextView = (TextView) findViewById(R.id.title_textView);
		titleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isShowDireMenu = !isShowDireMenu;
				if (isShowDireMenu) {
					AnimationManager.setShowAnimation(directionLayout, 300,
							null);
				} else {
					AnimationManager.setHideAnimation(directionLayout, 300,
							null);
				}
			}
		});

		powerImageView = (ImageButton) findViewById(R.id.power_imageView);
		powerImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:

					break;
				case MotionEvent.ACTION_UP:

					break;
				default:
					break;
				}
				return false;
			}
		});

		directionImageView = (ImageButton) findViewById(R.id.direction_imageView);
		directionImageView.setOnTouchListener(new OnTouchListener() {
			private long direction = -1;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				int x = (int) event.getRawX();
				int y = (int) event.getRawY() - v.getHeight() / 2;

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					int tempTop = v.getTop();
					int tempLeft = v.getLeft();

					int centerX = tempLeft + v.getWidth() / 2;
					int centerY = tempTop + v.getHeight() / 2;

					int left = x - v.getWidth() / 2;
					int top = y - v.getHeight() / 2;

					if (isIn(left, top, centerX, centerY, v.getWidth(),
							v.getHeight(), 100)) {
						directionImageView
								.setBackgroundResource(R.drawable.way_pressed_center);
						direction = JoystickButtons.A;
					} else {
						float tempX = x - centerX;
						float tempY = y - centerY;
						if (tempX == 0
								&& tempY > 0
								|| (tempY < 0 && (tempX / tempY) < 1 && (tempX / tempY) > -1)) {
							directionImageView
									.setBackgroundResource(R.drawable.way_pressed_up);
							direction = JoystickButtons.L_UP;
						} else if (tempX == 0
								&& tempY < 0
								|| (tempY > 0 && (tempX / tempY) < 1 && (tempX / tempY) > -1)) {
							directionImageView
									.setBackgroundResource(R.drawable.way_pressed_down);
							direction = JoystickButtons.L_Down;
						} else if (tempX > 0
								&& tempY == 0
								|| (tempX > 0 && ((tempX / tempY) > 1 || (tempX / tempY) < -1))) {
							directionImageView
									.setBackgroundResource(R.drawable.way_pressed_right);
							direction = JoystickButtons.L_Right;
						} else if (tempX < 0
								&& tempY == 0
								|| (tempX < 0 && ((tempX / tempY) > 1 || (tempX / tempY) < -1))) {
							directionImageView
									.setBackgroundResource(R.drawable.way_pressed_left);
							direction = JoystickButtons.L_Left;
						}
					}

					order = order | direction;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					directionImageView
							.setBackgroundResource(R.drawable.way_normal);
					order = order ^ direction;
					sendMessage();
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				default:
					break;
				}

				return false;
			}
		});

		voldownImageView = (ImageButton) findViewById(R.id.voldown_imageView);
		voldownImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.VolDown;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.VolDown;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});

		volupImageView = (ImageButton) findViewById(R.id.volup_imageView);
		volupImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.VolUp;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.VolUp;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		homeImageView = (ImageButton) findViewById(R.id.home_imageView);
		homeImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.Home;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.Home;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		rcbackImageView = (ImageButton) findViewById(R.id.rcback_imageView);
		rcbackImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.B;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.B;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		menuImageView = (ImageButton) findViewById(R.id.menu_imageView);
		menuImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.Menu;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.Menu;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});

	}

	private void setLanView() {
		directionLayout = (LinearLayout) findViewById(R.id.direction_layout);
		keycontrollerImageView = (ImageView) findViewById(R.id.keycontroller_imageView);
		gamerockerImageView = (ImageView) findViewById(R.id.gamerocker_imageView);
		gamerockerImageView.setSelected(true);

		keycontrollerImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLandscape = false;
				setDirection();
			}
		});

		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		titleTextView = (TextView) findViewById(R.id.title_textView);
		titleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isShowDireMenu = !isShowDireMenu;
				if (isShowDireMenu) {
					AnimationManager.setShowAnimation(directionLayout, 300,
							null);
				} else {
					AnimationManager.setHideAnimation(directionLayout, 300,
							null);
				}

			}
		});

		rockleftImageView = (ImageView) findViewById(R.id.rockleft_imageView);
		rockupImageView = (ImageView) findViewById(R.id.rockup_imageView);
		rockrightImageView = (ImageView) findViewById(R.id.rockright_imageView);
		rockdownImageView = (ImageView) findViewById(R.id.rockdown_imageView);

		rockleftImageView.setVisibility(View.INVISIBLE);
		rockupImageView.setVisibility(View.INVISIBLE);
		rockrightImageView.setVisibility(View.INVISIBLE);
		rockdownImageView.setVisibility(View.INVISIBLE);

		rockerImageButton = (ImageButton) findViewById(R.id.rocker_imageView);
		rockerImageButton.bringToFront();
		rockerImageButton.setOnTouchListener(new OnTouchListener() {
			int tempX = 0;
			int tempY = 0;

			int tempTop = 0;
			int tempLeft = 0;
			int tempBottom = 0;
			int tempRight = 0;

			int centerX = 0;
			int centerY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int actionType = event.getAction();

				int x = (int) event.getRawX();
				int y = (int) event.getRawY() - titleLayout.getHeight();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					// 取得原来图片中心点的坐标

					tempX = v.getWidth() / 2;
					tempY = v.getHeight() / 2;

					tempTop = v.getTop();
					tempLeft = v.getLeft();
					tempBottom = v.getBottom();
					tempRight = v.getRight();

					centerX = tempLeft + tempX;
					centerY = tempTop + tempY;
					break;
				case MotionEvent.ACTION_MOVE:
					int left = x - tempX;
					int top = y - tempY;
					int right = x + tempX;
					int bottom = y + tempY;
					if (isIn(left, top, centerX, centerY, tempX * 2, tempY * 2,
							100)) {
						v.layout(left, top, right, bottom);
						v.postInvalidate();
					} else {
						Point point = getPoint(left, top, centerX, centerY,
								tempX * 2, tempY * 2, 100);
						v.layout(point.x + centerX - tempX, point.y + centerY
								- tempY, point.x + centerX + tempX, point.y
								+ centerY + tempY);
						v.postInvalidate();
					}

					Direction currDirection = getDirection(left, top, centerX,
							centerY, tempX * 2, tempY * 2);
					if (currDirection != direction) {
						switch (direction) {
						case Left:
							order = order ^ JoystickButtons.L_Left;
							break;
						case Up:
							order = order ^ JoystickButtons.L_UP;
							break;
						case Right:
							order = order ^ JoystickButtons.L_Right;
							break;
						case Down:
							order = order ^ JoystickButtons.L_Down;
							break;

						default:
							break;
						}
						direction = currDirection;
						switch (direction) {
						case Left:
							order = order | JoystickButtons.L_Left;
							rockleftImageView.setVisibility(View.VISIBLE);
							rockupImageView.setVisibility(View.INVISIBLE);
							rockrightImageView.setVisibility(View.INVISIBLE);
							rockdownImageView.setVisibility(View.INVISIBLE);
							break;
						case Up:
							order = order | JoystickButtons.L_UP;
							rockleftImageView.setVisibility(View.INVISIBLE);
							rockupImageView.setVisibility(View.VISIBLE);
							rockrightImageView.setVisibility(View.INVISIBLE);
							rockdownImageView.setVisibility(View.INVISIBLE);
							break;
						case Right:
							order = order | JoystickButtons.L_Right;
							rockleftImageView.setVisibility(View.INVISIBLE);
							rockupImageView.setVisibility(View.INVISIBLE);
							rockrightImageView.setVisibility(View.VISIBLE);
							rockdownImageView.setVisibility(View.INVISIBLE);
							break;
						case Down:
							order = order | JoystickButtons.L_Down;
							rockleftImageView.setVisibility(View.INVISIBLE);
							rockupImageView.setVisibility(View.INVISIBLE);
							rockrightImageView.setVisibility(View.INVISIBLE);
							rockdownImageView.setVisibility(View.VISIBLE);
							break;

						default:
							break;
						}
						sendMessage();
					}

					break;
				case MotionEvent.ACTION_UP:
					v.layout(tempLeft, tempTop, tempRight, tempBottom);
					v.postInvalidate();
					rockleftImageView.setVisibility(View.INVISIBLE);
					rockupImageView.setVisibility(View.INVISIBLE);
					rockrightImageView.setVisibility(View.INVISIBLE);
					rockdownImageView.setVisibility(View.INVISIBLE);
					switch (direction) {
					case Left:
						order = order ^ JoystickButtons.L_Left;
						break;
					case Up:
						order = order ^ JoystickButtons.L_UP;
						break;
					case Right:
						order = order ^ JoystickButtons.L_Right;
						break;
					case Down:
						order = order ^ JoystickButtons.L_Down;
						break;

					default:
						break;
					}
					sendMessage();
					direction = Direction.Unknow;

					break;
				default:
					break;
				}

				return false;
			}
		});

		leftImageButton = (ImageButton) findViewById(R.id.left_imageView);
		rightImageButton = (ImageButton) findViewById(R.id.right_imageView);
		sensorImageButton = (ImageButton) findViewById(R.id.sensor_imageView);
		selectImageButton = (ImageButton) findViewById(R.id.select_imageView);
		startImageButton = (ImageButton) findViewById(R.id.start_imageView);
		xImageButton = (ImageButton) findViewById(R.id.x_imageView);
		yImageButton = (ImageButton) findViewById(R.id.y_imageView);
		aImageButton = (ImageButton) findViewById(R.id.a_imageView);
		bImageButton = (ImageButton) findViewById(R.id.b_imageView);
		
		leftImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();

				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.LT;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.LT;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		rightImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.RT;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.RT;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		sensorImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return false;
			}
		});
		selectImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.Back;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.Back;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		startImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.Start;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.Start;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		xImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.X;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.X;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		yImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.Y;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.Y;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		aImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.A;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.A;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		bImageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int actionType = event.getAction();
				
				switch (actionType) {
				case MotionEvent.ACTION_DOWN:
					order = order | JoystickButtons.B;
					sendMessage();
					break;
				case MotionEvent.ACTION_UP:
					order = order ^ JoystickButtons.B;
					sendMessage();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
	}

	private void setDirection() {
		if (isLandscape) {
			ControllerActivity.this
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			ControllerActivity.this
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putBoolean("isLandscape", isLandscape);
	}

	private boolean isIn(int left, int top, int centerX, int centerY,
			int width, int height, int r) {
		int tempX = left + width / 2;
		int tempY = top + height / 2;

		if ((tempX - centerX) * (tempX - centerX) + (tempY - centerY)
				* (tempY - centerY) > r * r) {
			return false;
		}

		return true;
	}

	private Point getPoint(int left, int top, int centerX, int centerY,
			int width, int height, int r) {
		Point point = new Point();
		int tempX = left + width / 2 - centerX;
		int tempY = top + height / 2 - centerY;

		if (tempY > 0) {
			point.y = (int) Math.sqrt(r
					* r
					/ (((float) tempX / (float) tempY)
							* ((float) tempX / (float) tempY) + 1));
			point.x = (int) (((float) tempX / (float) tempY) * Math.sqrt(r
					* r
					/ (((float) tempX / (float) tempY)
							* ((float) tempX / (float) tempY) + 1)));
		} else if (tempY < 0) {
			point.y = (int) -Math.sqrt(r
					* r
					/ (((float) tempX / (float) tempY)
							* ((float) tempX / (float) tempY) + 1));
			point.x = (int) (((float) tempX / (float) tempY) * -Math.sqrt(r
					* r
					/ (((float) tempX / (float) tempY)
							* ((float) tempX / (float) tempY) + 1)));
		} else if (tempX < 0 && tempY == 0) {
			point.y = 0;
			point.x = -r;
		} else if (tempX < 0 && tempY == 0) {
			point.y = 0;
			point.x = r;
		}

		return point;
	}

	private Direction getDirection(int left, int top, int centerX, int centerY,
			int width, int height) {
		Direction direction = Direction.Unknow;
		int tempX = left + width / 2 - centerX;
		int tempY = top + height / 2 - centerY;

		if (tempX > 0) {
			float k = (float) tempY / tempX;
			if (k < 1 && k > -1) {
				direction = Direction.Right;
			} else {
				if (tempY > 0) {
					direction = Direction.Down;
				} else {
					direction = Direction.Up;
				}
			}
		} else if (tempX < 0) {
			float k = (float) tempY / tempX;
			if (k < 1 && k > -1) {
				direction = Direction.Left;
			} else {
				if (tempY > 0) {
					direction = Direction.Down;
				} else {
					direction = Direction.Up;
				}
			}
		} else {
			if (tempY > 0) {
				direction = Direction.Down;
			} else {
				direction = Direction.Up;
			}
		}

		return direction;
	}

	private void sendMessage() {
		GlobalParams.tcpServerHelper.send(GlobalParams.controllerSocket,
				getOrderString());
	}

	private String getOrderString() {
		Logger.e("msg", "{\"handle\" : \"" + order + "\"}");
		return "{\"handle\" : \"" + order + "\"}";
	}

}
