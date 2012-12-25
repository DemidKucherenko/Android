package com.example.dictionary;
//��� ������� ��� � ���������. ������� � System.out. ������� ��� ������� public String translate(String S). GL & HF
//����� ���������, 2539 ��� ����

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends Activity {
	DrawSource drawSource;

	static public String translate(String S) throws IOException, IllegalStateException, URISyntaxException {
		 BufferedReader in = null;
		  String ans = "";
	        try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    String adress = "http://translate.reference.com/translate?query=" + S + "&src=en&dst=ru";
                    request.setURI(new URI(adress));
                    HttpResponse response ;
                    try{
                    response = client.execute(request);
                    }
                    catch (Exception ex){
                         response = null;
                    }
                    in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    String page = sb.toString();

                   String help = "<span class=\"BAB_CPPOSStyle\">";
                   int j = -1;
                    for (int i  = 0; i < page.length() - help.length(); i++) {
                        if (page.substring(i, i + help.length()).equals(help)) {
                            j = i;
                            break;
                        }
                    }

                    while (page.charAt(j) != '<' || page.charAt(j+1) != '/') {
                        j++;
                    }


                    j += 7;

                    while (page.charAt(j) != ';') {
                        ans += page.charAt(j);
                        j++;
                    }
	            

	            } catch (Exception ex){
                ans=null;
            }finally
             {
	            if (in != null) {
	                try {
	                    in.close();
	                    } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	         }

	   
	        return ans;			
	}

	class DrawSource extends SurfaceView implements Runnable {
		Thread thread = null;
		boolean running;

		public DrawSource(Context context) {
			super(context);
		}

		public void resume() {
			running = true;
			thread = new Thread(this);
			thread.start();
		}

		public void pause() {
			running = false;
			try {
				thread.join();
			} catch (InterruptedException ignore) {
			}
		}


		public void run() {

			try {
				String s = translate("Development");
				System.out.println(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		drawSource.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		drawSource.pause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		drawSource = new DrawSource(this);
		setContentView(drawSource);
	}


}
