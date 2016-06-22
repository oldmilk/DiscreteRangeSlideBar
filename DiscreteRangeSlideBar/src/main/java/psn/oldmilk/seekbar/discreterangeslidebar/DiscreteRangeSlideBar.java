package psn.oldmilk.seekbar.discreterangeslidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class DiscreteRangeSlideBar extends View {

    private static final String TAG = DiscreteRangeSlideBar.class.getSimpleName();

    private static final int DEFAULT_BAR_HEIGHT = 10;

    private static final int DEFAULT_PAINT_STROKE_WIDTH = 0;

    private static final int DEFAULT_FILLED_SLOT_RADIUS_IN_DP = 30;
    private static final int DEFAULT_FILLED_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_FILLED_TEXTCOLOR = Color.parseColor("#000000");
    private static final int DEFAULT_FILLED_TEXTSIZE_IN_DP = 16;

    private static final int DEFAULT_EMPTY_SLOT_RADIUS_IN_DP = 30;
    private static final int DEFAULT_EMPTY_COLOR = Color.parseColor("#C3C3C3");
    private static final int DEFAULT_EMPTY_TEXTCOLOR = Color.parseColor("#000000");
    private static final int DEFAULT_EMPTY_TEXTSIZE_IN_DP = 16;

    private static final int DEFAULT_SELECTED_SLIDER_RADIUS_IN_DP = 40;
    private static final int DEFAULT_SELECTED_COLOR = Color.parseColor("#FFA500");
    private static final int DEFAULT_SELECTED_TEXTCOLOR = Color.parseColor("#C3C3C3");
    private static final int DEFAULT_SELECTED_TEXTSIZE_IN_DP = 16;
    private static final boolean DEFAULT_SELECTED_SHOW_SHADOW = true;


    private static final int DEFAULT_RANGE_COUNT = 5;
    private static final int DEFAULT_HEIGHT_IN_DP = 50;

    protected Paint paint;
    //    protected Paint ripplePaint;
//    protected float radius;
//    protected float emptySlotRadius;
    private int currentIndex;
    private float currentSlidingX;
    private float currentSlidingY;
    private float selectedSlotX;
    private float selectedSlotY;

    private float[] slotPositions;

    private int filledSlotRadius = DEFAULT_FILLED_SLOT_RADIUS_IN_DP;
    private int filledColor = DEFAULT_FILLED_COLOR;
    private int filledTextColor = DEFAULT_FILLED_TEXTCOLOR;
    private float filledTextSize = DEFAULT_FILLED_TEXTSIZE_IN_DP;

    private int emptySlotRadius = DEFAULT_EMPTY_SLOT_RADIUS_IN_DP;
    private int emptyColor = DEFAULT_EMPTY_COLOR;
    private int emptyTextColor = DEFAULT_EMPTY_TEXTCOLOR;
    private float emptyTextSize = DEFAULT_EMPTY_TEXTSIZE_IN_DP;

    private int selectedSliderRadius = DEFAULT_SELECTED_SLIDER_RADIUS_IN_DP;
    private int selectedColor = DEFAULT_SELECTED_COLOR;
    private int selectedTextColor = DEFAULT_SELECTED_TEXTCOLOR;
    private float selectedTextSize = DEFAULT_SELECTED_TEXTSIZE_IN_DP;
    private boolean selectedShowShadow = DEFAULT_SELECTED_SHOW_SHADOW;


    private int barHeight = DEFAULT_BAR_HEIGHT;
    private int rangeCount = DEFAULT_RANGE_COUNT;

    private OnSlideListener listener;
//    private float rippleRadius = 0.0f;
//    private float downX;
//    private float downY;

    private Path innerPath = new Path();
    private Path outerPath = new Path();


    private int layoutHeight;
    private Typeface mTypeface;

    public DiscreteRangeSlideBar(Context context) {
        this(context, null);
    }

    public DiscreteRangeSlideBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DiscreteRangeSlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiscreteRangeSlideBar);
            TypedArray sa = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_height});
            try {
                layoutHeight = sa.getLayoutDimension(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                rangeCount = a.getInt(R.styleable.DiscreteRangeSlideBar_rangeCount, DEFAULT_RANGE_COUNT);

                barHeight = (int) a.getDimension(R.styleable.DiscreteRangeSlideBar_barHeight, DEFAULT_BAR_HEIGHT);

                emptySlotRadius = (int) a.getDimension(R.styleable.DiscreteRangeSlideBar_emptySlotRadius, DEFAULT_EMPTY_SLOT_RADIUS_IN_DP);
                emptyColor = a.getColor(R.styleable.DiscreteRangeSlideBar_emptyColor, DEFAULT_EMPTY_COLOR);
                emptyTextColor = a.getColor(R.styleable.DiscreteRangeSlideBar_emptyTextColor, DEFAULT_EMPTY_TEXTCOLOR);
                emptyTextSize = a.getDimension(R.styleable.DiscreteRangeSlideBar_emptyTextSize, DEFAULT_EMPTY_TEXTSIZE_IN_DP);

                filledSlotRadius = (int) a.getDimension(R.styleable.DiscreteRangeSlideBar_filledSlotRadius, DEFAULT_FILLED_SLOT_RADIUS_IN_DP);
                filledColor = a.getColor(R.styleable.DiscreteRangeSlideBar_filledColor, DEFAULT_FILLED_COLOR);
                filledTextColor = a.getColor(R.styleable.DiscreteRangeSlideBar_filledTextColor, DEFAULT_FILLED_TEXTCOLOR);
                filledTextSize = a.getDimension(R.styleable.DiscreteRangeSlideBar_filledTextSize, DEFAULT_FILLED_TEXTSIZE_IN_DP);

                selectedSliderRadius = (int) a.getDimension(R.styleable.DiscreteRangeSlideBar_selectedSliderRadius, DEFAULT_SELECTED_SLIDER_RADIUS_IN_DP);
                selectedColor = a.getColor(R.styleable.DiscreteRangeSlideBar_seletedColor, DEFAULT_SELECTED_COLOR);
                selectedTextColor = a.getColor(R.styleable.DiscreteRangeSlideBar_seletedTextColor, DEFAULT_SELECTED_TEXTCOLOR);
                selectedTextSize = a.getDimension(R.styleable.DiscreteRangeSlideBar_seletedTextSize, DEFAULT_SELECTED_TEXTSIZE_IN_DP);
                selectedShowShadow = a.getBoolean(R.styleable.DiscreteRangeSlideBar_seletedShowShadow, DEFAULT_SELECTED_SHOW_SHADOW);

            } finally {
                a.recycle();
                sa.recycle();
            }
        }

        setBarHeight(barHeight);
        setRangeCount(rangeCount);
        setEmptySlotRadius(emptySlotRadius);
        setSelectedSliderRadius(selectedSliderRadius);

        slotPositions = new float[rangeCount];
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(DEFAULT_PAINT_STROKE_WIDTH);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);

                // Compute drawing position again
                preComputeDrawingPosition();

                // Ready to draw now
                return true;
            }
        });
        currentIndex = 0;
    }

    /**
     * Helper method to convert pixel to dp
     *
     * @param context
     * @param px
     * @return
     */
    static int pxToDp(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    /**
     * Helper method to convert dp to pixel
     *
     * @param context
     * @param dp
     * @return
     */
    static int dpToPx(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public int getRangeCount() {
        return rangeCount;
    }

    public void setRangeCount(int rangeCount) {
        if (rangeCount < 2) {
            throw new IllegalArgumentException("rangeCount must be >= 2");
        }
        this.rangeCount = rangeCount;
    }

    public float getBarHeight() {
        return barHeight;
    }

    public void setBarHeight(int height) {
        if (height < 0) {
            throw new IllegalArgumentException("Bar height percent must be in [0, *]");
        }
        this.barHeight = height;
    }

    public float getEmptySlotRadius() {
        return emptySlotRadius;
    }

    public void setEmptySlotRadius(int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Slot radius percent must be in (0, 1]");
        }
        this.emptySlotRadius = radius;
    }

    public float getSelectedSliderRadius() {
        return selectedSliderRadius;
    }

//    @AnimateMethod
//    public void setRadius(final float radius) {
//        rippleRadius = radius;
//        if (rippleRadius > 0) {
//            RadialGradient radialGradient = new RadialGradient(
//                    downX,
//                    downY,
//                    rippleRadius * 3,
//                    Color.TRANSPARENT,
//                    Color.BLACK,
//                    Shader.TileMode.MIRROR
//            );
//            ripplePaint.setShader(radialGradient);
//        }
//        invalidate();
//    }

    public void setSelectedSliderRadius(int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Slot radius percent must be in (0, 1]");
        }
        this.selectedSliderRadius = radius;
    }

    public void setOnSlideListener(OnSlideListener listener) {
        this.listener = listener;
    }

    /**
     * Perform all the calculation before drawing, should only run once
     */
    private void preComputeDrawingPosition() {
        int w = getWidthWithPadding();
        int h = getHeightWithPadding();

        /** Space between each slot */
        int spacing = w / rangeCount;

        /** Center vertical */
        int y = getPaddingTop() + h / 2;
        currentSlidingY = y;
        selectedSlotY = y;
        /**
         * Try to center it, so start by half
         * <pre>
         *
         *  Example for 4 slots
         *
         *  ____o____|____o____|____o____|____o____
         *  --space--
         *
         * </pre>
         */
        int x = getPaddingLeft() + (spacing / 2);

        /** Store the position of each slot index */
        for (int i = 0; i < rangeCount; ++i) {
            slotPositions[i] = x;
            if (i == currentIndex) {
                currentSlidingX = x;
                selectedSlotX = x;
            }
            x += spacing;
        }
    }

    public void setInitialIndex(int index) {
        if (index < 0 || index >= rangeCount) {
            throw new IllegalArgumentException("Attempted to set index=" + index + " out of range [0," + rangeCount + "]");
        }
        currentIndex = index;
        currentSlidingX = selectedSlotX = slotPositions[currentIndex];
        invalidate();
    }

    public int getFilledColor() {
        return filledColor;
    }

    public void setFilledColor(int filledColor) {
        this.filledColor = filledColor;
        invalidate();
    }

    public int getFilledTextColor() {
        return filledTextColor;
    }

    public void setFilledTextColor(int filledTextColor) {
        this.filledTextColor = filledTextColor;
        invalidate();
    }

    public int getEmptyColor() {
        return emptyColor;
    }

    public void setEmptyColor(int emptyColor) {
        this.emptyColor = emptyColor;
        invalidate();
    }

    public int getEmptyTextColor() {
        return emptyTextColor;
    }

    public void setEmptyTextColor(int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * Measures height according to the passed measure spec
     *
     * @param measureSpec int measure spec to use
     * @return int pixel size
     */
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            final int height;
            if (layoutHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = dpToPx(getContext(), DEFAULT_HEIGHT_IN_DP);
            } else if (layoutHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                height = getMeasuredHeight();
            } else {
                height = layoutHeight;
            }
            result = height + getPaddingTop() + getPaddingBottom() + (2 * DEFAULT_PAINT_STROKE_WIDTH);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Measures width according to the passed measure spec
     *
     * @param measureSpec int measure spec to use
     * @return int pixel size
     */
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = specSize + getPaddingLeft() + getPaddingRight() + (2 * DEFAULT_PAINT_STROKE_WIDTH) + (int) (2 * selectedSliderRadius);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

//    private void animateRipple() {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "radius", 0, radius);
//        animator.setInterpolator(new AccelerateInterpolator());
//        animator.setDuration(RIPPLE_ANIMATION_DURATION_MS);
//        animator.start();
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                rippleRadius = 0;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//    }

    private void updateCurrentIndex() {
        float min = Float.MAX_VALUE;
        int j = 0;
        /** Find the closest to x */
        for (int i = 0; i < rangeCount; ++i) {
            float dx = Math.abs(currentSlidingX - slotPositions[i]);
            if (dx < min) {
                min = dx;
                j = i;
            }
        }
        /** This is current index of slider */
        if (j != currentIndex) {
            if (listener != null) {
                listener.onSlide(j);
            }
        }
        currentIndex = j;
        /** Correct position */
        currentSlidingX = slotPositions[j];
        selectedSlotX = currentSlidingX;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (x >= slotPositions[0] && x <= slotPositions[rangeCount - 1]) {
                    currentSlidingX = x;
                    currentSlidingY = y;
                    updateCurrentIndex();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (x >= slotPositions[0] && x <= slotPositions[rangeCount - 1]) {
                    currentSlidingX = x;
                    currentSlidingY = y;
                    invalidate();

                    for (int i = 0; i < rangeCount; i++) {
                        float dx = Math.abs(x - (int) slotPositions[i]);
                        if (dx <= (selectedSliderRadius / 2)) {

                            updateCurrentIndex();
                            break;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:

                if (x >= slotPositions[0] && x <= slotPositions[rangeCount - 1]) {
                    currentSlidingX = x;
                    currentSlidingY = y;
                    updateCurrentIndex();
                }

                break;
        }
        return true;
    }

    private boolean isInSelectedSlot(float x, float y) {
        return
                selectedSlotX - selectedSliderRadius <= x && x <= selectedSlotX + selectedSliderRadius &&
                        selectedSlotY - selectedSliderRadius <= y && y <= selectedSlotY + selectedSliderRadius;
    }

    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;


    }

    private void drawEmptySlots(Canvas canvas) {

        Paint textPaint = new Paint();
        textPaint.setColor(emptyTextColor);
        textPaint.setTextSize(emptyTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (mTypeface != null) {

            final Typeface oldTypeface = textPaint.getTypeface();
            final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
            final int fakeStyle = oldStyle & ~mTypeface.getStyle();

            if ((fakeStyle & Typeface.BOLD) != 0) {
                textPaint.setFakeBoldText(true);
            }

            if ((fakeStyle & Typeface.ITALIC) != 0) {
                textPaint.setTextSkewX(-0.25f);
            }

            textPaint.setTypeface(mTypeface);

        }

        int xTextPosOffset = 0;
        int yTextPosOffset = (int) ((textPaint.descent() + textPaint.ascent()) / 2);

        paint.setColor(emptyColor);
        int h = getHeightWithPadding();
        int y = getPaddingTop() + (h >> 1);
        for (int i = 0; i < rangeCount; ++i) {

            canvas.drawCircle(slotPositions[i], y, emptySlotRadius, paint);

            String text = String.valueOf(i);
            canvas.drawText(text, slotPositions[i] + xTextPosOffset, y - yTextPosOffset, textPaint);
        }
    }

    public int getHeightWithPadding() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    public int getWidthWithPadding() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private void drawFilledSlots(Canvas canvas) {

        Paint textPaint = new Paint();
        textPaint.setColor(filledTextColor);
        textPaint.setTextSize(filledTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (mTypeface != null) {
            final Typeface oldTypeface = textPaint.getTypeface();
            final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
            final int fakeStyle = oldStyle & ~mTypeface.getStyle();

            if ((fakeStyle & Typeface.BOLD) != 0) {
                textPaint.setFakeBoldText(true);
            }

            if ((fakeStyle & Typeface.ITALIC) != 0) {
                textPaint.setTextSkewX(-0.25f);
            }

            textPaint.setTypeface(mTypeface);
        }

        int xTextPosOffset = 0;
        int yTextPosOffset = (int) ((textPaint.descent() + textPaint.ascent()) / 2); // why this will correct the layout?

        paint.setColor(filledColor);
        int h = getHeightWithPadding();
        int y = getPaddingTop() + (h >> 1);
        for (int i = 0; i < rangeCount; ++i) {
            if (slotPositions[i] <= currentSlidingX) {

                canvas.drawCircle(slotPositions[i], y, filledSlotRadius, paint);

                String text = String.valueOf(i);
                canvas.drawText(text, slotPositions[i] + xTextPosOffset, y - yTextPosOffset, textPaint);
            }
        }
    }

    private void drawBar(Canvas canvas, int from, int to, int color) {
        if (this.barHeight > 0) {
            paint.setColor(color);
            int h = getHeightWithPadding();
            int half = (barHeight >> 1);
            int y = getPaddingTop() + (h >> 1);
            canvas.drawRect(from, y - half, to, y + half, paint);
        }
    }

//    private void drawRippleEffect(Canvas canvas) {
//        if (rippleRadius != 0) {
//            canvas.save();
//            ripplePaint.setColor(Color.GRAY);
//            outerPath.reset();
//            outerPath.addCircle(downX, downY, rippleRadius, Path.Direction.CW);
//            canvas.clipPath(outerPath);
//            innerPath.reset();
//            innerPath.addCircle(downX, downY, rippleRadius / 3, Path.Direction.CW);
//            canvas.clipPath(innerPath, Region.Op.DIFFERENCE);
//            canvas.drawCircle(downX, downY, rippleRadius, ripplePaint);
//            canvas.restore();
//        }
//    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidthWithPadding();
        int h = getHeightWithPadding();
        int spacing = w / rangeCount;
        int border = (spacing >> 1);
        int x0 = getPaddingLeft() + border;
        int y0 = getPaddingTop() + (h >> 1);

        drawEmptySlots(canvas);
        drawFilledSlots(canvas);

        /** Draw empty bar */
        drawBar(canvas, (int) slotPositions[0], (int) slotPositions[rangeCount - 1], emptyColor);

        /** Draw filled bar */
        drawBar(canvas, x0, (int) currentSlidingX, filledColor);

        //        //Color.parseColor("#88757575")
        if(selectedShowShadow) {

            // Draw Border
            int borderWidth = 0;
            float shadowRadius = 2.0f;
            int shadowColor = Color.BLACK;

            Paint paintBorder = new Paint();
            paintBorder.setAntiAlias(true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
            }
            paintBorder.setShadowLayer(shadowRadius, 0.0f, shadowRadius / 2, shadowColor);
            canvas.drawCircle(currentSlidingX, y0, selectedSliderRadius + borderWidth - (shadowRadius + shadowRadius / 2), paintBorder);
            // Draw CircularImageView
            paint.setColor(selectedColor);
            canvas.drawCircle(currentSlidingX, y0, selectedSliderRadius - (shadowRadius + shadowRadius / 2), paint);

        }else{
            /** Draw the selected range circle */
            paint.setColor(selectedColor);
            canvas.drawCircle(currentSlidingX, y0, selectedSliderRadius, paint);
        }



        Paint textPaint = new Paint();
        textPaint.setColor(selectedTextColor);
        textPaint.setTextSize(selectedTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (mTypeface != null) {
            final Typeface oldTypeface = textPaint.getTypeface();
            final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
            final int fakeStyle = oldStyle & ~mTypeface.getStyle();

            if ((fakeStyle & Typeface.BOLD) != 0) {
                textPaint.setFakeBoldText(true);
            }

            if ((fakeStyle & Typeface.ITALIC) != 0) {
                textPaint.setTextSkewX(-0.25f);
            }

            textPaint.setTypeface(mTypeface);
        }
        int xTextPosOffset = 0;
        int yTextPosOffset = (int) ((textPaint.descent() + textPaint.ascent()) / 2); // why this will correct the layout?


        String text = String.valueOf(currentIndex);
        canvas.drawText(text, currentSlidingX + xTextPosOffset, y0 - yTextPosOffset, textPaint);



    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.saveIndex = this.currentIndex;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.currentIndex = ss.saveIndex;
    }

    /**
     * Interface to keep track sliding position
     */
    public interface OnSlideListener {

        /**
         * Notify when slider change to new index position
         *
         * @param index The index value of range count [0, rangeCount - 1]
         */
        void onSlide(int index);
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        int saveIndex;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.saveIndex = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.saveIndex);
        }
    }
}
