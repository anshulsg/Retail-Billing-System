package ui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.layout.TableColumnLayout;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.wb.swt.SWTResourceManager;

import database.Database;
import database.DatabaseHandlerException;
import ui.*;

public class Deliverables {
	private DataBindingContext m_bindingContext;
	protected Shell shell;
	private Table table;
	private Composite composite_buttonHolder,composite_tableViewer;
	private Button button_placeOrder, button_checkInventory, button_viewInvoices, button_refreshDeliverables;
	private Label label_actions, label_status;
	private Display display;
	public static void main(String[] args) {
		initDatabase();
		System.out.println("Connected to database :D");
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					Deliverables window = new Deliverables(display);
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Deliverables(Display display){
		this.display= display;
	}
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
	private void setTableContents(){
		// ideally, fetch deliverables from database and show on the page here
		table.removeAll();
		try {
			ResultSet deliverables= Database.getTable("Deliverables");
			while(deliverables.next()){
				String inv_name, address, amount;
				inv_name= deliverables.getString(1);
				amount= deliverables.getString(2);
				address= deliverables.getString(3)+", "+deliverables.getString(4);
				TableItem item= new TableItem(table, SWT.NONE);
				item.setText(0,inv_name);
				item.setText(1, amount);
				item.setText(2, address);
			}
		} catch (DatabaseHandlerException e){
			// TODO Auto-generated catch block
		}
		catch(SQLException e){
			
		}
	}
	protected void createContents() {
		shell = new Shell();
		shell.setSize(600, 400);
		shell.setText("Retail Store Management: Pending Deliveries");
		shell.setLayout(new GridLayout(2, false));
		shell.setMaximized(true);
		
		//First Composite and its Elements
		composite_buttonHolder= new Composite(shell, SWT.NONE);
		GridData col_1= new GridData();
		col_1.verticalAlignment= SWT.FILL;
		col_1.grabExcessVerticalSpace=true;
		composite_buttonHolder.setLayoutData(col_1);
		composite_buttonHolder.setLayout(new GridLayout(1,false));
		
		label_actions= getLabel(composite_buttonHolder, "Actions");
		
		addHorizontalSeperator();
		
		button_placeOrder= getButton(composite_buttonHolder, "Place an order");
		button_placeOrder.addListener(SWT.Selection, (event)->{openOrdersPage(event);});
		
		button_checkInventory= getButton(composite_buttonHolder, "Check Inventory");
		button_checkInventory.addListener(SWT.Selection, (event)->{openInventoryPage(event);});
		
		button_viewInvoices= getButton(composite_buttonHolder, "View Invoices");
		button_viewInvoices.addListener(SWT.Selection, (event)->{openInvoicesPage(event);});
		
		addHorizontalSeperator();
		
		button_refreshDeliverables= getButton(composite_buttonHolder, "Mark As Delivered");
		addHorizontalSeperator();
			
		//Second Composite and its elements
		composite_tableViewer = new Composite(shell, SWT.NONE);
		TableColumnLayout tcl_composite_tableViewer = new TableColumnLayout();
		composite_tableViewer.setLayout(tcl_composite_tableViewer);
		GridData col_2= new GridData();
		col_2.grabExcessHorizontalSpace=true;
		col_2.grabExcessVerticalSpace=true;
		col_2.horizontalAlignment=SWT.FILL;
		col_2.verticalAlignment= SWT.FILL;
		composite_tableViewer.setLayoutData(col_2);
		
		TableViewer tableViewer = new TableViewer(composite_tableViewer, SWT.BORDER | SWT.CHECK);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tcl_composite_tableViewer.setColumnData(getTableColumn("Invoice Number"), new ColumnPixelData(120, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn("Total Amount"), new ColumnPixelData(120, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn("Delivery Address"), new ColumnPixelData(180, false, true));
		setTableContents();
		
		new Label(shell, SWT.NONE);
		label_status=new Label(shell, SWT.NONE);
		button_refreshDeliverables.addListener(SWT.Selection, new Listener(){
			
			@Override
			public void handleEvent(Event arg0) {
				TableItem[] delivered=table.getItems();
				if(delivered.length==0){
					return;
				}
				for(TableItem item:delivered){
					if(item.getChecked()){
						try {
							Database.update("InvoiceCollection", "Is_Delivered", Integer.toString(1), "Invoice_name="+"'"+item.getText(0)+"'");
						} catch (DatabaseHandlerException e) {
							e.printStackTrace();
						}
					}
				}
				setTableContents();
			}
			
		});
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
	}
	public Button getButton(Composite parent, String title){
		Button button= new Button(parent, SWT.PUSH);
		button.setText(title);
		GridData layout=new GridData();
		layout.horizontalAlignment= SWT.FILL;
		layout.grabExcessHorizontalSpace=true;
		button.setLayoutData(layout);
		return button;
	}
	public Label getLabel(Composite parent, String title){
		Label label= new Label(parent, SWT.CENTER);
		label.setText(title);
		GridData layout=new GridData();
		layout.horizontalAlignment= SWT.FILL;
		layout.grabExcessHorizontalSpace=true;
		label.setLayoutData(layout);
		return label;
	}
	public TableColumn getTableColumn(String heading){
		TableColumn t_col= new TableColumn(table, SWT.NONE);
		t_col.setText(heading);
		return t_col;
	}
	public void addHorizontalSeperator(){
		Label label=new Label(composite_buttonHolder, SWT.BORDER | SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		label.setFont(SWTResourceManager.getFont("Noto Sans", 17, SWT.NORMAL));
	}
	public void openOrdersPage(Event event){
		Login login= new Login(display);
		shell.close();
		login.open();
	}
	public void openInventoryPage(Event event){
		InventoryPage page= new InventoryPage(display);
		shell.close();
		page.open();
	}
	public void openInvoicesPage(Event event){
		InvoicePage invoices= new InvoicePage(display);
		shell.close();
		invoices.open();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
