/*
 Copyright (c) 2022, Robby, Kansas State University
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sireum.presentasi;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

public class PresentasiJFX {

    public static class App extends javafx.application.Application {
        @Override
        public void start(Stage primaryStage) {
            init = true;
            latch.countDown();
        }
    }

    private final static CountDownLatch latch = new CountDownLatch(1);
    private static boolean init = false;

    public static void initJavaFX() {
        if (init) {
            return;
        }
        Thread t = new Thread(() -> Application.launch(App.class));
        t.setDaemon(true);
        t.start();
        while (latch.getCount() > 0) try {
            latch.await();
        } catch (Throwable th) {
            // skip
        }
    }

    public static long getSoundDuration(final String uri) {
        try {
            initJavaFX();
            final CountDownLatch latch = new CountDownLatch(1);
            final long[] duration = new long[]{-1L};
            final boolean[] error = new boolean[]{false};
            final javafx.scene.media.MediaPlayer mediaPlayer =
                    new javafx.scene.media.MediaPlayer(new javafx.scene.media.Media(uri));
            mediaPlayer.setOnReady(() -> {
                duration[0] = (long) Math.ceil(mediaPlayer.getTotalDuration().toMillis());
                latch.countDown();
            });
            mediaPlayer.setOnError(() -> {
                error[0] = true;
                latch.countDown();
            });
            latch.await();
            return error[0] ? -1L : duration[0];
        } catch (Throwable t) {
            t.printStackTrace();
            return -1L;
        }
    }

    public static long getVideoDuration(final String uri) {
        try {
            initJavaFX();
            final CountDownLatch latch = new CountDownLatch(1);
            final javafx.scene.media.MediaPlayer mediaPlayer =
                    new javafx.scene.media.MediaPlayer(new javafx.scene.media.Media(uri));
            final boolean[] error = new boolean[]{false};
            final long[] duration = new long[]{-1L};
            mediaPlayer.setOnReady(() -> {
                duration[0] = (long) Math.ceil(mediaPlayer.getTotalDuration().toMillis());
                latch.countDown();
            });
            mediaPlayer.setOnError(() -> {
                error[0] = true;
                latch.countDown();
            });
            javafx.scene.media.MediaView mediaView = new javafx.scene.media.MediaView(mediaPlayer);
            latch.await();
            return error[0] ? -1L : duration[0];
        } catch (Throwable t) {
            t.printStackTrace();
            return -1L;
        }
    }

    public static boolean checkImage(final String uri) {
        try {
            initJavaFX();
            final javafx.scene.image.Image image = new javafx.scene.image.Image(uri);
            new javafx.scene.image.ImageView(image);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public static void shutdown() {
        if (init) {
            javafx.application.Platform.exit();
        }
    }
}
