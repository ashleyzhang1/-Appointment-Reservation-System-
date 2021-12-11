package scheduler.model;

import scheduler.db.ConnectionManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class Appointment {
    private static int id;
    private final String Patient_name;
    private final String Caregiver_name;
    private final String Vaccine_name;
    private final Date Time;

    private Appointment(scheduler.model.Appointment.AppointmentBuilder builder) {
        this.id = builder.id;
        this.Patient_name = builder.Patient_name;
        this.Caregiver_name = builder.Caregiver_name;
        this.Vaccine_name = builder.Vaccine_name;
        this.Time = builder.Time;
    }
    public static int getMaxID() throws SQLException{
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        String getMAX = "Select MAX(ID) from Appointments";
        try {
            PreparedStatement statement = con.prepareStatement(getMAX);
            ResultSet getmax = statement.executeQuery();
            if (getmax.next()) {
                return getmax.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new SQLException("Error occured when getting max ID");
        } finally {
            cm.closeConnection();
        }
    }

    public static class AppointmentBuilder {
        private final int id;
        private final String Patient_name;
        private final String Caregiver_name;
        private final String Vaccine_name;
        private final Date Time;

        public AppointmentBuilder(int id, String Patient_name, String Caregiver_name, String Vaccine_name, Date Time) {
            this.id = id;
            this.Patient_name = Patient_name;
            this.Caregiver_name = Caregiver_name;
            this.Vaccine_name = Vaccine_name;
            this.Time = Time;
        }

        public scheduler.model.Appointment build() throws SQLException {
            return new scheduler.model.Appointment(this);
        }
    }

    public void saveToDB() throws SQLException {
        scheduler.db.ConnectionManager cm = new scheduler.db.ConnectionManager();
        Connection con = cm.createConnection();

        String addAppointment = "INSERT INTO Appointments VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(addAppointment);
            statement.setInt(1, this.id);
            statement.setString(2, this.Patient_name);
            statement.setString(3, this.Caregiver_name);
            statement.setString(4, this.Vaccine_name);
            statement.setDate(5, this.Time);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            cm.closeConnection();
        }
    }

}