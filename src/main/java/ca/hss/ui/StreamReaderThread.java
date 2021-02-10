/**
 * StreamReaderThread.java
 *
 * Copyright 2015-2021 Heartland Software Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the license at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LIcense is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.hss.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.SwingUtilities;

public class StreamReaderThread implements Runnable {
	
	private TextAreaTableModel model = null;
	private BufferedWriter stream = null;
	private BufferedReader input = null;
	
	/**
	 * Stream the output of a process to /dev/nul.
	 * @param input The stream to read.
	 */
	public StreamReaderThread(InputStream input) {
		initialize(input);
	}
	
	/**
	 * Stream the output of a process to a table.
	 * @param input The stream to read.
	 * @param model The table model to write text to.
	 */
	public StreamReaderThread(InputStream input, TextAreaTableModel model) {
		this.model = model;
		initialize(input);
	}
	
	/**
	 * Stream the output of a process to another stream.
	 * @param input The stream to read.
	 * @param stream The stream to write text to.
	 */
	public StreamReaderThread(InputStream input, OutputStream stream) {
		this.stream = new BufferedWriter(new OutputStreamWriter(stream));
		initialize(input);
	}
	
	private void initialize(InputStream input) {
		this.input = new BufferedReader(new InputStreamReader(input));
	}
	
	/**
	 * Invoke the Ui Thread to write the line of text to the table model.
	 * @param text The text to add to the table model.
	 */
	private void queueModelWrite(final String text) {
		SwingUtilities.invokeLater(() -> {
			model.addRow(text);
		});
	}
	
	@Override
	public void run() {
		try {
			String text;
			while ((text = input.readLine()) != null) {
				if (stream != null) {
					stream.write(text);
					stream.newLine();
				}
				else if (model != null)
					queueModelWrite(text);
			}
		}
		catch (IOException e) {
		}
	}
}
