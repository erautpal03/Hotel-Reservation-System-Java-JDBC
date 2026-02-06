package HotelReservation;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "your_password"; //Enter Your Password!!


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println();
                System.out.println("===== Hotel Reservation System =====");
                System.out.println("1. Reserve a Room");
                System.out.println("2. View All Reservations");
                System.out.println("3. Get Room Number by Reservation ID");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        connection.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine(); 

            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter Room Number: ");
            int roomNumber = scanner.nextInt();

            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.next();

            String query = "INSERT INTO reservations (guest_name, room_no, contact_number) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

            Statement stmt = connection.createStatement();
            int affectedRows = stmt.executeUpdate(query);

            if (affectedRows > 0) {
                System.out.println("Reservation completed successfully!");
            } else {
                System.out.println("Reservation could not be completed. Please try again.");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void viewReservations(Connection connection) {
        try {
            String query = "SELECT * FROM reservations";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.printf("%-5s %-15s %-10s %-15s %-20s%n",
                    "ID", "Guest Name", "Room No", "Contact", "Date");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int room = rs.getInt("room_no");
                String contact = rs.getString("contact_number");
                String date = rs.getTimestamp("reservation_date").toString();

                System.out.printf("%-5d %-15s %-10d %-15s %-20s%n",
                        id, name, room, contact, date);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Reservation ID: ");
            int reservationId = scanner.nextInt();

            String query = "SELECT room_no FROM reservations WHERE reservation_id = " + reservationId;

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                int roomNumber = rs.getInt("room_no");
                System.out.println("Assigned Room Number: " + roomNumber);
            } else {
                System.out.println("No reservation found with the given ID.");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Reservation ID to delete: ");
            int id = scanner.nextInt();

            String query = "DELETE FROM reservations WHERE reservation_id = " + id;

            Statement stmt = connection.createStatement();
            int rowsDeleted = stmt.executeUpdate(query);

            if (rowsDeleted > 0) {
                System.out.println("Reservation cancelled successfully.");
            } else {
                System.out.println("No reservation found with the given ID.");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Reservation ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            System.out.print("Enter new Guest Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter new Room Number: ");
            int room = scanner.nextInt();

            System.out.print("Enter new Contact Number: ");
            String contact = scanner.next();

            String query = "UPDATE reservations SET " +
                    "guest_name = '" + name + "', " +
                    "room_no = " + room + ", " +
                    "contact_number = '" + contact + "'" +
                    " WHERE reservation_id = " + id;

            Statement stmt = connection.createStatement();
            int rowsUpdated = stmt.executeUpdate(query);

            if (rowsUpdated > 0) {
                System.out.println("Reservation updated successfully!");
            } else {
                System.out.println("No reservation found with the given ID.");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while (i != 0) {
            System.out.print(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using the Hotel Reservation System. Have a great day!");
    }
}


