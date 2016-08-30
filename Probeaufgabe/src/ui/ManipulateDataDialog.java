package ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ManipulateDataDialog extends Dialog {

	protected int result;
	protected Shell shlManipulateData;
	
	private String[] header;
	private String[] values;
	
	private ArrayList<Text> editors;
	private String action;
	
	private Group group;
	
	private int buttonY = 236;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ManipulateDataDialog(Shell parent, int style, String[] header, String[] values, String action) {
		super(parent, style);
		setText("SWT Dialog");
		this.header = header;
		this.values = values;
		this.action = action;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public int open() {
		createContents();
		createLayout();
		shlManipulateData.open();
		shlManipulateData.layout();
		Display display = getParent().getDisplay();
		while (!shlManipulateData.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void createLayout(){
		editors = new ArrayList<Text>();
		int labelX = 60;
		int labelY = 25;
		int height = 25;
		int spacing = 25;
		int textX = 270;
		int textY = 25;
		for(int i=0; i<header.length; i++){
			Text text = new Text(group, SWT.BORDER);
			text.setBounds(textX, textY, 150, height);
			text.setText(values[i]);
			editors.add(text);
			
			Label lblNewLabel = new Label(group, SWT.NONE);
			lblNewLabel.setBounds(labelX, labelY, 150, height);
			lblNewLabel.setText(header[i]+":");
			
			labelY = labelY + spacing;
			textY = textY + spacing;
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlManipulateData = new Shell(getParent(), getStyle());
		shlManipulateData.setSize(450, 300);
		shlManipulateData.setText("Manipulate Data");
		
		Button btnUpdate = new Button(shlManipulateData, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = SWT.OK;
				for(int i=0; i<editors.size(); i++){
					values[i] = editors.get(i).getText();
				}
				shlManipulateData.close();
			}
		});
		btnUpdate.setBounds(282, buttonY, 75, 25);
		btnUpdate.setText(action);
		
		Button btnCancel = new Button(shlManipulateData, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = SWT.CANCEL;
				shlManipulateData.close();
			}
		});
		btnCancel.setBounds(72, buttonY, 75, 25);
		btnCancel.setText("Cancel");
		
		group = new Group(shlManipulateData, SWT.NONE);
		group.setBounds(10, 10, 424, 220);

	}
	
	public String[] getValues(){
		return values;
	}
}
