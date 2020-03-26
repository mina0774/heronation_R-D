/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.heronation.measurement.helper;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.heronation.measurement.MeasurementARActivity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Helper to detect taps using Android GestureDetector, and pass the taps between UI thread and
 * render thread.
 */
public final class TapHelper implements OnTouchListener {
  private final GestureDetector gestureDetector;
  private final BlockingQueue<MotionEvent> queuedSingleTaps = new ArrayBlockingQueue<>(16);
  private final BlockingQueue<MotionEvent> queuedLongPress = new ArrayBlockingQueue<>(16);

  /**
   * Creates the tap helper.
   *
   * @param context the application's context.
   */
  public TapHelper(Context context) {
      gestureDetector =
              new GestureDetector(
                      context,
                      new GestureDetector.SimpleOnGestureListener() {
                          @Override
                          public boolean onSingleTapUp(MotionEvent e) { // 터치가 끝날 때
                              // Queue tap if there is space. Tap is lost if queue is full.
                              queuedSingleTaps.offer(e);
                              return true;
                          }
                          @Override
                          public void onLongPress(MotionEvent e) { //화면에 특정 픽셀을 꾹 누르는 이벤트를 처리

                              queuedLongPress.offer(e);
                          }

                          @Override
                          public boolean onDown(MotionEvent e) { // 터치
                              return true;
                          }
                      });
  }

    /**
   * Polls for a tap.
   *
   * @return if a tap was queued, a MotionEvent for the tap. Otherwise null if no taps are queued.
     */
    public MotionEvent queuedSingleTapsPoll() {
        return queuedSingleTaps.poll();
    }

    public MotionEvent longPressPoll() {
        return queuedLongPress.poll();
    }

    public MotionEvent queuedSingleTapsPeek() {
        return queuedSingleTaps.peek();
    }

    public MotionEvent longPressPeek() {
        return queuedLongPress.peek();
    }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    return gestureDetector.onTouchEvent(motionEvent);
  }
}
