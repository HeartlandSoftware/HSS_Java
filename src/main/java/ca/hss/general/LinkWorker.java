/**
 * LinkWorker.java
 *
 * Copyright 2015-2019 Heartland Software Solutions Inc.
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

package ca.hss.general;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

/**
 * Worker thread for opening links.
 * 
 * @author Travis
 */
public class LinkWorker extends SwingWorker<Void, Void> {
	private final URI uri;
	
	public LinkWorker(URI uri) {
		this.uri = uri;
	}
	
	@Override
	protected Void doInBackground() throws IOException {
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(uri);
		return null;
	}
	
	@Override
	protected void done() {
		try {
			get();
		}
		catch (ExecutionException ex) {
			ex.printStackTrace();
		}
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
