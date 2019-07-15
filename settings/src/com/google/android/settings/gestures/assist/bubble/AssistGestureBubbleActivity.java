package com.google.android.settings.gestures.assist.bubble;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.settings.gestures.assist.AssistGestureHelper;
import android.app.Activity;
import com.android.settings.R;

public class AssistGestureBubbleActivity extends Activity
{
    private AssistGestureHelper mAssistGestureHelper;
    private TextView mCurrentScoreTextView;
    private AssistGestureGameDrawable mEasterEggDrawable;
    private AssistGestureGameDrawable.GameStateListener mEasterEggListener;
    private AssistGesturePlayButtonDrawable mEasterEggPlayDrawable;
    private int mGameState;
    private ImageView mGameView;
    private AssistGestureHelper.GestureListener mGestureListener;
    private Handler mHandler;
    private boolean mIsNavigationHidden;
    private ImageView mPlayView;
    private boolean mShouldStartNewGame;

    public AssistGestureBubbleActivity() {
        this.mShouldStartNewGame = true;
        this.mEasterEggListener = new AssistGestureGameDrawable.GameStateListener() {
            @Override
            public void gameStateChanged(final int n) {
                AssistGestureBubbleActivity.this.mGameState = n;
                if (n == 4) {
                    AssistGestureBubbleActivity.this.pauseGame();
                    AssistGestureBubbleActivity.this.mShouldStartNewGame = true;
                }
            }

            @Override
            public void updateScoreText(final String text) {
                AssistGestureBubbleActivity.this.mCurrentScoreTextView.setText((CharSequence)text);
            }
        };
        this.mGestureListener = new AssistGestureHelper.GestureListener() {
            @Override
            public void onGestureDetected() {
                AssistGestureBubbleActivity.this.mAssistGestureHelper.setListener(null);
                AssistGestureBubbleActivity.this.mAssistGestureHelper.unbindFromElmyraServiceProxy();
                AssistGestureBubbleActivity.this.mHandler.post((Runnable)new onGestureDetectedHere(this));
            }

            @Override
            public void onGestureProgress(final float n, final int n2) {
            }
        };
    }

    public class  onGestureDetectedHere implements  Runnable {
        public onGestureDetectedHere(AssistGestureHelper.GestureListener gestureListener) {
        }

        public void run() {
        AssistGestureBubbleActivity.onGestureDetectedHere.this.run();
        }
}
    private void enterFullScreen() {
        this.getWindow().getDecorView().setSystemUiVisibility(3846);
    }

    private void pauseGame() {
        if (this.mPlayView.getVisibility() == View.INVISIBLE) {
            this.mPlayView.setVisibility(View.VISIBLE);
        }
        this.mEasterEggDrawable.pauseGame();
        this.mAssistGestureHelper.bindToElmyraServiceProxy();
        this.mAssistGestureHelper.setListener(this.mGestureListener);
    }

    private void registerDecorViewListener() {
        this.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener((View.OnSystemUiVisibilityChangeListener)new View.OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(final int n) {
                if ((n & 0x4) == 0x0) {
                    AssistGestureBubbleActivity.this.mIsNavigationHidden = false;
                }
                else {
                    AssistGestureBubbleActivity.this.mIsNavigationHidden = true;
                }
                AssistGestureBubbleActivity.this.updateGameState();
            }
        });
    }

    private void unregisterDecorViewListener() {
        this.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener((View.OnSystemUiVisibilityChangeListener)null);
    }

    private void updateGameState() {
        if (this.mPlayView.getVisibility() == 4 && this.mIsNavigationHidden) {
            this.startGame(this.mShouldStartNewGame);
        }
        else {
            this.pauseGame();
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.assist_gesture_bubble_activity);
        this.getWindow().setBackgroundDrawableResource(R.drawable.assist_gesture_bubble_activity_bg);
        this.mHandler = new Handler(this.getMainLooper());
        this.mAssistGestureHelper = new AssistGestureHelper(this.getApplicationContext());
        this.mCurrentScoreTextView = (TextView)this.findViewById(R.id.current_score);
        this.mGameView = (ImageView)this.findViewById(R.id.game_view);
        this.mEasterEggDrawable = new AssistGestureGameDrawable(this.getApplicationContext(), this.mEasterEggListener);
        this.mGameView.setImageDrawable((Drawable)this.mEasterEggDrawable);
        this.mPlayView = (ImageView)this.findViewById(R.id.play_view);
        (this.mEasterEggPlayDrawable = new AssistGesturePlayButtonDrawable()).setAlpha(200);
        this.mPlayView.setImageDrawable((Drawable)this.mEasterEggPlayDrawable);
        this.mPlayView.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            boolean mTouching;

            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                final int actionMasked = motionEvent.getActionMasked();
                if (actionMasked != 3) {
                    switch (actionMasked) {
                        case 1: {
                            if (this.mTouching) {
                                AssistGestureBubbleActivity.this.mPlayView.setVisibility(View.INVISIBLE);
                                AssistGestureBubbleActivity.this.enterFullScreen();
                                AssistGestureBubbleActivity.this.startGame(AssistGestureBubbleActivity.this.mShouldStartNewGame);
                                this.mTouching = false;
                                break;
                            }
                            break;
                        }
                        case 0: {
                            if (AssistGestureBubbleActivity.this.mEasterEggPlayDrawable.hitTest(motionEvent.getX(), motionEvent.getY())) {
                                this.mTouching = true;
                                break;
                            }
                            this.mTouching = false;
                            break;
                        }
                    }
                }
                else {
                    this.mTouching = false;
                }
                return true;
            }
        });
    }

    public void onPause() {
        super.onPause();
        this.mEasterEggDrawable.pauseGame();
        this.unregisterDecorViewListener();
        this.mAssistGestureHelper.setListener(null);
        this.mAssistGestureHelper.unbindFromElmyraServiceProxy();
    }

    public void onResume() {
        super.onResume();
        this.registerDecorViewListener();
        this.enterFullScreen();
    }

    public void startGame(final boolean b) {
        this.enterFullScreen();
        if (this.mPlayView.getVisibility() == View.VISIBLE) {
            this.mPlayView.setVisibility(View.INVISIBLE);
        }
        this.mEasterEggDrawable.startGame(b);
        this.mShouldStartNewGame = false;
    }
}

