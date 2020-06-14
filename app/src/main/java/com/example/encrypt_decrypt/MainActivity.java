package com.example.encrypt_decrypt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.encrypt_decrypt.Util.MyEncrypter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;
public class MainActivity extends AppCompatActivity {
    
    ImageView imageView;
    Button encrypt, decrypt;
    File myDir;
    private static final String FILE_NAME_ENC = "double";
    private static final String FILE_NAME_DEC = "double.png";

    String my_key = "ltVkg0knCiDc9K80";
    String my_spec_key = "BentHldIPoOEawVa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        imageView = findViewById( R.id.image );
        encrypt = findViewById( R.id.encrypt );
        decrypt = findViewById( R.id.decrypt );
        
        //Init Path
        myDir = new File( Environment.getExternalStorageDirectory().toString() + "/saved_images" );
        
        Dexter.withActivity( this ).withPermissions( new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE
        } ).withListener( new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                encrypt.setEnabled( true );
                decrypt.setEnabled( true );
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText( MainActivity.this, "You must enable permission!", Toast.LENGTH_SHORT ).show();
            }
        } ).check();
        
        encrypt.setOnClickListener( view ->{
            //Convert drawable to bitmap
            Drawable drawable = ContextCompat.getDrawable( MainActivity.this, R.drawable.doble );
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream(  );
            bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream );
            InputStream inputStream = new ByteArrayInputStream( stream.toByteArray() );

            //Create file
            File outputFileEnc = new File( myDir, FILE_NAME_ENC );
            try {
                MyEncrypter.encryptToFile( my_key, my_spec_key
                        , inputStream, new FileOutputStream( outputFileEnc ) );
                Toast.makeText( this, "Encrypted!", Toast.LENGTH_SHORT ).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }

        } );
        
        decrypt.setOnClickListener( view -> {
            File outputFile = new File( myDir, FILE_NAME_DEC );
            File encFile = new File( myDir, FILE_NAME_ENC );
            try {
                MyEncrypter.decryptToFile( my_key, my_spec_key, new FileInputStream( encFile ), new FileOutputStream( outputFile ) );
                
                imageView.setImageURI( Uri.fromFile( outputFile ) );
                //If you want to delete image after decrypted uncomment below line
//               outputFile.delete();
                Toast.makeText( this, "Deprecated!", Toast.LENGTH_SHORT ).show();
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
        } );
    }
}
