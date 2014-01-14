package gui.book.room;

import gui.AbstractFrame;
import gui.FrameSwitcher;
import gui.VerwaltungMainFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import app.BookingRoomControlImp;
import app.BookingRoomControlInterface;
import db.entities.Room;

public class FreeRoomsFrame extends AbstractFrame{

	private JLabel header;
	private JList<Room> list;
	private JScrollPane listScroller;
	private JPanel southPanel;
	private JButton book;
	private JButton stepback;
	private JPanel boxdsouthPanel;
	private VerwaltungMainFrame mf;
	private SelectTimeIntervallRoomFrame sf;
	private Room[] freeRooms;
	
	public FreeRoomsFrame(VerwaltungMainFrame mf,SelectTimeIntervallRoomFrame sf) {
		this.mf = mf;
		this.sf = sf;
		BookingRoomControlInterface tmp = new BookingRoomControlImp();
		freeRooms = tmp.getFreeRoom(sf.getStartDate(), sf.getEndDate());
	}
	

	protected void createWidget() {
		header = new JLabel("Freie Zimmer");
		header.setPreferredSize(new Dimension(400,40));
		header.setForeground(Color.WHITE);
		header.setBackground(Color.BLACK);
		header.setOpaque(true);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setFont(header.getFont().deriveFont(Font.BOLD + Font.ITALIC , 30));
		
		
		list = new JList<>(freeRooms);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1,2,10,10));

		book = new JButton("Buchen");
		book.setPreferredSize(new Dimension(20, 30));
		book.setActionCommand("Book");

		stepback = new JButton("Zur�ck");
		stepback.setPreferredSize(new Dimension(20, 30));
		stepback.setActionCommand("Back");
		boxdsouthPanel = new JPanel();	
		boxdsouthPanel.setLayout(new BoxLayout(boxdsouthPanel,BoxLayout.PAGE_AXIS));
	}

	@Override
	protected void addWidget() {
		getContentPane().setLayout(new BorderLayout(5,5));
		getContentPane().add(BorderLayout.NORTH,header);
		getContentPane().add(BorderLayout.CENTER,listScroller);
		southPanel.add(book);
		southPanel.add(stepback);
		boxdsouthPanel.add(southPanel);
		boxdsouthPanel.add(Box.createVerticalGlue());
		getContentPane().add(BorderLayout.SOUTH,boxdsouthPanel);
	}
	
	protected void setupInteractions() {
		final FrameSwitcher fs = new FrameSwitchImpl(this,sf);
		final FreeRoomsFrame frf = this;
		
		stepback.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
					fs.switchFrame();
			}
		});
		book.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				HotelRoom[] tmp = list.getSelectedValuesList().toArray(new HotelRoom[0]);
				SelectCostumerByRoomFrame scf = new SelectCostumerByRoomFrame(mf,frf,tmp,sf);
				scf.init();
				scf.setVisible(false);
				FrameSwitcher fs2 = new FrameSwitchImpl(frf,scf);
				fs2.switchFrame();
			}	
		});
	}
}
