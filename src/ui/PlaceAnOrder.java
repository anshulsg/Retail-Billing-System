package ui;

import java.awt.Point;
import java.awt.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.TableColumnLayout;
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
import org.eclipse.swt.events.SelectionListener;

public class PlaceAnOrder {
	private DataBindingContext m_bindingContext;
	protected Shell shell;
	private Table table,table_1;
	private Composite composite_tableViewer_1,composite_tableViewer,composite_holder,tab_holder;
	private Label label_actions, label_status;
	private Spinner Quantity;
	TableEditor editor;
	CCombo combo;
	private int InvoiceNo;
	private int userId;
	private Display display;
	final String invoiceTableSyntax= " itemName varchar(50), Unit_price int, Qty int, total_price int ";
	/**
	 * Launch the application.
	 * @param args
	 */
	public PlaceAnOrder(Display d, int user){
		display=d;
		userId= user;
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
	public void open() throws DatabaseHandlerException, SQLException {
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
	 * @throws SQLException 
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
		}
	}

	private void setComboContents(){
		//Adds Options for category


		combo.add("Beverages");
		combo.add("Bakery");
		combo.add("Personal Care");
		combo.add("Stationary");
		combo.add("Confectionary & Chocolates");
		combo.add("Fresh Produce");
		combo.add("Dairy");
		combo.setVisible(true);
	}



	private void setTableContents_Quantity(TableItem t[]) throws SQLException{
		// ideally, fetch deliverables from database and show on the page here
				//new Text(table,SWT.BORDER);
		int i=0;
		for(i=0;i<t.length;i++){
			if(t[i].getChecked()){
				TableItem item=new TableItem(table,SWT.NONE);	
				item.setText(0, t[i].getText(1));
				item.setText(1, t[i].getText(2));
				item.setText(2, Quantity.getText());
				
				
			}
		}
		//editor.setEditor(s1, item, 2);


	}


	protected void createContents() throws DatabaseHandlerException, SQLException {
		shell = new Shell();
		shell.setSize(600, 400);
		shell.setText("Retail Store Management: Shopping cart");
		shell.setLayout(new GridLayout(2, false));
		shell.setMaximized(true);


		// first Composite
		
		
		
		composite_holder = new Composite(shell,SWT.BORDER);
		composite_holder.setLayout(new GridLayout(5,true));
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
		col_4.grabExcessHorizontalSpace=false;
		col_4.grabExcessVerticalSpace=true;
		col_4.horizontalAlignment=SWT.FILL;
		col_4.verticalAlignment= SWT.FILL;
		col_4.horizontalSpan=1;
		tab_holder.setLayoutData(col_4);

		Button backToMainPage = new Button(tab_holder,SWT.BORDER  );
		backToMainPage.setText("Back to Main Page");
		backToMainPage.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				Deliverables d =  new Deliverables(display);
				shell.close();
				d.open();
				
			}
		});
		
		combo = new CCombo(tab_holder,SWT.RIGHT);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 143;
		combo.setLayoutData(gd_combo);
		combo.setText("Category");
		setComboContents();
		
		
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				int i = combo.getSelectionIndex();
				if(i==-1){
					
				}else{
					ResultSet rs;
					try {
						System.out.println(combo.getItem(i));
							rs = Database.searchFieldInTable("Category_List","Class_name", "'"+combo.getItem(i)+"'");
						setTableContents(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
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


		TableViewer tableViewer = new TableViewer(composite_tableViewer, SWT.BORDER | SWT.CHECK | SWT.MULTI);
		table_1 = tableViewer.getTable();
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);


		tcl_composite_tableViewer.setColumnData(getTableColumn_1("Category"), new ColumnPixelData(160, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn_1("Item Name"), new ColumnPixelData(200, false, false));
		tcl_composite_tableViewer.setColumnData(getTableColumn_1("Price"), new ColumnPixelData(60, false,true));
		//setTableContents();
		
	

		 Quantity = new Spinner(tab_holder,SWT.BORDER );
	
				
		Quantity.setMinimum(1);
		Quantity.setMaximum(100);
		Quantity.setIncrement(1);
		Quantity.setSelection(1);
		Quantity.setPageIncrement(100);



		Button AddToCart = new Button(tab_holder,SWT.BORDER );
		
		
		AddToCart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		AddToCart.setText("Add to Cart");
		
		AddToCart.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				TableItem t1[] = table_1.getItems();
				try {
					setTableContents_Quantity(t1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		

		Label space = new Label(tab_holder, SWT.NONE);
		space.setText("                                          ");

		Label space_1 = new Label(tab_holder, SWT.NONE);
		
	

		//label_status=new Label(shell, SWT.NONE);


		Button buy = new Button(tab_holder,SWT.BORDER  );		
		buy.setText("        BUY        ");
		new Label(tab_holder, SWT.NONE);
		new Label(tab_holder, SWT.NONE);
		new Label(tab_holder, SWT.NONE);
		
		
		buy.addListener(SWT.Selection,new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				ResultSet rs;
				try {
					rs = Database.query("SELECT max(Invoice_name) FROM InvoiceCollection");
					rs.next();
					String max= rs.getString(1);
					InvoiceNo= Integer.parseInt(max)+1;
					//Database.createTable(Integer.toString(InvoiceNo), invoiceTableSyntax);
					Database.update("CREATE TABLE INVOICE"+InvoiceNo+" ("+invoiceTableSyntax+")");
					TableItem[] items= table.getItems();
					for(TableItem item: items){
						int total = Integer.parseInt(item.getText(1)) * Integer.parseInt(item.getText(2));
						String val= "'"+item.getText(0)+"'"+","+item.getText(1)+","+item.getText(2)+","+total;
						Database.insert("INVOICE"+InvoiceNo,val );
						Database.update("UPDATE Inventory SET Qty = Qty-"+item.getText(2)+
								" WHERE Item_id= (SELECT Item_id FROM Catalog WHERE Name= '"
								+item.getText(0)+"')" );
					}
					int totalAmt;
					rs= Database.query("SELECT sum(total_price) FROM INVOICE"+InvoiceNo);
					rs.next();
					totalAmt= rs.getInt(1);
					String val= "'"+InvoiceNo+"'"+","+userId+","+"CURRENT_DATE"+","+totalAmt+","+0;
					Database.insert("InvoiceCollection", val);
					InvoicePage page= new InvoicePage(display);
					shell.close();
					page.open();
					
				} catch (DatabaseHandlerException|SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//Second Composite and its Elements

		composite_tableViewer_1 = new Composite(shell, SWT.NONE);
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

		table = new Table(composite_tableViewer_1,SWT.BORDER |  SWT.MULTI);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);



		tcl_composite_tableViewer_1.setColumnData(getTableColumn("Item"), new ColumnPixelData(180, false, false));
		tcl_composite_tableViewer_1.setColumnData(getTableColumn("Price"), new ColumnPixelData(80, false, false));
		tcl_composite_tableViewer_1.setColumnData(getTableColumn("Quantity"), new ColumnPixelData(80, false, true));
		//tableViewer_1.setContentProvider(ArrayContentProvider.getInstance());








		//buy.setLocation(00, 900);

		
		
		
		


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

	public TableColumn getTableColumn_1(String heading){
		TableColumn t_col_1= new TableColumn(table_1, SWT.NONE);
		t_col_1.setText(heading);
		return t_col_1;
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
