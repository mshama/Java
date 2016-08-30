package ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import core.DataController;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class MainWindow {
	private static Table table;
	private DataController dc;
	private Shell shlDataManagement;

	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shlDataManagement = new Shell();
		shlDataManagement.setSize(745, 490);
		shlDataManagement.setText("Data Management");
		
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
	
	private void setupTable(ArrayList<String[]> data){
		// add columns
		for(int i=0; i<data.get(0).length; i++){
			TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
			tblclmnId.setWidth(100);
			tblclmnId.setText(data.get(0)[i]);
		}		
		
		for(int i=1; i<data.size(); i++){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(data.get(i));
		}
	}
	
	private void prepareImport(SelectionEvent e){
		ChooseModelDialog modelDlg = new ChooseModelDialog(shlDataManagement, SWT.SHEET);
		if(modelDlg.open()){
			String model = modelDlg.getModel();
			FileDialog dialog = new FileDialog(shlDataManagement, SWT.OPEN);
			dialog.setFilterExtensions(new String [] {"*.csv"});
			dialog.setFilterPath("c:\\");
			String filename = dialog.open();
			
			ArrayList<String[]> data = dc.readData(model, filename);
			
			setupTable(data);
		}
	}
	
	public void addComponents(){
		ToolBar toolBar = new ToolBar(shlDataManagement, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 10, 709, 26);
		
		ToolItem tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prepareImport(e);
			}
		});
		tltmImport.setImage(SWTResourceManager.getImage("C:\\Users\\eclip\\git\\Java\\Probeaufgabe\\res\\ico\\import-icon.png"));
		tltmImport.setToolTipText("Import data");
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExport = new ToolItem(toolBar, SWT.NONE);
		tltmExport.setImage(SWTResourceManager.getImage("C:\\Users\\eclip\\git\\Java\\Probeaufgabe\\res\\ico\\export-icon.png"));
		tltmExport.setToolTipText("Export data");
		
		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.setImage(SWTResourceManager.getImage("C:\\Users\\eclip\\git\\Java\\Probeaufgabe\\res\\ico\\save.png"));
		tltmSave.setToolTipText("Save changes");
		
		table = new Table(shlDataManagement, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 42, 637, 399);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		
		Button btnAddRecord = new Button(shlDataManagement, SWT.NONE);
		btnAddRecord.setToolTipText("Add new record");
		btnAddRecord.setImage(SWTResourceManager.getImage("C:\\Users\\eclip\\git\\Java\\Probeaufgabe\\res\\ico\\Add-icon.png"));
		btnAddRecord.setBounds(653, 42, 35, 35);
		
		Button btnNewButton = new Button(shlDataManagement, SWT.NONE);
		btnNewButton.setImage(SWTResourceManager.getImage("C:\\Users\\eclip\\git\\Java\\Probeaufgabe\\res\\ico\\delete-icon.png"));
		btnNewButton.setBounds(653, 94, 35, 35);
	}
	
	
	/**
	 * Launch the application.
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
