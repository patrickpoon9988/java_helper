package com.example.lingua_try;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PracticePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PracticePage extends Fragment implements AudioListAdapter.OnItemListClickListener{

    private ImageButton back;
    private ImageButton go_record;
    private String recordfile;
    private String recordpath;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private boolean isPlaying;
    private OkHttpClient okHttpClient = new OkHttpClient();

    public PracticePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practice_page, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        go_record = view.findViewById(R.id.practice_start);
        back = view.findViewById(R.id.back_exercise_fragment);
        Button upload = view.findViewById(R.id.upload_recording);

        recordpath = getActivity().getExternalFilesDir("/").getAbsolutePath();   // the path is a string

        File[] allFiles;
        File directory = new File(recordpath);
        allFiles = directory.listFiles();


        RecyclerView audiolist = view.findViewById(R.id.audio_list);

        AudioListAdapter audioListAdapter = new AudioListAdapter(allFiles, this); // create adapter object
        audiolist.setLayoutManager(new LinearLayoutManager(getContext()));
        audiolist.setAdapter(audioListAdapter); // set adapter


        go_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pop_up_window, null);  // LayoutInflater is never directly used. it actually the same as oncreate view

                // can be used to display an arbitrary view. The popup window is a floating container that appears on top of the current activity.
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                final ColorDrawable dw = new ColorDrawable(0xb0080000);
                popupWindow.setBackgroundDrawable(dw);

                // finally show up your popwindow

                popupWindow.showAsDropDown(popupView, 0, 0);  // <----- make it alike popwindow (drop-down)

                // define your view here that found in popup_layout <--- important.
                Button close = popupView.findViewById(R.id.practice_dismiss);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                /// recording
                final Button record = popupView.findViewById(R.id.practice_record);

                record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                        View popupView2 = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pop_up_recording_now, null);
                        final PopupWindow popupWindow2 = new PopupWindow(popupView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                        popupWindow2.setBackgroundDrawable(dw);
                        popupWindow2.showAsDropDown(popupView2);

                        startRecording();

                        final Chronometer timer = popupView2.findViewById(R.id.chronometer_timer);
                        timer.start();

                        ImageButton recordEnd = popupView2.findViewById(R.id.record_end);
                        recordEnd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recorder.stop();
                                timer.stop();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setCancelable(false);
                                builder.setMessage("Save this Recordings? ");
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteRecording();
                                        dialog.cancel();
                                        popupWindow2.dismiss();

                                    }
                                });
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        recorder.release();
                                        dialog.cancel();
                                        popupWindow2.dismiss();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                        });

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_practicePage_to_exerciseFragment);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadRecording();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("failed", "failed");
                }
            }
        });
    }

    public void startRecording() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   hh-mm-ss", Locale.TAIWAN);
        Date now = new Date();
        recordfile = formatter.format(now) + ".wav";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(recordpath + "/" + recordfile);  // the path is created in recordpath;  the / and recordfile is the file name (it creates the new file at the same time)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    public void deleteRecording() {
        String filepath = recordpath + "/" + recordfile;
        File recordingFile = new File(filepath);
        recordingFile.delete();
    }

    @Override                 // implements interface method, must override it to finish implementation of interface
    public void onItemClick(File file, int position) {
        isPlaying = true;
        startPlay(file);
    }

    private void startPlay(File file) {
        player = new MediaPlayer();

        try {
            player.setDataSource(file.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void uploadRecording() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "login")
                .addFormDataPart("login_id", "testing")
                .addFormDataPart("password", "demo")
                .addFormDataPart("Recording", "audio", RequestBody.create(MediaType.parse("audio/wave"), new File(recordpath + "/" + "2021-05-12   07-15-51.wav")))
                .build();

        Request request = new Request.Builder()
                .url("http://ureplydev2.ureply.mobi/file_upload/file_upload.php")
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            // Login succeed
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("OkHttp success", result);
            }

            //Login failed
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHttp failure", e.toString());

            }
        });
        }
    }

