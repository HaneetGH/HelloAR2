package technorapper.com.helloar;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import junit.framework.TestResult;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arfragment);
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                    Anchor anchor = hitResult.createAnchor();

                    ModelRenderable.builder()
                            .setSource(this, Uri.parse("ArcticFox_Posed.sfb"))
                            .build()
                            .thenAccept(renderable -> addModelToScene(anchor,renderable))
                            .exceptionally(
                                    throwable -> {
                                        Toast toast =
                                                Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        return null;
                                    });
                });
    }

    private void addModelToScene(Anchor anchor, ModelRenderable renderable) {

        AnchorNode anchorNode=new AnchorNode(anchor);

        TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
        andy.setParent(anchorNode);
        andy.setRenderable(renderable);

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        andy.select();
    }
}
