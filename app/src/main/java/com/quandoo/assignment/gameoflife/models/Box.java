package com.quandoo.assignment.gameoflife.models;

public final class Box {

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

        /*
         * Determines what side of a line a point lies.
         */
        private static final byte side(float Ax, float Ay, 
                                          float Bx, float By, 
                                          float x, float y) {
            float result = (Bx-Ax)*(y-Ay) - (By-Ay)*(x-Ax);
            if (result<0) {
                // below or right
                return -1;
            }
            if (result>0) {
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
            if (first==(second*-1)) return true;
            return false;
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
            return "ul=("+ulX+", "+ulY+") "+
                    "ur=("+urX+", "+urY+")\n"+
                    "ll=("+llX+", "+llY+") "+
                    "lr=("+lrX+", "+lrY+")";
        }
    }