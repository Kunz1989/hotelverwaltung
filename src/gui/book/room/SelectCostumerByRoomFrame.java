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
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

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
import app.CustomerControlImp;
import db.entities.Customer;
import db.entities.Room;

/** Selection of the customer wich wants to book a room */
@SuppressWarnings("serial")
public class SelectCostumerByRoomFrame extends AbstractFrame {

	private VerwaltungMainFrame mf;
	private FreeRoomsFrame frf;
	private JLabel header;
	private JList<Customer> list;
	private JScrollPane listScroller;
	private JPanel southPanel;
	protected JButton book;
	protected JButton stepback;
	private JPanel boxdsouthPanel;
	private Room[] selectedRooms;
	private SelectTimeIntervallRoomFrame sf;

	public SelectCostumerByRoomFrame(VerwaltungMainFrame mf,
			FreeRoomsFrame frf, Room[] selectedRooms,
			SelectTimeIntervallRoomFrame sf) {
		this.mf = mf;
		this.frf = frf;
		this.selectedRooms = selectedRooms;
		this.sf = sf;
	}

	@Override
	protected void createWidget() {
		header = new JLabel("Kunde wählen");
		header.setPreferredSize(new Dimension(400, 40));
		header.setForeground(Color.WHITE);
		header.setBackground(Color.BLACK);
		header.setOpaque(true);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setFont(header.getFont().deriveFont(Font.BOLD + Font.ITALIC, 30));

		try {
			list = new JList<>(new CustomerControlImp().getAll());
		} catch (SQLException e) {
			mf.addProtocolLine("Es konnte keine verbindung zur Datenbank hergestellt werden, kontaktieren Sie ihren Administrator");
			e.printStackTrace();
		}
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(600, 160));

		southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 2, 10, 10));

		book = new JButton("Buchen");
		book.setPreferredSize(new Dimension(20, 30));
		book.setActionCommand("Book");

		stepback = new JButton("Zurück");
		stepback.setPreferredSize(new Dimension(20, 30));
		stepback.setActionCommand("Back");
		boxdsouthPanel = new JPanel();
		boxdsouthPanel.setLayout(new BoxLayout(boxdsouthPanel,
				BoxLayout.PAGE_AXIS));
	}

	@Override
	protected void addWidget() {
		getContentPane().setLayout(new BorderLayout(5, 5));
		getContentPane().add(BorderLayout.NORTH, header);
		getContentPane().add(BorderLayout.CENTER, listScroller);
		southPanel.add(book);
		southPanel.add(stepback);
		boxdsouthPanel.add(southPanel);
		boxdsouthPanel.add(Box.createVerticalGlue());
		getContentPane().add(BorderLayout.SOUTH, boxdsouthPanel);

	}

	@Override
	protected void setupInteractions() {
		final FrameSwitcher fs = new FrameSwitchImpl(this, frf);
		final FrameSwitcher fs2 = new FrameSwitchImpl(this, mf);
		stepback.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fs.switchFrame();
			}
		});
		book.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar start = Calendar.getInstance();
				start.setTime(sf.getStartDate());
				Calendar end = Calendar.getInstance();
				end.setTime(sf.getEndDate());
				BookingRoomControlInterface controller = new BookingRoomControlImp();
				double price = 0.0;
				for (Date date = start.getTime(); !start.after(end); start.add(
						Calendar.DATE, 1), date = start.getTime()) {
					for (Room r : selectedRooms) {
						try {
							price = price + r.getPrice();
							controller.create(
									new java.sql.Date(date.getTime()), r,
									list.getSelectedValue());
							mf.addProtocolLine("Buchung von Zimmer: "
									+ r.getRid() + "am Tag:" + date.toString()
									+ " wurde in der Datenbank angelegt\n.");
						} catch (SQLException e1) {
							mf.addProtocolLine("Buchung konnte nicht erstellt werden.");
							e1.printStackTrace();
						} catch (NullPointerException e1) {
							mf.addProtocolLine("Fehler! Es wurde kein Kunde ausgewählt.");
						}
					}

				}
				mf.addProtocolLine("Die Komplette buchung kostet: " + price
						+ ".");
				fs2.switchFrame();
			}
		});
	}
}