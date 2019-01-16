/**
 * WebDownloader.java
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class WebDownloader {
	public enum ConnectionType {
		DIRECT,
		PROXY,
		NONE
	}

	private static boolean m_initialized;
	private static ConnectionType m_connectionType;
	private static String m_proxyAddress;
	private static int m_proxyPort;
	private static Semaphore sem = new Semaphore(1);

	public static ConnectionType getConnectionType() {
		hasInternetConnection();
		return m_connectionType;
	}

	public static String ProxyAddress() {
		hasInternetConnection();
		return m_proxyAddress;
	}

	public static int ProxyPort() {
		hasInternetConnection();
		return m_proxyPort;
	}

	private static synchronized void block() throws InterruptedException {
		sem.acquire();
	}

	private static synchronized void release() {
		sem.release();
	}

	public static boolean hasInternetConnection() {
		return hasInternetConnection(false);
	}

	public static boolean hasInternetConnection(boolean disable) {
		if (disable) {
			try {
				block();
				if (!m_initialized) {
					m_initialized = true;
					m_connectionType = ConnectionType.NONE;
				}
				release();
			}
			catch (InterruptedException ex) { }
		}
		try {
			block();
			if (!m_initialized)
				isConnectedToNetwork();
			release();
		} catch (InterruptedException e) {
		}
		boolean result = m_connectionType != ConnectionType.NONE;
		return result;
	}

	private static boolean isConnectedToNetwork() {
		boolean retval = true;
		m_connectionType = ConnectionType.NONE;
		try {
			URL url = new URL("http://www.google.com");
			HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
			@SuppressWarnings("unused")
			Object objData = urlConnect.getContent();
			if (!m_initialized)
				m_connectionType = ConnectionType.DIRECT;
			m_initialized = true;
		}
		catch(UnknownHostException e) {
			retval = false;
		}
		catch (IOException e) {
			retval = false;
		}
		if (!retval) {
			if (m_initialized)
				retval = true;
		}
		if (!retval) {
			try {
				System.setProperty("java.net.useSystemProxies", "true");
				List<Proxy> l = ProxySelector.getDefault().select(new URI("http://www.google.com"));
				for (Iterator<Proxy> iter = l.iterator(); iter.hasNext(); ) {
					Proxy proxy = (Proxy)iter.next();
					InetSocketAddress addr = (InetSocketAddress)proxy.address();
					if (addr != null) {
						retval = true;
						if (!m_initialized) {
							m_proxyAddress = addr.getHostName();
							m_proxyPort = addr.getPort();
							m_connectionType = ConnectionType.PROXY;
						}
						m_initialized = true;
						break;
					}
				}
			}
			catch (Exception e) { }
		}
		return retval;
	}

	/**
	 * Downloads the file at {@link address}.
	 * @param address The URL of the file to download.
	 * @return The path to the downloaded file. This file will be deleted when the virtual machine terminates.
	 * @throws IOException
	 */
	public static String Download(URL address) throws IOException {
		if (!hasInternetConnection())
			throw new IOException("No internet connection.");
		String tempDir = System.getProperty("java.io.tmpdir");
		String file = address.getFile();
		int lastSlash = file.lastIndexOf('/');
		boolean secondLast = false;
		if (lastSlash == (file.length() - 1)) {
			lastSlash = file.substring(0, lastSlash - 1).lastIndexOf('/');
			secondLast = true;
		}
		int questionMark = file.lastIndexOf('?');
		if (questionMark > 0) {
			file = file.substring(0, questionMark);
		}
		String filename = tempDir + "/";
		if (lastSlash >= 0) {
			if (secondLast)
				filename = filename + URLEncoder.encode(file.substring(lastSlash + 1, file.length() - 1) + ".html", "UTF-8");
			else
				filename = filename + URLEncoder.encode(file.substring(lastSlash + 1, file.length()), "UTF-8");
		}
		else filename = filename + file;

		HttpURLConnection connection;
		if (m_connectionType == ConnectionType.PROXY) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(m_proxyAddress, m_proxyPort));
			connection = (HttpURLConnection)address.openConnection(proxy);
		}
		else
			connection = (HttpURLConnection)address.openConnection();
		connection.setInstanceFollowRedirects(true);
		connection.setRequestMethod("GET");
		connection.connect();
		int code = connection.getResponseCode();
		if (code != HttpURLConnection.HTTP_OK) {
			if (code == HttpURLConnection.HTTP_MOVED_TEMP ||
					code == HttpURLConnection.HTTP_MOVED_PERM ||
					code == HttpURLConnection.HTTP_SEE_OTHER) {
				String newUrl = connection.getHeaderField("Location");
				System.out.println("Redirect: " + newUrl);
				connection = (HttpURLConnection)new URL(newUrl).openConnection();
				connection.setInstanceFollowRedirects(true);
				connection.setRequestMethod("GET");
				code = connection.getResponseCode();
			}
		}
		
		if (code != 200) {
			System.out.println("Code: " + code);
			System.out.println("URL: " + address.toString());
		}
		else {
			InputStream is = connection.getInputStream();
			ReadableByteChannel rbc = Channels.newChannel(is);
			File fl = new File(filename);
			fl.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(fl);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			is.close();
		}
		return filename;
	}

	private WebDownloader() { }
}
