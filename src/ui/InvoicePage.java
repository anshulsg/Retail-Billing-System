package ui;

import java.awt.Point;
import java.awt.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import database.Database;
import database.DatabaseHandlerException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class InvoicePage {
	private DataBindingContext m_bindingContext;
	protected Shell shell;
	private Display display;
	private Table table,table_1;
	private Composite composite_tableViewer_1,composite_tableViewer,composite_holder,tab_holder,composite_labelHolder,composite_Second;
	private Button button_placeOrder, button_checkInventory, button_viewInvoices, button_refreshDeliverables;
	private Label label_InvoiceName,label_TotalAmount,label_actions,label_status;
	private Text text_Amount;
	TableEditor editor;
	CCombo combo;
	/**
	 * Launch the application.
	 * @param args
	 */
	public InvoicePage(Display d){
		display=d;
	}

	/**
	 * Open the window.
	 */
	public static void initDatabase(){
		try{
			Database.init();
		}
		catch(DatabaseHandlerException exc){
			System.out.println(exc);
			exc.printStackTrace();
		}
	}
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	private void setTableContents(ResultSet rs) throws SQLException{
		// ideally, fetch deliverables from database and show on the page here
//		table.removeAll();
		
		table_1.removeAll();
		while(rs.next()){
			TableItem item= new TableItem(table_1, SWT.NONE);
			item.setText(0, rs.getString(1));
			item.setText(1, rs.getString(2));
			item.setText(2, rs.getString(3));
			item.setText(3, rs.getString(4));
			item.setText(4, rs.getString(5));
		}
	}


	



	private void setTableContents_Quantity(){
		// ideally, fetch deliverables from database and show on the page here
		table.removeAll();

		


	}


	protected void createContents() {
		shell = new Shell();
		shell.setSize(600, 400);
		shell.setText("Retail Store Management: Invoices");
		shell.setLayout(new GridLayout(2, false));
		shell.setMaximized(true);


		// first Composite
		
		
		
		composite_holder = new Composite(shell,SWT.BORDER);
		composite_holder.setLayout(new GridLayout(6,true));
		GridData col_3= new GridData();
		col_3.grabExcessHorizontalSpace=true;
		col_3.grabExcessVerticalSpace=true;
		col_3.horizontalAlignment=SWT.FILL;
		col_3.verticalAlignment= SWT.FILL;
		composite_holder.setLayoutData(col_3);



		tab_holder= new Composite(composite_holder,SWT.NONE);
		tab_holder.setLayout(new GridLayout(1,true));
		GridData col_4= new GridData();
		col_4.widthHint = 60;
		col_4.grabExcessHorizontalSpace=true;
		col_4.grabExcessVerticalSpace=true;
		col_4.horizontalAlignment=SWT.FILL;
		col_4.verticalAlignment= SWT.FILL;
		col_4.horizontalSpan=2;
		tab_holder.setLayoutData(col_4);

		Button backToMainPage = new Button(tab_holder,SWT.BORDER  );
		backToMainPage.setText("Back to Main Page");
		backToMainPage.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				Deliverables d= new Deliverables(display);
				shell.close();
				d.open();
				
			}
		});
		Button showInvoice = new Button(tab_holder,SWT.BORDER  );
		showInvoice.setText("Show Invoice ");
		
		showInvoice.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				table.removeAll();
				TableItem[] item = table_1.getSelection();
				label_InvoiceName.setText("INVOICE"+item[0].getText(0));
				text_Amount.setText(item[0].getText(3));
				ResultSet rs1;
				try {
					rs1 = Database.getTable("INVOICE"+item[0].getText(0));
				
				while(rs1.next())
				{
					TableItem i = new TableItem(table,SWT.NONE);
					
					try {
						i.setText(0,rs1.getString(1));
						i.setText(1,rs1.getString(2));
						i.setText(2,rs1.getString(3));
						i.setText(3,rs1.getString(4));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
				catch (DatabaseHandlerException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		
		
		
		
	/*	combo = new CCombo(tab_holder,SWT.RIGHT);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 143;
		combo.setLayoutData(gd_combo);
		combo.setText("Category");
		setComboContents();*/


		composite_tableViewer = new Composite(composite_holder, SWT.BORDER);
		TableColumnLayout tcl_composite_tableViewer = new TableColumnLayout();
		composite_tableViewer.setLayout(tcl_composite_tableViewer);
		GridData col_2= new GridData();
		col_2.grabExcessHorizontalSpace=true;
		col_2.grabExcessVerticalSpace=true;
		col_2.horizontalAlignment=SWT.FILL;
		col_2.verticalAlignment= SWT.FILL;
		col_2.horizontalSpan=4;
		composite_tableViewer.setLayoutData(col_2);


		TableViewer tableViewer = new TableViewer(composite_tableViewer, SWT.BORDER );
		table_1 = tableViewer.getTable();
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);

		tcl_composite_tableViewer.setColumnData(getTableColumn_1("Invoice No."), new ColumnPixelData(120, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn_1("User ID "), new ColumnPixelData(120, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn_1("Date"), new ColumnPixelData(100, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn_1("Total Price"), new ColumnPixelData(100, false, true));
		tcl_composite_tableViewer.setColumnData(getTableColumn_1("Is Delivered"), new ColumnPixelData(100, false, true));
		
		ResultSet rs;
		try {
			
				rs = Database.getTable("InvoiceCollection");
			setTableContents(rs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		//Second Composite and its Elements
		
		composite_Second = new Composite(shell,SWT.NONE);
		composite_Second.setLayout(new GridLayout(1,true));
		GridData col_6= new GridData();
		col_6.grabExcessHorizontalSpace=true;
		col_6.grabExcessVerticalSpace=true;
		col_6.horizontalAlignment=SWT.FILL;
		col_6.verticalAlignment= SWT.FILL;
		composite_Second.setLayoutData(col_6);
		
		
		label_InvoiceName = new Label(composite_Second,SWT.NONE);
		label_InvoiceName.setText("    Invoice Name    ");
		

		composite_tableViewer_1 = new Composite(composite_Second, SWT.NONE);
		TableColumnLayout tcl_composite_tableViewer_1 = new TableColumnLayout();
		composite_tableViewer_1.setLayout(tcl_composite_tableViewer_1);
		GridData col_1= new GridData();
		col_1.grabExcessHorizontalSpace=true;
		col_1.grabExcessVerticalSpace=true;
		col_1.horizontalAlignment=SWT.FILL;
		col_1.verticalAlignment= SWT.FILL;
		composite_tableViewer_1.setLayoutData(col_1);
		

		//TableViewer tableViewer_1 = new TableViewer(composite_tableViewer_1, SWT.BORDER );
		//table = tableViewer_1.getTable();

		table = new Table(composite_tableViewer_1,SWT.BORDER);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);



		tcl_composite_tableViewer_1.setColumnData(getTableColumn("Item"), new ColumnPixelData(150, false, false));
		tcl_composite_tableViewer_1.setColumnData(getTableColumn("Price"), new ColumnPixelData(120, false, false));
		tcl_composite_tableViewer_1.setColumnData(getTableColumn("Quantity"), new ColumnPixelData(80, false, true));
		tcl_composite_tableViewer_1.setColumnData(getTableColumn("Total Amount"), new ColumnPixelData(100, false, true));
		setTableContents_Quantity();
		//tableViewer_1.setContentProvider(ArrayContentProvider.getInstance());


		composite_labelHolder =  new Composite(composite_Second,SWT.NONE);
		composite_labelHolder.setLayout(new GridLayout(2,true));

		
		label_TotalAmount = new Label(composite_labelHolder,SWT.NONE);
		label_TotalAmount.setText("Total Amount");
		
		text_Amount = new Text(composite_labelHolder,SWT.BORDER);
		text_Amount.setText("Amount here");
		text_Amount.setEditable(false);



	
	}
	
	
	public TableColumn getTableColumn(String heading){
		TableColumn t_col= new TableColumn(table, SWT.NONE);
		t_col.setText(heading);
		return t_col;
	}
	public TableColumn getTableColumn_1(String heading){
		TableColumn t_col= new TableColumn(table_1, SWT.NONE);
		t_col.setText(heading);
		return t_col;
	}

	public void addHorizontalSeperator(){
		Label label=new Label(composite_tableViewer_1, SWT.BORDER | SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		label.setFont(SWTResourceManager.getFont("Noto Sans", 17, SWT.NORMAL));
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
	class StatusPopup implements Runnable{	
		StatusPopup(String description){
			label_actions.setText(description);
			Thread t = new Thread(this);
			t.start();
		}
		public void run(){
			try{
				Thread.sleep(10000);
			}
			catch(InterruptedException exc){

			}
			finally{
				label_status.setText("");
			}

		}
	}
}
