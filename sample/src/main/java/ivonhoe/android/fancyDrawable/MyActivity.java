package ivonhoe.android.fancyDrawable;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.ivonhoe.drawable.*;

public class MyActivity extends Activity {

    private ColorfulDrawable mColorfulDrawable;

    private ImageView mGridImage;
    private ImageView mStreakImage;
    private ImageView mBallsLineImage;
    private ImageView mTextLineImage;
    private ImageView mBallsCircleImage;
    private ImageView mYouZanImage;
    private ImageView mWangYiImage;
    private ImageView mTaoBaoImage;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loadDrawables();
    }

    private void loadDrawables() {
        /**
         *
         * */
        mGridImage = (ImageView) findViewById(R.id.gridImage);
        GridDrawable gridDrawable = new GridDrawable();
        Drawable itemDrawable = getResources().getDrawable(R.drawable.ic_launcher);
        for (int i = 0; i < 10; i++) {
            gridDrawable.addDrawable(itemDrawable);
        }
        int size = getResources().getDimensionPixelSize(R.dimen.itemSize);
        int padding = getResources().getDimensionPixelOffset(R.dimen.itemPadding);
        gridDrawable.setPadding(padding, padding, padding, padding);
        gridDrawable.setDefaultChildSize(size, size);
        mGridImage.setImageDrawable(gridDrawable);

        /**
         *
         * */
        Drawable streakDrawable = new StreakDrawable();
        mStreakImage = (ImageView) findViewById(R.id.streakImage);
        mStreakImage.setImageDrawable(streakDrawable);

        /**
         *
         * */
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            mColorfulDrawable = new ColorfulDrawable();
            actionBar.setBackgroundDrawable(mColorfulDrawable);
        }

        /**
         *
         * */
        Drawable ballsDrawable = new BallsLineDrawable();
        mBallsLineImage = (ImageView) findViewById(R.id.ballsLineImage);
        mBallsLineImage.setImageDrawable(ballsDrawable);

        /**
         *
         * */
        Drawable textDrawable = new BallsLineDrawable();
        ((AtomDrawable) textDrawable).setAtomText("LOADING");
        Paint paint = new Paint();
        paint.setDither(false);
        paint.setAntiAlias(false);
        paint.setStrokeWidth(5);
        paint.setTextSize(40);
        paint.setColor(Color.BLUE);
        ((AtomDrawable) textDrawable).setPaint(paint);
        mTextLineImage = (ImageView) findViewById(R.id.textLineImage);
        mTextLineImage.setImageDrawable(textDrawable);

        /**
         *
         * */
        Drawable cycleDrawable = new BallsCircleDrawable();

        /**
         * 有赞客户端加载进度条
         * */
        Drawable youZanDrawable = new YouZanDrawable();
        mYouZanImage = (ImageView) findViewById(R.id.youZanImage);
        mYouZanImage.setImageDrawable(youZanDrawable);

        mYouZanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDrawableAnimation(mYouZanImage.getDrawable());
                startDrawableAnimation(mYouZanImage.getDrawable());
                stopDrawableAnimation(mWangYiImage.getDrawable());
                startDrawableAnimation(mWangYiImage.getDrawable());
            }
        });

        /**
         * 网易邮箱客户端加载进度条
         * */
        Drawable wangYiDrawable = new NetEaseDrawable(this);
        mWangYiImage = (ImageView) findViewById(R.id.wangYiImage);
        mWangYiImage.setImageDrawable(wangYiDrawable);

        /**
         * 手机淘宝客户端加载进度条
         * */
        Drawable taoBaoImage = new TaoBaoDrawable(this);
        mTaoBaoImage = (ImageView) findViewById(R.id.taoBaoImage);
        mTaoBaoImage.setImageDrawable(taoBaoImage);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startDrawableAnimation(mColorfulDrawable);
        startDrawableAnimation(mStreakImage.getDrawable());
        startDrawableAnimation(mBallsLineImage.getDrawable());
        startDrawableAnimation(mTextLineImage.getDrawable());
        startDrawableAnimation(mYouZanImage.getDrawable());
        startDrawableAnimation(mWangYiImage.getDrawable());
        startDrawableAnimation(mTaoBaoImage.getDrawable());
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopDrawableAnimation(mColorfulDrawable);
        stopDrawableAnimation(mStreakImage.getDrawable());
        stopDrawableAnimation(mBallsLineImage.getDrawable());
        stopDrawableAnimation(mTextLineImage.getDrawable());
        stopDrawableAnimation(mYouZanImage.getDrawable());
        stopDrawableAnimation(mWangYiImage.getDrawable());
        stopDrawableAnimation(mTaoBaoImage.getDrawable());
    }

    private void startDrawableAnimation(Drawable drawable) {
        if (drawable instanceof FancyDrawable) {
            ((FancyDrawable) drawable).start();
        }
    }

    private void stopDrawableAnimation(Drawable drawable) {
        if (drawable instanceof FancyDrawable) {
            ((FancyDrawable) drawable).stop();
        }
    }
}
