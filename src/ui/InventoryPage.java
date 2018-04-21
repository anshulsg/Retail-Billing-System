package ui;

import java.sql.ResultSet;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import database.Database;
import database.DatabaseHandlerException;

public class InventoryPage {
	private DataBindingContext m_bindingContext;
	protected Shell shell;
	private Table table;
	private Composite composite_buttonHolder,composite_tableViewer;
	private Button button_Catalog, button_Inventory, button_Back;
	private Label label_actions, label_status;
	private Display display;
	/**
	 * Launch the application.
	 * @param args
	 */
	public InventoryPage(Display display){
		this.display= display;
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
	protected void createContents() {
		shell = new Shell();
		shell.setSize(600, 400);
		shell.setText("Retail Store Management: Inventory");
		shell.setLayout(new GridLayout(2, false));
		shell.setMaximized(true);
		
		//First Composite and its Elements
		composite_buttonHolder= new Composite(shell, SWT.NONE);
		GridData col_1= new GridData();
		col_1.verticalAlignment= SWT.FILL;
		col_1.grabExcessVerticalSpace=true;
		composite_buttonHolder.setLayoutData(col_1);
		composite_buttonHolder.setLayout(new GridLayout(1,false));
		
		label_actions= new Label(composite_buttonHolder,SWT.NONE);
		
		addHorizontalSeperator();
		
		button_Back= new Button(composite_buttonHolder, SWT.BORDER);
		button_Back.setText("Back To Main Page");
		button_Back.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				Deliverables d= new Deliverables(display);
				shell.close();
				d.open();
				
			}
		});
		button_Catalog= new Button(composite_buttonHolder, SWT.BORDER);
		button_Catalog.setText("Catalog");
		
		button_Inventory= new Button(composite_buttonHolder, SWT.NONE);
		button_Inventory.setText("Inventory");
		
	
		addHorizontalSeperator();
		
		/*button_refreshDeliverables= getButton(composite_buttonHolder, "Mark As Delivered");
		addHorizontalSeperator();*/
			
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
		
		TableViewer tableViewer = new TableViewer(composite_tableViewer, SWT.BORDER );
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tcl_composite_tableViewer.setColumnData(getTableColumn("Category"), new ColumnPixelData(100, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn("Item"), new ColumnPixelData(180, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn("Unit Price"), new ColumnPixelData(100, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn("Quantity"), new ColumnPixelData(100, false, true));
		
		new Label(shell, SWT.NONE);
		label_status=new Label(shell, SWT.NONE);
		button_Catalog.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				try{
					table.removeAll();
					ResultSet rs= Database.getTable("CatalogView");
					while(rs.next()){
						TableItem item= new TableItem(table, SWT.NONE);
						item.setText(0, rs.getString(1));
						item.setText(1, rs.getString(2));
						item.setText(2, rs.getString(3));
						item.setText(3,"-");
					}
				}
				catch(Exception e){}
				
			}
		});
		button_Inventory.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				try{
					table.removeAll();
				ResultSet rs= Database.getTable("InventoryView");
				while(rs.next()){
					TableItem item= new TableItem(table, SWT.NONE);
					item.setText(0, rs.getString(1));
					item.setText(1, rs.getString(2));
					item.setText(2, rs.getString(3));
					item.setText(3, rs.getString(4));
					}
				}
			catch(Exception e)
			{
				
			}
		}});
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
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
	public void openMainPage(Event event){
		
	}
	public void showInventory(Event event){
		
	}
	public void showCatalog(Event event){
		
	}
	public void showLowStock(Event event){
		
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
