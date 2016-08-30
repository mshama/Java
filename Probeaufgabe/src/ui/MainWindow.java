package ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import core.DataController;
import exceptions.DependencyException;
import exceptions.NoItemWasFoundException;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;

public class MainWindow {
	private static Table table;
	private Label lblRecordsNumber;
	private Shell shlDataManagement;

	private ArrayList<String[]> data;
	private boolean dataLoaded;
	private DataController dc;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shlDataManagement = new Shell();
		shlDataManagement.setSize(745, 490);
		shlDataManagement.setText("Data Management");
		shlDataManagement.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				if (dc.isModified()) {
					MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					messageBox.setText("Confirm");
					messageBox.setMessage("Some data are not yet saved. Are you sure you want to exit?");
					if (messageBox.open() == SWT.OK) {
						event.doit = true;
					} else {
						event.doit = false;
					}
				} else {
					event.doit = true;
				}
			}
		});

		dc = new DataController();

		addComponents();

		shlDataManagement.open();
		shlDataManagement.layout();
		while (!shlDataManagement.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void setupTable() {
		table.setRedraw(false);
		// remove old columns if any exists
		while (table.getColumnCount() > 0) {
			table.getColumns()[0].dispose();
		}
		// add columns
		for (int i = 0; i < data.get(0).length; i++) {
			TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
			tblclmnId.setWidth(100);
			tblclmnId.setText(data.get(0)[i]);
		}

		// remove old rows
		while (table.getItemCount() > 0) {
			table.getItems()[0].dispose();
		}

		// add new rows
		for (int i = 1; i < data.size(); i++) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(data.get(i));
		}

		table.setRedraw(true);
	}

	private void prepareImport(SelectionEvent e) {
		ChooseModelDialog modelDlg = new ChooseModelDialog(shlDataManagement, SWT.SHEET);
		if (modelDlg.open() == SWT.OK) {
			String model = modelDlg.getModel();
			if (modelDlg.isFromDB()) {
				try {
					data = dc.readDataDB(model);

					setupTable();
					dataLoaded = true;
				} catch (NoItemWasFoundException e1) {
					MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText("Info");
					messageBox.setMessage("No data is found");
					messageBox.open();
				}
			} else {
				FileDialog dialog = new FileDialog(shlDataManagement, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.csv" });
				dialog.setFilterPath("c:\\");
				String filename = dialog.open();
				data = dc.readDataFile(model, filename);

				setupTable();
				dataLoaded = true;
			}
		}
	}

	public void addComponents() {
		ToolBar toolBar = new ToolBar(shlDataManagement, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 10, 709, 26);

		ToolItem tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareImport(e);
			}
		});
		tltmImport.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "/res/ico/import-icon.png"));
		tltmImport.setToolTipText("Import data");

		ToolItem toolItem = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem tltmExport = new ToolItem(toolBar, SWT.NONE);
		tltmExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shlDataManagement, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.csv" });
				dialog.setFilterPath("c:\\");
				String filename = dialog.open();

				dc.extractData(filename);

				MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_INFORMATION | SWT.OK);
				messageBox.setText("Info");
				messageBox.setMessage("Data is extracted");
				messageBox.open();
			}
		});
		tltmExport.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "/res/ico/export-icon.png"));
		tltmExport.setToolTipText("Export data");

		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					dc.saveData();

					refreshView();

					MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText("Info");
					messageBox.setMessage("Data is saved");
					messageBox.open();
				} catch (DependencyException e1) {
					MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_ERROR | SWT.OK);
					messageBox.setText("Error");
					messageBox.setMessage(
							"There was an error during saving of data. one of the records references a non existing record");
					messageBox.open();
				}
			}
		});
		tltmSave.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "/res/ico/save.png"));
		tltmSave.setToolTipText("Save changes");

		ToolItem toolItem_2 = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem tltmRefreshDat = new ToolItem(toolBar, SWT.NONE);
		tltmRefreshDat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dataLoaded) {
					refreshView();
				} else {
					MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText("Info");
					messageBox.setMessage("Please load data first");
					messageBox.open();
				}
			}
		});
		tltmRefreshDat
				.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "/res/ico/refresh-icon.png"));
		tltmRefreshDat.setToolTipText("Refresh data");

		table = new Table(shlDataManagement, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setBounds(10, 42, 637, 375);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		Button btnAddRecord = new Button(shlDataManagement, SWT.NONE);
		btnAddRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dataLoaded) {

					TableColumn[] headerColumns = table.getColumns();
					String[] values = new String[table.getColumnCount()];
					String[] headers = new String[table.getColumnCount()];
					for (int i = 0; i < values.length; i++) {
						values[i] = "";
						headers[i] = headerColumns[i].getText();
					}
					ManipulateDataDialog mdDlg = new ManipulateDataDialog(shlDataManagement, SWT.SHEET, headers, values,
							"Add");

					if (mdDlg.open() == SWT.OK) {
						String[] newData = mdDlg.getValues();
						TableItem tableItem = new TableItem(table, SWT.NONE);
						tableItem.setText(newData);
						dc.addData(newData);
						table.showItem(tableItem);
					}
				} else {
					MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText("Info");
					messageBox.setMessage("Please load data first");
					messageBox.open();
				}
			}
		});
		btnAddRecord.setToolTipText("Add new record");
		btnAddRecord.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "/res/ico/Add-icon.png"));
		btnAddRecord.setBounds(653, 42, 35, 35);

		Button btnRemoveRecord = new Button(shlDataManagement, SWT.NONE);
		btnRemoveRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dataLoaded) {
					try {
						dc.deleteData(table.getSelectionIndex());
						data.remove(table.getSelectionIndex());
						table.remove(table.getSelectionIndex());
					} catch (DependencyException e1) {
						MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_ERROR | SWT.OK);
						messageBox.setText("Error");
						messageBox.setMessage("Cannot delete this record others may depend on it");
						messageBox.open();
					}
				} else {
					MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText("Info");
					messageBox.setMessage("Please load data first");
					messageBox.open();
				}
			}
		});
		btnRemoveRecord.setToolTipText("Delete current record");
		btnRemoveRecord
				.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "/res/ico/delete-icon.png"));
		btnRemoveRecord.setBounds(653, 83, 35, 35);

		Button btnEditRecord = new Button(shlDataManagement, SWT.NONE);
		btnEditRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dataLoaded) {
					if (table.getSelectionIndex() == -1) {
						MessageBox messageBox = new MessageBox(shlDataManagement, SWT.ICON_INFORMATION | SWT.OK);
						messageBox.setText("Info");
						messageBox.setMessage("Please select a record first");
						messageBox.open();
					} else {
						TableItem selectedItem = table.getItem(table.getSelectionIndex());
						TableColumn[] headerColumns = table.getColumns();
						String[] values = new String[table.getColumnCount()];
						String[] headers = new String[table.getColumnCount()];
						for (int i = 0; i < values.length; i++) {
							values[i] = selectedItem.getText(i);
							headers[i] = headerColumns[i].getText();
						}
						ManipulateDataDialog mdDlg = new ManipulateDataDialog(shlDataManagement, SWT.SHEET, headers,
								values, "Update");

						if (mdDlg.open() == SWT.OK) {
							dc.updateData(mdDlg.getValues(), table.getSelectionIndex());
							selectedItem.setText(mdDlg.getValues());
						}
					}
				}
			}
		});
		btnEditRecord.setToolTipText("Edit selected record");
		btnEditRecord.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "/res/ico/edit-icon.png"));
		btnEditRecord.setBounds(653, 124, 35, 35);
		
		lblRecordsNumber = new Label(shlDataManagement, SWT.NONE);
		lblRecordsNumber.setBounds(10, 423, 637, 15);
		lblRecordsNumber.setText("Number of records:");
	}
	
	private void refreshView(){
		data = dc.getData();
		setupTable();
		lblRecordsNumber.setText("Number of records: " + Integer.toString(data.size()));
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
