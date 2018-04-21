package ui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import database.Database;
import database.DatabaseHandlerException;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;

public class Login {

	protected Shell shell;
	private Text textName;
	private Text textPhNo;
	private Text textAddress;
	private Text textRegion;
	private Text textLoginName;
	 ResultSet rs;
	private int UserID;
	private int LoginUserID;
	private Label lblMessage;
	private Display display;
	/**
	 * Launch the application.
	 * @param args
	 */
	public Login(Display display){
		this.display= display;
	}
	public static void main(String[] args) {
		try {
			Login window = new Login(Display.getDefault());
			Database.init();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Open the window.
	 */
	public void open() {
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
		shell.setText("Retail Store Management: Login Page");
		shell.setLayout(new GridLayout(2, false));
				
		try {
			rs = Database.getTable("UserList");
		} catch (DatabaseHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(rs.next())
			{
				rs.last();
				UserID = Integer.parseInt(rs.getString(1));// get latest ID
				UserID = UserID + 1;// Make this the new ID 				
			}
			else
			{
				UserID = 1001;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Group lg = new Group (shell,SWT.NONE);
		lg.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		lg.setLayout(new GridLayout(1,true));
		
		
		Label lblNewLabel_1 = new Label(lg, SWT.NONE);
		//lblNewLabel_1.setBounds(59, 10, 71, 17);
		lblNewLabel_1.setText("New User");
		lblNewLabel_1.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		
		Label lblNewLabel = new Label(lg, SWT.NONE);
		//lblNewLabel.setBounds(10, 41, 47, 17);
		lblNewLabel.setText("Name");
		lblNewLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));;
		
		textName = new Text(lg, SWT.BORDER);
		//text.setBounds(82, 41, 119, 29);
		textName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		Label lblPhoneNo = new Label(lg, SWT.NONE);
		lblPhoneNo.setText("Phone No.");
		//lblPhoneNo.setBounds(10, 88, 71, 17);
		lblPhoneNo.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		textPhNo = new Text(lg, SWT.BORDER);
		//text_1.setBounds(82, 88, 119, 29);
		textPhNo.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		Label lblAddress = new Label(lg, SWT.NONE);
		lblAddress.setText("Address");
		//lblAddress.setBounds(10, 130, 71, 17);
		lblAddress.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		textAddress = new Text(lg, SWT.BORDER);
		//text_2.setBounds(82, 130, 119, 34);
		textAddress.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		Label lblRegion = new Label(lg, SWT.NONE);
		lblRegion.setText("Region");
		//lblRegion.setBounds(10, 178, 71, 17);
		lblRegion.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		textRegion = new Text(lg, SWT.BORDER);
		//text_3.setBounds(82, 178, 119, 29);
		textRegion.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		Button btnRegister = new Button(lg, SWT.PUSH);
		//btnNewButton.setBounds(53, 213, 97, 29);
		btnRegister.setText("Register");
		btnRegister.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false));
		
		
		btnRegister.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				if(textName.getText().length()<1|| textPhNo.getText().length()<1||textAddress.getText().length()<1
						|| textRegion.getText().length()<1){
					lblNewLabel_1.setText("Fields cannot be left blank");
					return;
				}
				String Name ="'"+ textName.getText()+"'";
				String PhNo = "'"+textPhNo.getText()+"'";
				String Address = "'"+textAddress.getText()+"'";
				String Region = "'"+textRegion.getText()+"'";
				System.out.println(Name);
				String Values = UserID + " , " + Name + " , " +Address + " , " +Region + " , " +PhNo;
				int i;
				
				try {
					 i = Database.insert("UserList", Values);
					 System.out.println("Inserted : " + i);
					 PlaceAnOrder page= new PlaceAnOrder(display ,UserID);
					 shell.close();
					 page.open();
				} catch (DatabaseHandlerException|SQLException e) {
					// TODO Auto-generated catch block
					lblNewLabel_1.setText("Username already taken\nPlease choose another");
				}
			}
		});

		Group rg = new Group (shell,SWT.NONE);
		rg.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		rg.setLayout(new GridLayout(1,true));
		
		Label lblNewLabel_2 = new Label(rg, SWT.NONE);
		lblNewLabel_2.setAlignment(SWT.CENTER);
		//lblNewLabel_2.setBounds(39, 10, 94, 17);
		lblNewLabel_2.setText("Existing User");
		lblNewLabel_2.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		lblNewLabel_2.pack();
		
		
		Label label = new Label(rg, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		label.setText("Name");
		//label.setBounds(62, 75, 53, 17);
		label.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
		
		
		textLoginName = new Text(rg, SWT.BORDER);
		//text_4.setBounds(10, 98, 164, 29);
		textLoginName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		
		Button btnLogin = new Button(rg, SWT.PUSH);
		//btnNewButton_1.setBounds(39, 139, 97, 29);
		btnLogin.setText("Log In");
		btnLogin.setSize(100, 500);
		btnLogin.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,true));
		
		

		
		lblMessage = new Label(rg, SWT.NONE);
		lblMessage.setAlignment(SWT.CENTER);
		lblMessage.setText("                                            ");
		//label.setBounds(62, 75, 53, 17);
		lblMessage.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
		
		
		btnLogin.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				
				String Name = "'"+textLoginName.getText()+"'";
				try {
					rs = Database.searchFieldInTable(" UserList ", " User_name ", Name);
					rs.next();
					LoginUserID = Integer.parseInt(rs.getString(1));
					PlaceAnOrder page= new PlaceAnOrder(display, LoginUserID);
					shell.close();
					page.open();
					//GET THE UDER ID OF THE LOGGED IN USER
				} catch (DatabaseHandlerException | NumberFormatException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					lblMessage.setText("No User Found : Please register");
				}
				
			}
		});
		

	}
}
