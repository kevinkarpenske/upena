/*
 * Copyright 2013 Jive Software, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jivesoftware.os.upena.uba.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ShellOut {

    static public interface ShellOutput {

        void line(String line);

        void close();
    }
    private final ProcessBuilder pb;
    private final ShellOutput info;
    private final ShellOutput errors;

    public ShellOut(File directory, List command, ShellOutput info, ShellOutput errors) {
        pb = new ProcessBuilder(command);
        if (directory != null) {
            pb.directory(directory);
        }
        this.info = info;
        this.errors = errors;
    }

    public int exec() throws Exception {
        Process process = pb.start();
        CountDownLatch doneLatch = new CountDownLatch(2);
        StreamDrainer seInfo = new StreamDrainer(doneLatch, process.getInputStream(), info);
        StreamDrainer seError = new StreamDrainer(doneLatch, process.getErrorStream(), errors);
        seInfo.start();
        seError.start();
        try {
            int exitCode =  process.waitFor();
            doneLatch.await(5, TimeUnit.SECONDS);
            return exitCode;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        doneLatch.await(5, TimeUnit.SECONDS);
        return 0;
    }

    class StreamDrainer extends Thread {

        private final CountDownLatch doneLatch;
        private final InputStream in;
        private final ShellOutput output;

        StreamDrainer(CountDownLatch doneLatch, InputStream in, ShellOutput output) {
            this.doneLatch = doneLatch;
            this.in = in;
            this.output = output;
        }

        @Override
        public void run() {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = br.readLine()) != null) {
                    output.line(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            doneLatch.countDown();
        }
    }
}
