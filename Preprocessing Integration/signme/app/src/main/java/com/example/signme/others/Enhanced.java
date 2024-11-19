package com.example.signme.others;

import com.example.signme.ConnectionClass;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Enhanced {
    public boolean insertEnhancedFrame(String frameId, String emailId, String videoId, String enhancedFramesUrl) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertQuery = "INSERT INTO enhanced_frame (FRAME_ID, EMAIL_ID, VIDEO_ID, ENHANCED_FRAMES_URL) VALUES (?, ?, ?, ?)";
        try {
            // Establish the connection using Cloud_Connection
            ConnectionClass ConnectionClass = new ConnectionClass();
            connection = ConnectionClass.CONN();

            if (connection != null) {
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, frameId);
                preparedStatement.setString(2, emailId);
                preparedStatement.setString(3, videoId);
                preparedStatement.setString(4, enhancedFramesUrl);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Data inserted successfully!");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while inserting data!");
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
