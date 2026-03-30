/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.zeroturnaround.exec.ProcessExecutor;

@Service
public class Executor {

    /**
     * Execute a command or program
     *
     * @param program      program path to execute
     * @param arguments    List of arguments as flags
     * @param inputStream  Main input stream
     * @param outputStream Main outputstream
     */
    public void execute(final String program,
                        final List<String> arguments,
                        final PipedInputStream inputStream,
                        final PipedOutputStream outputStream) {
        final var command = String.format(
                "%s %s",
                program,
                arguments.stream().map(Object::toString).collect(Collectors.joining(" ")));
        try {
            final var future = new ProcessExecutor().commandSplit(command)
                    .redirectInput(inputStream)
                    .redirectOutput(outputStream)
                    .start()
                    .getFuture();
            if (future.isDone() || future.isCancelled()) {
                System.exit(1);
            }
        } catch (final IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Execute a command or program
     *
     * @param command      command to execute (including arguments divided by blanks)
     * @param inputStream  Main input stream
     * @param outputStream Main outputstream
     */
    public void execute(final String command,
                        final PipedInputStream inputStream,
                        final PipedOutputStream outputStream) {
        try {
            final var future = new ProcessExecutor().commandSplit(command)
                    .redirectInput(inputStream)
                    .redirectOutput(outputStream)
                    .start()
                    .getFuture();
            if (future.isDone() || future.isCancelled()) {
                System.exit(1);
            }
        } catch (final IOException e) {
            e.printStackTrace();

        }
    }

}
