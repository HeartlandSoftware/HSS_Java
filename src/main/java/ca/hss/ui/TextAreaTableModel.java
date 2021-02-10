/**
 * TextAreaTableModel.java
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

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class TextAreaTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private LinkedList<String> rows = new LinkedList<>();
	private int maxSize = -1;
	
	/**
	 * Create a new table model with no maximum number of rows.
	 */
	public TextAreaTableModel() { }
	
	/**
	 * Create a new table model with a maximum number of rows that can be displayed.
	 * @param maxSize The maximum number of rows to allow in the table.
	 */
	public TextAreaTableModel(int maxSize) {
		this.maxSize = maxSize;
	}
	
	/**
	 * Get the maximum number of rows allowed in the table model.
	 * @return The maximum number of rows allowed in the table.
	 */
	public int getMaxSize() { return maxSize; }
	
	/**
	 * Set the maximum number of rows allowed in the table model.
	 * @param maxSize The maximum number of rows allowed in the table.
	 */
	public void setMaxSize(int maxSize) {
		synchronized (rows) {
			this.maxSize = maxSize;
			clean();
		}
	}
	
	/**
	 * Add a row to the end of the table.
	 * @param text The text to add.
	 */
	public void addRow(String text) {
		synchronized (rows) {
			rows.add(text);
			int index = rows.size() - 1;
			fireTableRowsInserted(index, index);
			clean();
		}
	}
	
	/**
	 * Remove all rows from the table.
	 */
	public void clear() {
		synchronized (rows) {
			int size = rows.size();
			if (size > 0) {
				rows.clear();
				fireTableRowsDeleted(0, size - 1);
			}
		}
	}
	
	private void clean() {
		int removed = 0;
		while (rows.size() > maxSize) {
			rows.removeFirst();
			removed++;
		}
		if (removed > 0) {
			fireTableRowsDeleted(0, removed - 1);
		}
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		return rows.get(row);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public String getColumnName(int col) {
		return "";
	}
}
