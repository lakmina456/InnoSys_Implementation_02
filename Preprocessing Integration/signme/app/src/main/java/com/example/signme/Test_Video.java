package com.example.signme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class Test_Video extends AppCompatActivity {

    private static final String TAG = "Test_Video";
    private VideoView videoView;
    private ConnectionClass connectionClass;
    private ExecutorService executorService;
    private Connection con;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        videoView = findViewById(R.id.videoView);
        connectionClass = new ConnectionClass();

        // Get source and destination from the intent
        Intent intent = getIntent();
        String source = intent.getStringExtra("startingPoint");
        String destination = intent.getStringExtra("destination");

        // Play the specific video based on the route
        playVideo(source, destination);
/*
        int videoResId = getVideoResource(source, destination);

        // Play the video if a match is found

        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + videoResId);
        videoView.start();

            // Example data for insertion
            //int frameId = "1";
        int frameId = 1;
        String emailId = "example@example.com";
        String videoId = "v1"; //getVideoId(videoResId);
        String enhancedFrameUrl = "https://example.com/frames/enhanced_frame_001.jpg";

        // Insert data into the database
        insertEnhancedFrame(frameId, emailId, videoId, enhancedFrameUrl);
        generateAndInsertSQL();
*/
    }

    private void playVideo(String source, String destination) {
        String videoPath = getVideoPath(source, destination);
        if (videoPath != null) {
            Uri videoUri = Uri.parse(videoPath);
            videoView.setVideoURI(videoUri);

            // Add media controls for play/pause
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            // Start the video
            videoView.start();
        } else {
            // Handle unmatched routes (optional)
            System.out.println("No video available for the selected route.");
        }
    }

    private String getVideoPath(String source, String destination) {
        // Define route-to-video mapping
        if (source.equals("Matale Junction") && destination.equals("Anuradhapura New Town")) {
            return "android.resource://" + getPackageName() + "/" + R.raw.v1;
        } else if (source.equals("Saliyapura") && destination.equals("Rambewa")) {
            return "android.resource://" + getPackageName() + "/" + R.raw.v2;
        } else if (source.equals("Kekirawa") && destination.equals("Maradankadawala")) {
            return "android.resource://" + getPackageName() + "/" + R.raw.e4;
        }
        return null; // No match found
    }

    private int getVideoResource(String source, String destination) {
        if (source.equals("Matale Junction") && destination.equals("Anuradhapura New Town")) {
            return R.raw.v1;
        } else if (source.equals("Saliyapura") && destination.equals("Rambewa")) {
            return R.raw.v2;
        } else if (source.equals("Kekirawa") && destination.equals("Maradankadawala")) {
            return R.raw.v3;
        }
        return 0; // No match found
    }

    private String getVideoId(int videoResId) {
        if (videoResId == R.raw.v1) {
            return "v1";
        } else if (videoResId == R.raw.v2) {
            return "v2";
        } else if (videoResId == R.raw.v3) {
            return "v3";
        }
        return "UNKNOWN_VIDEO";
    }

    private void insertEnhancedFrame(int frameId, String emailId, String videoId, String enhancedFrameUrl) {
        //Connection connection = connectionClass.CONN();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
        try {
            con = connectionClass.CONN();
            if (con == null) {
                runOnUiThread(() -> showToast("Error in connection with server"));
                return;
            }


            String insertQuery = "INSERT INTO enhanced_frame (FRAME_ID, EMAIL_ID, VIDEO_ID, ENHANCED_FRAMES_URL) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, frameId);
                preparedStatement.setString(2, emailId);
                preparedStatement.setString(3, videoId);
                preparedStatement.setString(4, enhancedFrameUrl);

                int rowsAffected = preparedStatement.executeUpdate();

                runOnUiThread(() -> {
                    if (rowsAffected > 0) {
                        Log.i("Test_Video", "Data inserted successfully into enhanced_frame.");
                    } else {
                        Log.e("Test_Video", "Data insertion failed.");
                    } });
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> showToast("Error saving session: " + e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> showToast("Exception: " + e.getMessage()));
            }

            //runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_SHORT).show()); // Show connection status



        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
        // Close the database connection
        });
            //executorService.shutdown();



    }

    public void generateAndInsertSQL() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();

        // Load the Python module
        PyObject pyModule = py.getModule("sql"); // Name of your Python file without '.py'

        // Call the function and get SQL statements
        PyObject result = pyModule.callAttr(
                "sql",
                "isuru150@gmail.com", // email_id
                "v1",                // video_id
                "https://raw.githubusercontent.com/lakmina456/traffic/refs/heads/main/captured_frames/v1/", // base_url
                17600,                // frame_count
                0                 // current_row
        );

        // Process the list of SQL commands
        List<PyObject> sqlCommands = result.asList();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                con = connectionClass.CONN();
                
                    for (PyObject sqlCommand : sqlCommands) {
                        try (PreparedStatement stmt = con.prepareStatement(sqlCommand.toString())) {
                            stmt.executeUpdate();
                        }
                    }
                    con.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
