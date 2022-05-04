package com.example.automltranslation;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.automl.v1.AutoMlClient;
import com.google.cloud.automl.v1.ExamplePayload;
import com.google.cloud.automl.v1.ListModelEvaluationsRequest;
import com.google.cloud.automl.v1.ModelEvaluation;
import com.google.cloud.automl.v1.ModelName;

import java.io.IOException;
import java.io.InputStream;

import com.example.automltranslation.databinding.FragmentFirstBinding;
import com.google.cloud.automl.v1.PredictRequest;
import com.google.cloud.automl.v1.PredictResponse;
import com.google.cloud.automl.v1.PredictionServiceClient;
import com.google.cloud.automl.v1.PredictionServiceSettings;
import com.google.cloud.automl.v1.TextSnippet;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try (InputStream is = getResources().openRawResource(R.raw.key)) {
                    String txtToTranslate = binding.inputEditText.getText().toString();
                    String projectId = "add-project-id";
                    String modelId = "add-model-id";
                    CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(is));
                    PredictionServiceSettings predictionServiceSettings = PredictionServiceSettings.newBuilder().setCredentialsProvider(credentialsProvider).setTransportChannelProvider(PredictionServiceSettings.defaultTransportChannelProvider()).build();
                    PredictionServiceClient client = PredictionServiceClient.create(predictionServiceSettings);
                    // Get the full path of the model.
                    ModelName name = ModelName.of(projectId, "us-central1", modelId);

                    String content = txtToTranslate;

                    TextSnippet textSnippet = TextSnippet.newBuilder().setContent(content).build();
                    ExamplePayload payload = ExamplePayload.newBuilder().setTextSnippet(textSnippet).build();
                    PredictRequest predictRequest =
                            PredictRequest.newBuilder().setName(name.toString()).setPayload(payload).build();

                    PredictResponse response = client.predict(predictRequest);
                    TextSnippet translatedContent =
                            response.getPayload(0).getTranslation().getTranslatedContent();
                    binding.inputEditText2.setText(translatedContent.getContent());
                    //System.out.format("Translated Content: %s\n", translatedContent.getContent());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}