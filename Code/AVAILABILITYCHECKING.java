package Code;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

import java.awt.Color;
import java.awt.Font;
import java.io.FileWriter;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Toolkit;

public class AVAILABILITYCHECKING extends JFrame  {
    private static final long serialVersionUID = 1L;
    private List<String> reservations;
    private Date selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private JTextArea textArea;
	private String userName;
	private String contactNumber;

    public AVAILABILITYCHECKING() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(AVAILABILITYCHECKING.class.getResource("/Code/IMAGES/LASTHOME.png")));
        initialize();
        reservations = loadReservations();
    }
    
    public AVAILABILITYCHECKING(String name, String number) {
    	this.userName = name;
        this.contactNumber = number;
        initialize();
        reservations = loadReservations();
        System.out.println(name);
        System.out.println(number);
        
    }

    private void initialize () {
        setTitle("Availability Checking");
        setBounds(100, 100, 1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        // Create and configure components
        JDateChooser daychecker = new JDateChooser();
        daychecker.setBounds(186, 150, 243, 20);
        daychecker.setForeground(new Color(0, 0, 0));
        daychecker.setDateFormatString("MMMM d, y");

        List<String> startOptions = generateTimeOptions();
        DefaultComboBoxModel<String> comboBoxModelStart = new DefaultComboBoxModel<>(startOptions.toArray(new String[0]));

        List<String> endOptions = generateTimeOptions();
        DefaultComboBoxModel<String> comboBoxModelEnd = new DefaultComboBoxModel<>(endOptions.toArray(new String[0]));

        JComboBox<String> startBox = new JComboBox<>(comboBoxModelStart);
        startBox.setBounds(186, 305, 195, 22);

        JComboBox<String> endBox = new JComboBox<>(comboBoxModelEnd);
        endBox.setBounds(186, 355, 195, 22);

        // Add components to the content pane
        getContentPane().add(daychecker);
        getContentPane().add(startBox);
        getContentPane().add(endBox);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(500, 150, 410, 250);
        getContentPane().add(scrollPane_1);
        
        
        textArea = new JTextArea();
        scrollPane_1.setViewportView(textArea);
        textArea.setEditable(false);

        // Register event listeners
        daychecker.getCalendarButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedDate = daychecker.getDate();
                String selectedDateString = String.format("Selected Date: %tF", selectedDate);
                System.out.println(selectedDateString);         
            }
        });

        endBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedEndTime = (String) endBox.getSelectedItem();
                System.out.println("Selected end time: " + selectedEndTime);
            }
        });

        startBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedStartTime = (String) startBox.getSelectedItem();
                System.out.println("Selected start time: " + selectedStartTime);
            }
        });
                
        JPanel checkPanel = new JPanel();
		checkPanel.setOpaque(false);
		checkPanel.setBounds(157, 414, 140, 40);
		getContentPane().add(checkPanel);
		checkPanel.setLayout(null);
		
		JLabel checkBtn = new JLabel("");
		checkBtn.setBounds(5, 5, 125, 30);
		checkPanel.add(checkBtn);
		checkBtn.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/availbtn.png")));
		
		JLabel checkBig = new JLabel("");
		checkPanel.addMouseListener(new ButtonMouseAdapter(checkBtn, checkBig) {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		checkBig.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/availBig.png")));
		checkBig.setBounds(0, 0, 140, 40);
		checkBig.setVisible(false);
		checkPanel.add(checkBig);
	
		JPanel seePanel = new JPanel();
		seePanel.setOpaque(false);
		seePanel.setBounds(500, 100, 140, 45);
		getContentPane().add(seePanel);
		seePanel.setLayout(null);
		
		JLabel seeBtn = new JLabel("");
		seeBtn.setBounds(5, 5, 128, 36);
		seePanel.add(seeBtn);
		seeBtn.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/reservationsbtn.png")));
		
		JLabel seeBig = new JLabel("");
		seePanel.addMouseListener(new ButtonMouseAdapter(seeBtn, seeBig) {
			@Override
			public void mouseClicked(MouseEvent e) {
				reservations = loadReservations();
		        StringBuilder sb = new StringBuilder();
		        for (String reservation : reservations) {
		            sb.append(reservation).append("\n");
		        }
		        textArea.setText(sb.toString());
			}
		});
		seeBig.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/reservationsBig.png")));
		seeBig.setHorizontalAlignment(SwingConstants.CENTER);
		seeBig.setBounds(0, 0, 140, 45);
		seeBig.setVisible(false);
		seePanel.add(seeBig);
		
		JPanel reservePanel = new JPanel();
		reservePanel.setOpaque(false);
		reservePanel.setBounds(460, 480, 100, 57);
		getContentPane().add(reservePanel);
		reservePanel.setLayout(null);
		reservePanel.setVisible(false);
		
		JLabel reserveBtn = new JLabel("");
		reserveBtn.setHorizontalAlignment(SwingConstants.CENTER);
		reserveBtn.setBounds(0, 11, 100, 36);
		reservePanel.add(reserveBtn);
		reserveBtn.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/reservebtn.png")));
		
		JLabel reserveBig = new JLabel("");
		
		reserveBig.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/reserveBig.png")));
		reserveBig.setBounds(0, 11, 100, 35);
		reserveBig.setVisible(false);
		reservePanel.add(reserveBig);
		
		JLabel availImg = new JLabel("");
		availImg.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/AVAILABILITYCHECKING.png")));
		availImg.setBounds(0, 0, 984, 561);
		getContentPane().add(availImg);
		
		reservePanel.addMouseListener(new ButtonMouseAdapter(reserveBtn, reserveBig) {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				RESERVATIONMANAGEMENT r = new RESERVATIONMANAGEMENT(userName,contactNumber,selectedDate, selectedStartTime, selectedEndTime);
				
				EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            r.frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
				dispose();
				
			}
		});
		
		checkPanel.addMouseListener(new ButtonMouseAdapter(checkBtn, checkBig) {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        // Check if the selected date is null
		        if (selectedDate == null) {
		            JOptionPane.showMessageDialog(null, "Please select a date.");
		            return; // Exit the method early
		        }

		        // Load reservations from file
		        List<String> reservations = loadReservations();

		        // Get the selected date, start time, and end time
		        Date selectedDate = daychecker.getDate();
		        String selectedStartTime = (String) startBox.getSelectedItem();
		        String selectedEndTime = (String) endBox.getSelectedItem();

		        // Format the selected date
		        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        String selectedDateStr = dateFormat.format(selectedDate);

		        // Check if the reservation exists for the selected date, start time, and end time
		        boolean reservationExists = checkReservation(reservations, selectedDateStr, selectedStartTime, selectedEndTime);
		        boolean timeOverlap = checkTimeOverlap(reservations, selectedDateStr, selectedStartTime, selectedEndTime);

		        if (reservationExists) {
		            JOptionPane.showMessageDialog(null, "Reservation already exists for the selected date, start time, and end time.");
		        } else if (timeOverlap) {
		            JOptionPane.showMessageDialog(null, "Time overlap with existing reservation. Please choose another time.");
		        } else if (selectedStartTime.equals(selectedEndTime)) {
		            JOptionPane.showMessageDialog(null, "Cannot make reservation. Start time and end time are the same.");
		        } else {
		            JOptionPane.showMessageDialog(null, "Reservation available for the selected date, start time, and end time.");
		            reservePanel.setVisible(true);
		        }
		    }
		});


    }

	
    private boolean checkTimeOverlap(List<String> reservations, String selectedDate, String selectedStartTime, String selectedEndTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date start = null;
        Date end = null;

        try {
            start = timeFormat.parse(selectedStartTime);
            end = timeFormat.parse(selectedEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (String reservation : reservations) {
            String[] reservationData = reservation.split(",");
            if (reservationData.length == 3) {
                String date = reservationData[0].trim();
                String startTime = reservationData[1].trim();
                String endTime = reservationData[2].trim();

                try {
                    Date existingStart = timeFormat.parse(startTime);
                    Date existingEnd = timeFormat.parse(endTime);

                    // Check for date overlap
                    if (date.equals(selectedDate)) {
                        // Check for time overlap
                        if ((start.before(existingEnd) && end.after(existingStart)) ||
                                (existingStart.before(end) && existingEnd.after(start))) {
                            return true; // Time overlap found
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return false; // No time overlap found
    }

    private List<String> loadReservations() {
        List<String> reservations = new ArrayList<>();

        try {
            File file = new File("reservation.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String reservation = scanner.nextLine();
                reservations.add(reservation);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return reservations;
    }
    


    List<String> generateTimeOptions() {
        List<String> timeOptions = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date startDate;
        try {
            startDate = dateFormat.parse("07:00");
            Date endDate = dateFormat.parse("23:00");
            while (startDate.before(endDate)) {
                timeOptions.add(dateFormat.format(startDate));
                startDate = new Date(startDate.getTime() + 60 * 60000); // Add 1 hour
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeOptions;
    }
    
    
    private boolean checkReservation(List<String> reservations,String selectedDate, String selectedStartTime, String selectedEndTime) {
       

        // Check if the reservation exists
        for (String reservation : reservations) {
            String[] reservationData = reservation.split(",");
            if (reservationData.length == 3) {
                String date = reservationData[0].trim();
                String startTime = reservationData[1].trim();
                String endTime = reservationData[2].trim();

                // Compare the selected date, start time, and end time with the reservation data
                if (date.equals(selectedDate) && startTime.equals(selectedStartTime) && endTime.equals(selectedEndTime)) {
                    return true; // Reservation exists
                }
            }
        }
        return false; // No reservation found
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AVAILABILITYCHECKING window = new AVAILABILITYCHECKING();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    private class ButtonMouseAdapter extends MouseAdapter{
		JLabel label;
		JLabel bigLabel;
		public ButtonMouseAdapter(JLabel label, JLabel bigLabel) {
			this.label = label;
			this.bigLabel = bigLabel;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			label.setVisible(false);
			bigLabel.setVisible(true);
		}
		public void mouseExited(MouseEvent e) {
			label.setVisible(true);
			bigLabel.setVisible(false);
		}
		public void mousePressed(MouseEvent e) {
			label.setVisible(true);
			bigLabel.setVisible(false);
		}
		public void mouseReleased(MouseEvent e) {
			label.setVisible(false);
			bigLabel.setVisible(true);
		}	
    }
}