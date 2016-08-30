package ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ChooseModelDialog extends Dialog {

	protected boolean action;
	protected Shell shlUploadDataFor;
	
	private String selectedModel;
	
	private Label lblErrorMessage;
	private Combo comboModel;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ChooseModelDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean open() {
		createContents();
		shlUploadDataFor.open();
		shlUploadDataFor.layout();
		Display display = getParent().getDisplay();
		while (!shlUploadDataFor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return action;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlUploadDataFor = new Shell(getParent(), getStyle());
		shlUploadDataFor.setSize(299, 193);
		shlUploadDataFor.setText("Upload data for:");
		
		Label lblNewLabel = new Label(shlUploadDataFor, SWT.NONE);
		lblNewLabel.setBounds(30, 28, 104, 15);
		lblNewLabel.setText("Choose Category:");
		
		lblErrorMessage = new Label(shlUploadDataFor, SWT.NONE);
		lblErrorMessage.setBounds(30, 128, 230, 15);
		
		Button btnNext = new Button(shlUploadDataFor, SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(comboModel.getSelectionIndex() == -1){
					lblErrorMessage.setText("Please choose a category!");
				} else{
					action = true;
					selectedModel = comboModel.getItem(comboModel.getSelectionIndex());
					shlUploadDataFor.close();
				}
			}
		});
		btnNext.setBounds(185, 78, 75, 25);
		btnNext.setText("Next");
		
		Button btnCancel = new Button(shlUploadDataFor, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action = false;
				selectedModel = "";
				shlUploadDataFor.close();
			}
		});
		btnCancel.setBounds(30, 78, 75, 25);
		btnCancel.setText("Cancel");
		
		comboModel = new Combo(shlUploadDataFor, SWT.NONE);
		comboModel.setItems(new String[] {"ProductGroup"});
		comboModel.setBounds(169, 28, 91, 23);

	}
	
	public String getModel(){
		return selectedModel;
	}
}
