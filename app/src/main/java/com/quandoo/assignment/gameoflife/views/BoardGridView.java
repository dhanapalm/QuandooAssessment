package com.quandoo.assignment.gameoflife.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.quandoo.assignment.gameoflife.R;
import com.quandoo.assignment.gameoflife.models.Life;

public class BoardGridView extends View {

    public static final int PAUSE = 0;
    public static final int RUNNING = 1;
    private final Box box = new Box();
    Context context;
    private Life _life;
    private long _moveDelay = 250;
    private RefreshHandler _redrawHandler = new RefreshHandler();
    private int mode;

    public BoardGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        _life = new Life(w);
        initGridView();
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (mode == RUNNING) {
            update();
            return;
        }
        if (mode == PAUSE) {
            stop();
        }
    }

    public boolean isRunning() {
        return mode == RUNNING;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw background
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.windowBackground));
        canvas.drawRect(0, 0, Life.AREA, Life.AREA, background);

        // draw gridlines
        Paint line = new Paint();
        line.setColor(getResources().getColor(R.color.line));
        for (int x = 0; x < Life.COLS; x++) {
            canvas.drawLine(x * Life.CELL_SIZE, 0, x * Life.CELL_SIZE, Life.AREA, line);
        }
        for (int y = 0; y < Life.ROWS; y++) {
            canvas.drawLine(0, y * Life.CELL_SIZE, Life.AREA, y * Life.CELL_SIZE, line);
        }
        line.setStyle(Paint.Style.STROKE);
        line.setStrokeWidth(10);
        canvas.drawRect(0, 0, Life.AREA, Life.AREA, line);

        // draw cells
        Paint cell = new Paint();
        cell.setColor(getResources().getColor(R.color.cell));

        for (int h = 0; h < Life.COLS; h++) {
            for (int w = 0; w < Life.ROWS; w++) {
                if (_life.isCellExists(h, w)) {
                    canvas.drawRect(
                            w * Life.CELL_SIZE,
                            h * Life.CELL_SIZE,
                            (w * Life.CELL_SIZE) + (Life.CELL_SIZE - 1),
                            (h * Life.CELL_SIZE) + (Life.CELL_SIZE - 1),
                            cell);
                }
            }
        }
    }

    private void update() {
        _life.generateNextGeneration();
        _redrawHandler.sleep(_moveDelay);
    }

    private void stop() {
        _life.generateNextGeneration();
        _redrawHandler.stop(_moveDelay);
    }

    private void initGridView() {
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
        // See if the motion event is on a Marker
        for (int h = 0; h < Life.COLS; h++) {
            for (int w = 0; w < Life.ROWS; w++) {

                box.set(w * Life.CELL_SIZE, h * Life.CELL_SIZE);
                if (box.isPointInBox(event.getX(), event.getY())) {
                    if (Life.getGrid()[h][w] == 0) {
                        Life.getGrid()[h][w] = 1;
                    } else {
                        Life.getGrid()[h][w] = 0;
                    }
                    invalidate();
                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    private static final class Box {

        // UL
        private float ulX = 0;
        private float ulY = 0;
        // UR
        private float urX = 0;
        private float urY = 0;
        // LL
        private float llX = 0;
        private float llY = 0;
        // LR
        private float lrX = 0;
        private float lrY = 0;

        /*
         * Determines what side of a line a point lies.
         */
        private static final byte side(float Ax, float Ay,
                                       float Bx, float By,
                                       float x, float y) {
            float result = (Bx - Ax) * (y - Ay) - (By - Ay) * (x - Ax);
            if (result < 0) {
                // below or right
                return -1;
            }
            if (result > 0) {
                // above or left
                return 1;
            }
            // on the line
            return 0;
        }

        /*
         * Determines if point is between two lines
         */
        private static final boolean between(float aX, float aY,
                                             float bX, float bY,
                                             float cX, float cY,
                                             float dX, float dY,
                                             float x, float y) {
            byte first = side(aX, aY, bX, bY, x, y);
            byte second = side(cX, cY, dX, dY, x, y);
            if (first == (second * -1)) return true;
            return false;
        }

        private void set(float xPoint, float yPoint) {

            // UL
            ulX = xPoint;
            ulY = yPoint;

            // UR
            urX = xPoint + Life.CELL_SIZE;
            urY = yPoint;

            // LL
            llX = xPoint;
            llY = yPoint + Life.CELL_SIZE;

            // LR
            lrX = xPoint + Life.CELL_SIZE;
            lrY = yPoint + Life.CELL_SIZE;
        }

        private boolean isPointInBox(float xPoint, float yPoint) {
            // Is the point between the top and bottom lines
            boolean betweenTB = between(ulX, ulY,
                    urX, urY,
                    llX, llY,
                    lrX, lrY,
                    xPoint, yPoint);
            // Is the point between the left and right lines
            boolean betweenLR = between(ulX, ulY,
                    llX, llY,
                    urX, urY,
                    lrX, lrY,
                    xPoint, yPoint);
            if (betweenTB && betweenLR) return true;
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "ul=(" + ulX + ", " + ulY + ") " +
                    "ur=(" + urX + ", " + urY + ")\n" +
                    "ll=(" + llX + ", " + llY + ") " +
                    "lr=(" + lrX + ", " + lrY + ")";
        }
    }

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            if (message.what == 0)
                BoardGridView.this.update();
            BoardGridView.this.invalidate();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }

        public void stop(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(1), delayMillis);
        }
    }
}