package com.google.android.settings.gestures.assist.bubble;

import android.animation.TimeAnimator.TimeListener;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.text.format.DateFormat;
import android.graphics.PointF;
import android.provider.Settings;
import java.util.ArrayList;
import android.os.Vibrator;
import android.graphics.Paint;
import android.os.VibrationEffect;
import android.animation.TimeAnimator;
import android.content.Context;
import java.util.List;
import android.graphics.Rect;
import com.google.android.settings.gestures.assist.AssistGestureHelper;
import android.graphics.drawable.Drawable;

public class AssistGestureGameDrawable extends Drawable
{
    private AssistGestureHelper mAssistGestureHelper;
    private Rect mBounds;
    private boolean mBubbleShouldShrink;
    private boolean mBubbleTouchedBottom;
    private List<Bubble> mBubbles;
    private Context mContext;
    private List<Bubble> mDeadBubbles;
    private TimeAnimator mDriftAnimation;
    private VibrationEffect mErrorVibrationEffect;
    private int mGameState;
    private GameStateListener mGameStateListener;
    private AssistGestureHelper.GestureListener mGestureListener;
    private int mKilledBubbles;
    private long mLastGestureTime;
    private float mLastProgress;
    private Bubble mLastShrunkBubble;
    private int mLastStage;
    private float mLastTime;
    private float mNextBubbleTime;
    private Paint mPaint;
    private boolean mServiceConnected;
    private List<SpiralingAndroid> mSpiralingAndroids;
    private int mTopKilledBubbles;
    private long mTopKilledBubblesDate;
    private Vibrator mVibrator;
    
    public AssistGestureGameDrawable(final Context mContext, final GameStateListener mGameStateListener) {
        this.mBubbleShouldShrink = true;
        this.mGestureListener = new AssistGestureHelper.GestureListener() {
            @Override
            public void onGestureDetected() {
                AssistGestureGameDrawable.this.onGestureDetected();
            }
            
            @Override
            public void onGestureProgress(final float n, final int n2) {
                AssistGestureGameDrawable.this.onGestureProgress(n, n2);
            }
        };
        this.mContext = mContext;
        this.mAssistGestureHelper = new AssistGestureHelper(this.mContext);
        this.mGameStateListener = mGameStateListener;
        this.mVibrator = (Vibrator)mContext.getSystemService((Class)Vibrator.class);
        this.mErrorVibrationEffect = VibrationEffect.get(1);
        (this.mPaint = new Paint()).setAntiAlias(true);
        this.mBubbles = new ArrayList<Bubble>();
        this.mDeadBubbles = new ArrayList<Bubble>();
        this.mSpiralingAndroids = new ArrayList<SpiralingAndroid>();
        this.mTopKilledBubbles = Settings.Secure.getIntForUser(mContext.getContentResolver(), "assist_gesture_egg_top_score", 0, -2);
        this.mTopKilledBubblesDate = Settings.Secure.getLongForUser(mContext.getContentResolver(), "assist_gesture_egg_top_score_time", 0L, -2);
        this.updateScoreText();
    }
    
    private void connectService() {
        this.mAssistGestureHelper.bindToElmyraServiceProxy();
        this.mAssistGestureHelper.setListener(this.mGestureListener);
        this.mServiceConnected = true;
    }
    
    private double distance(final Bubble bubble, final Bubble bubble2) {
        final PointF point = bubble.getPoint();
        final PointF point2 = bubble2.getPoint();
        return Math.sqrt(Math.pow(point2.x - point.x, 2.0) + Math.pow(point2.y - point.y, 2.0));
    }
    
    private boolean hasCollisionWithDeadBubbles(final Bubble bubble) {
        for (int i = 0; i < this.mDeadBubbles.size(); ++i) {
            final Bubble bubble2 = this.mDeadBubbles.get(i);
            if (this.distance(bubble, bubble2) < bubble.getSize() + bubble2.getSize()) {
                return true;
            }
        }
        return false;
    }
    
    private void notifyGameStateChanged() {
        if (this.mGameStateListener != null) {
            this.mGameStateListener.gameStateChanged(this.mGameState);
        }
    }
    
    private void onGestureDetected() {
        if (this.mGameState != 3) {
            return;
        }
        this.mLastProgress = 0.0f;
        this.mLastStage = 0;
        this.mBubbleShouldShrink = true;
        final long currentTimeMillis = System.currentTimeMillis();
        this.mLastGestureTime = currentTimeMillis;
        if (this.mLastShrunkBubble == null) {
            return;
        }
        synchronized (this) {
            this.mLastShrunkBubble.setState(1);
            // monitorexit(this)
            ++this.mKilledBubbles;
            if (this.mKilledBubbles > this.mTopKilledBubbles) {
                this.mTopKilledBubbles = this.mKilledBubbles;
                this.mTopKilledBubblesDate = currentTimeMillis;
                Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "assist_gesture_egg_top_score", this.mTopKilledBubbles, -2);
                Settings.Secure.putLongForUser(this.mContext.getContentResolver(), "assist_gesture_egg_top_score_time", this.mTopKilledBubblesDate, -2);
            }
            this.mNextBubbleTime = 0.0f;
            this.updateScoreText();
        }
    }
    
    private void onGestureProgress(final float mLastProgress, final int mLastStage) {
        if (this.mGameState != 3) {
            return;
        }
        if (mLastStage == 0 && this.mLastStage == 2) {
            this.mVibrator.vibrate(this.mErrorVibrationEffect);
        }
        if (mLastStage == 0) {
            this.mBubbleShouldShrink = true;
        }
        // monitorenter(this)
        int i = 0;
        try {
            while (i < this.mBubbles.size()) {
                if (this.mBubbles.get(i).getState() == 0) {
                    if (mLastStage != 0 && !this.mBubbles.get(0).equals(this.mLastShrunkBubble)) {
                        this.mBubbleShouldShrink = false;
                        break;
                    }
                    this.mBubbleShouldShrink = true;
                    this.mLastShrunkBubble = this.mBubbles.get(0);
                    break;
                }
                else {
                    ++i;
                }
            }
            if (this.mLastShrunkBubble != null && this.mBubbleShouldShrink && this.mLastShrunkBubble.getState() == 0) {
                this.mLastShrunkBubble.setSize(Math.max((int)(this.mLastShrunkBubble.getOriginalSize() - this.mLastShrunkBubble.getOriginalSize() * mLastProgress), 16));
            }
            // monitorexit(this)
            this.mLastProgress = mLastProgress;
            this.mLastStage = mLastStage;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    private void resetGameState() {
        this.resetSpiralingAndroids(this.mBounds);
        this.mDeadBubbles.clear();
        this.mKilledBubbles = 0;
        this.updateScoreText();
        this.mBubbleTouchedBottom = false;
    }
    
    private void resetSpiralingAndroids(final Rect rect) {
        synchronized (this) {
            this.mSpiralingAndroids.clear();
            for (int i = 0; i < 40; ++i) {
                this.mSpiralingAndroids.add(new SpiralingAndroid(this.mContext, rect));
            }
        }
    }
    
    private void updateScoreText() {
        final String string = DateFormat.format((CharSequence)"MM/dd/yyyy HH:mm:ss", this.mTopKilledBubblesDate).toString();
        final GameStateListener mGameStateListener = this.mGameStateListener;
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mKilledBubbles);
        sb.append("/");
        sb.append(this.mTopKilledBubbles);
        sb.append(" ");
        sb.append(string);
        mGameStateListener.updateScoreText(sb.toString());
    }
    
    public void disconnectService() {
        this.mAssistGestureHelper.setListener(null);
        this.mAssistGestureHelper.unbindFromElmyraServiceProxy();
        this.mServiceConnected = false;
    }
    
    public void draw(final Canvas canvas) {
        final long currentTimeMillis = System.currentTimeMillis();
        canvas.save();
        // monitorenter(this)
        final int n = 0;
        int i = 0;
        try {
            while (i < this.mBubbles.size()) {
                final Bubble bubble = this.mBubbles.get(i);
                this.mPaint.setColor(bubble.getColor());
                canvas.drawCircle(bubble.getPoint().x, bubble.getPoint().y, (float)bubble.getSize(), this.mPaint);
                ++i;
            }
            for (int j = 0; j < this.mDeadBubbles.size(); ++j) {
                final Bubble bubble2 = this.mDeadBubbles.get(j);
                this.mPaint.setColor(bubble2.getColor());
                canvas.drawCircle(bubble2.getPoint().x, bubble2.getPoint().y, (float)bubble2.getSize(), this.mPaint);
            }
            // monitorexit(this)
            this.mPaint.setColor(-1);
            this.mPaint.setAlpha(180);
            final float n2 = this.mBounds.height() - 80;
            final float n3 = this.mBounds.height();
            float n5;
            float n6;
            if (currentTimeMillis - this.mLastGestureTime < 450L) {
                final float n4 = this.mBounds.centerX() * (currentTimeMillis - this.mLastGestureTime) / 450L;
                n5 = this.mBounds.centerX() - n4;
                n6 = this.mBounds.centerX() + n4;
            }
            else {
                final float n8;
                final float n7 = this.mBounds.width() - (n8 = this.mBounds.centerX() * this.mLastProgress);
                n5 = n8;
                n6 = n7;
            }
            canvas.drawRect(n5, n2, n6, n3, this.mPaint);
            if (this.mGameState != 3) {
                // monitorenter(this)
                int k = n;
                try {
                    while (k < this.mSpiralingAndroids.size()) {
                        canvas.save();
                        final SpiralingAndroid spiralingAndroid = this.mSpiralingAndroids.get(k);
                        final Drawable android = spiralingAndroid.getAndroid();
                        canvas.rotate(spiralingAndroid.getCurrentRotation(), (float)android.getBounds().centerX(), (float)android.getBounds().centerY());
                        spiralingAndroid.getAndroid().draw(canvas);
                        canvas.restore();
                        ++k;
                    }
                }
                finally {
                }
                // monitorexit(this)
            }
            canvas.restore();
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public void onBoundsChange(final Rect mBounds) {
        this.mBounds = mBounds;
        if (this.mGameState == 2) {
            this.startGame(true);
        }
    }
    
    public void pauseGame() {
        if (this.mGameState == 1) {
            return;
        }
        this.mGameState = 1;
        this.notifyGameStateChanged();
        this.disconnectService();
        this.mNextBubbleTime -= this.mLastTime;
        if (this.mDriftAnimation != null) {
            this.mDriftAnimation.pause();
        }
    }
    
    public void setAlpha(final int alpha) {
        this.mPaint.setAlpha(alpha);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
    
    public void startGame(final boolean b) {
        if (this.mBounds == null) {
            this.mGameState = 2;
            this.notifyGameStateChanged();
            return;
        }
        if (this.mGameState == 3) {
            return;
        }
        if (b) {
            this.resetGameState();
        }
        this.connectService();
        if (this.mBubbleTouchedBottom) {
            this.mGameState = 4;
        }
        else {
            this.mGameState = 3;
            this.notifyGameStateChanged();
        }
        if (this.mDriftAnimation == null) {
            (this.mDriftAnimation = new TimeAnimator()).setTimeListener((TimeAnimator.TimeListener)new TimeAnimator.TimeListener() {
                public void onTimeUpdate(final TimeAnimator timeAnimator, final long n, final long n2) {
                    AssistGestureGameDrawable.this.mLastTime = n * 0.001f;
                    if (AssistGestureGameDrawable.this.mGameState == 3) {
                        synchronized (this) {
                            if (AssistGestureGameDrawable.this.mLastTime > AssistGestureGameDrawable.this.mNextBubbleTime) {
                                AssistGestureGameDrawable.this.mBubbles.add(new Bubble(AssistGestureGameDrawable.this.mBounds));
                                AssistGestureGameDrawable.this.mNextBubbleTime = AssistGestureGameDrawable.this.mLastTime + 1.0f;
                            }
                            for (int i = AssistGestureGameDrawable.this.mBubbles.size() - 1; i >= 0; --i) {
                                final Bubble bubble = AssistGestureGameDrawable.this.mBubbles.get(i);
                                bubble.update(n, n2);
                                if (bubble.isBubbleDead()) {
                                    AssistGestureGameDrawable.this.mBubbles.remove(i);
                                }
                                else if (bubble.isBubbleTouchingTop() && bubble.getState() == 0) {
                                    AssistGestureGameDrawable.this.mDeadBubbles.add(bubble);
                                    AssistGestureGameDrawable.this.mBubbles.remove(i);
                                }
                                else if (AssistGestureGameDrawable.this.hasCollisionWithDeadBubbles(bubble)) {
                                    if (bubble.getPoint().y + bubble.getSize() > AssistGestureGameDrawable.this.mBounds.bottom) {
                                        AssistGestureGameDrawable.this.mGameState = 4;
                                        AssistGestureGameDrawable.this.mBubbleTouchedBottom = true;
                                    }
                                    if (bubble.getState() == 0) {
                                        AssistGestureGameDrawable.this.mDeadBubbles.add(bubble);
                                        AssistGestureGameDrawable.this.mBubbles.remove(i);
                                    }
                                }
                            }
                        }
                    }
                    if (AssistGestureGameDrawable.this.mGameState == 4) {
                        // monitorenter(this)
                        boolean b = false;
                        int j = 0;
                        try {
                            while (j < AssistGestureGameDrawable.this.mSpiralingAndroids.size()) {
                                final SpiralingAndroid spiralingAndroid = AssistGestureGameDrawable.this.mSpiralingAndroids.get(j);
                                if (spiralingAndroid.getAndroid().getBounds().bottom < AssistGestureGameDrawable.this.mBounds.bottom) {
                                    spiralingAndroid.update(n, n2);
                                    b = true;
                                }
                                ++j;
                            }
                            if (AssistGestureGameDrawable.this.mServiceConnected) {
                                AssistGestureGameDrawable.this.disconnectService();
                            }
                            if (!b) {
                                AssistGestureGameDrawable.this.notifyGameStateChanged();
                                AssistGestureGameDrawable.this.mDriftAnimation.pause();
                            }
                        }
                        finally {
                        }
                        // monitorexit(this)
                    }
                    AssistGestureGameDrawable.this.invalidateSelf();
                }
            });
        }
        this.mDriftAnimation.start();
    }
    
    public interface GameStateListener
    {
        void gameStateChanged(final int p0);
        
        void updateScoreText(final String p0);
    }
}
