package com.ivan.skripsi.skripsi_ivan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_Kamera extends Activity {

    Intent intent;
    Uri fileUri;
    Button btn_choose_image, btn_detect_image, btn_jarak_sebenarnya;
    ImageView imageView;
    TextView txtSatuanSatu, txtSatuanDua;
    EditText etJarakSebenarnya;
    Bitmap bitmap, decoded, bitmapGrayscale, bitmapBlack;
    LinearLayout layoutJarak;
    ProgressBar loadingBar;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;

    double jarakSebenarnya;

    ProgressDialog loading;

    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__kamera);

        btn_choose_image = (Button) findViewById(R.id.btn_choose_image);
        btn_detect_image = (Button) findViewById(R.id.btn_detect_image);
        btn_jarak_sebenarnya = (Button) findViewById(R.id.btn_input_jarak);
        layoutJarak = (LinearLayout) findViewById(R.id.layout_jarak);
        txtSatuanSatu = findViewById(R.id.txt_satuan_satu);
        txtSatuanDua = findViewById(R.id.txt_satuan_dua);
        loadingBar = findViewById(R.id.loading_bar);
        etJarakSebenarnya = findViewById(R.id.et_jarak_sebenarnya);

        imageView = (ImageView) findViewById(R.id.image_view);

        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_jarak_sebenarnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_detect_image.setVisibility(View.VISIBLE);
            }
        });

        btn_detect_image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etJarakSebenarnya.getText().toString())) {
                    jarakSebenarnya = Double.parseDouble(etJarakSebenarnya.getText().toString());
                } else {
                    Toast.makeText(Activity_Kamera.this, "Jarak sebenarnya belum diisi !", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingBar.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(convertToBinary(decoded));
                txtSatuanSatu.setText(Chain.mainMethod(convertToBinary(decoded), jarakSebenarnya)[0] + "\n" + Chain.mainMethod(convertToBinary(decoded), jarakSebenarnya)[1]);
                txtSatuanDua.setText(Chain.mainMethod(convertToBinary(decoded), jarakSebenarnya)[2] + "\n" + Chain.mainMethod(convertToBinary(decoded), jarakSebenarnya)[3]);
                loadingBar.setVisibility(View.GONE);
            }
        });

    }

    public Bitmap convertToBinary(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                //perhitungan proses bitmap to grayscale
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

                // use 128 as threshold, above -> white, below -> black
                //perhitungan proses grayscale to binary
                if (gray > 128) {
                    gray = 255;
                } else {
                    gray = 0;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        return bmOut;
    }

    private void selectImage() {
        StrictMode.VmPolicy.Builder builderCamera = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builderCamera.build());
        imageView.setImageResource(0);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Kamera.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    Log.e("GALERI", String.valueOf(Uri.parse(data.getData().toString())));
                    bitmap = MediaStore.Images.Media.getBitmap(Activity_Kamera.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Untuk menampilkan bitmap pada ImageView
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
        layoutJarak.setVisibility(View.VISIBLE);
    }

    // Untuk resize bitmap
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "2D-OD");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_Detector_" + timeStamp + ".jpg");

        return mediaFile;
    }

}