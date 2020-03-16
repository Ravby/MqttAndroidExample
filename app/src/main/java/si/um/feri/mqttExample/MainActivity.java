package si.um.feri.mqttExample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.MqttHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String PUBLISH_TOPIC = BuildConfig.mqttPublishTopic;

    MqttHelper mqttHelper;
    EditText payload;
    TextView response;
    Button publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payload = findViewById(R.id.et_payload);
        response = findViewById(R.id.tv_message_received);
        publish = findViewById(R.id.btn_publish);
        publish.setOnClickListener(publishMessage);

        mqttHelper = new MqttHelper(getApplicationContext());

        mqttHelper.connect();
        startMqtt();
    }

    private void startMqtt() {
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w(TAG, "Connected");
            }

            @Override
            public void connectionLost(Throwable throwable) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w(TAG, mqttMessage.toString());
                response.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }

    private View.OnClickListener publishMessage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mqttHelper.publishToTopic(PUBLISH_TOPIC, payload.getText().toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttHelper.disconnect();
    }
}
