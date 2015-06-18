package com.example.sonic.fspotter.extras;

/**
 * Created by sonic on 18.06.15.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class Blur {
    private static final float BLUR_RADIUS = 25f;

    private final RenderScript rs;
    ScriptIntrinsicBlur script;
    private Allocation input;
    private Allocation output;
    private boolean configured = false;
    private Bitmap tmp = null;
    int[] pixels = null;


    public static Blur newInstance(Context context) {
        return new Blur(context);
    }

    private Blur(Context context) {
        rs = RenderScript.create(context);
    }

    public Bitmap blur(Bitmap image) {
        if (image == null)
            return null;

        image = RGB565toARGB888(image);
        if (!configured) {
            input = Allocation.createFromBitmap(rs, image);
            output = Allocation.createTyped(rs, input.getType());
            script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(BLUR_RADIUS);
            configured = true;
        } else
            input.copyFrom(image);

        script.setInput(input);
        script.forEach(output);
        output.copyTo(image);

        return image;
    }

    private Bitmap RGB565toARGB888(Bitmap img) {
        int numPixels = img.getWidth()* img.getHeight();

        //Create a Bitmap of the appropriate format.
        if (tmp == null) {
            tmp = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
            pixels = new int[numPixels];
        }

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Set RGB pixels.
        tmp.setPixels(pixels, 0, tmp.getWidth(), 0, 0, tmp.getWidth(), tmp.getHeight());

        return tmp;
    }
}
