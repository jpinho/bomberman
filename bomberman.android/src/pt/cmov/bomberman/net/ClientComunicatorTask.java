package pt.cmov.bomberman.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class ClientComunicatorTask extends AsyncTask<String, Void, Integer> {
        private Socket client;
        private PrintWriter printwriter;
        @Override
		protected Integer doInBackground(String...strings) {
                // validate input parameters
                if (strings.length <= 0) {
                        return 0;
                }
                // connect to the server and send the message
                try {
                        client = new Socket("192.168.1.77", 4889);
                        printwriter = new PrintWriter(client.getOutputStream(),true);
                        printwriter.write(strings[0]);
                        printwriter.flush();
                        printwriter.close();
                        client.close();
                } catch (UnknownHostException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return 0;
        }
        protected void onPostExecute(Long result) {
                return;
        }
}
