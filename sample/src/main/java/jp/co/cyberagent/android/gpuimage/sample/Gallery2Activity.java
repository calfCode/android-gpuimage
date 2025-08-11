package jp.co.cyberagent.android.gpuimage.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.co.cyberagent.android.gpuimage.sample.R;

public class Gallery2Activity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1;

    private FilterAdjuster filterAdjuster;
    private GPUImageView gpuImageView;
    private SeekBar seekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gpuImageView = findViewById(R.id.gpuimage);
        seekBar      = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (filterAdjuster != null) {
                    filterAdjuster.adjust(progress);
                    gpuImageView.requestRender();
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.button_choose_filter).setOnClickListener(v ->
                GPUImageFilterTools2.showDialog(this, filter -> {
                    switchFilterTo(filter);
                    gpuImageView.requestRender();
                }));

        findViewById(R.id.button_save).setOnClickListener(v -> saveImage());

        startPhotoPicker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                gpuImageView.setImage(data.getData());
            } else {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".jpg";
        gpuImageView.saveToPictures("GPUImage", fileName, uri ->
                Toast.makeText(this, "Saved: " + uri, Toast.LENGTH_SHORT).show());
    }

    private void switchFilterTo(GPUImageFilter filter) {
        if (gpuImageView.getFilter() == null ||
                !gpuImageView.getFilter().getClass().equals(filter.getClass())) {

            gpuImageView.setFilter(filter);
            filterAdjuster = new FilterAdjuster(filter);

            if (filterAdjuster.canAdjust()) {
                seekBar.setVisibility(View.VISIBLE);
                filterAdjuster.adjust(seekBar.getProgress());
            } else {
                seekBar.setVisibility(View.GONE);
            }
        }
    }
}