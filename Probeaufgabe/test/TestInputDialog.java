import static org.junit.Assert.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import ui.ManipulateDataDialog;

public class TestInputDialog {

	@Test
	public void test() {
		Display display = Display.getDefault();
		Shell shlDataManagement = new Shell();
		shlDataManagement.setSize(745, 490);
		shlDataManagement.setText("Data Management");
		
		ManipulateDataDialog mdDlg = new ManipulateDataDialog(shlDataManagement, SWT.NONE, new String[]{"Test1","Test2", "Test3"}, new String[]{"","",""}, "Add");
		if(mdDlg.open() == SWT.OK){
			String[] newData = mdDlg.getValues();
		}
		shlDataManagement.open();
		shlDataManagement.layout();
		while (!shlDataManagement.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
